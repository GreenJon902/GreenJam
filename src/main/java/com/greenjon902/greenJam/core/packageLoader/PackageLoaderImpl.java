package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.Module;
import com.greenjon902.greenJam.api.core.PackageList;
import com.greenjon902.greenJam.api.core.packageLoader.PackageLoader;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import com.greenjon902.greenJam.utils.StackedClassBase;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.greenjon902.greenJam.utils.TomlUtils.*;

// TODO: Load other packages (bases, overwrides)
// TODO: load a reader for each file
// TODO: Use regexes group system to pull only the desired part from the file name, maybe save other parts to a field?

/**
 * See {@link #loadSinglePackage(File)}
 */
public class PackageLoaderImpl implements PackageLoader {
	private final LoadingConfig lc;

	public PackageLoaderImpl() {
		lc = LoadingConfig.getDefault();
	}

	@Override
	public LoadedPackage loadPackagesFor(File root) throws IOException {
		PackageList packageList = PackageList.getInstance();  // So we don't need to run it a lot

		// Load the package info and save it
		LoadedPackage rootPackage = loadSinglePackage(root);
		packageList.add(rootPackage.toml().getString("name", ""), rootPackage.toml().getString("version", ""), rootPackage);
		// Add this to package list before to prevent circular dependents looping

		// Now we can check if any new dependencies need to be loaded
		Set<PackageReference> dependencies = rootPackage.dependencies();
		for (PackageReference dependency : dependencies) {
			if (!packageList.hasPackage(dependency)) {
				if (dependency instanceof LoadedPackageReference loadedDependency) {
					File source = locatePackageFromReference(loadedDependency);

					if (source == null) {
						throw new RuntimeException("Could not find package with formatted name \"" +
								loadedDependency.formatName() + "\" in JAMPATH");
					}

					// Not loaded yet, but checks have passed, so now we need to load it
					loadPackagesFor(source);  // But we don't care about this one's return value

				} else {
					throw new RuntimeException("Expected dependency to be of type LoadedPackageReference, not " +
							dependency.getClass().getSimpleName());
				}
			}
		}

		return rootPackage;
	}

	/**
	 * Loads a singular package's info (with its files and modules) from the package config file. Then load it as if
	 * it was a module (ignoring module config), see
	 * {@link #loadModuleInto(LoadedModule.Builder, Toml, File)}.
	 * <br><br>
	 * Note: This does not add it to the {@link PackageList}!
	 *
	 * @param root The root folder of the package
	 * @return The built package
	 */
	public LoadedPackage loadSinglePackage(File root) throws IOException {
		// Load toml and read any information in, then load data as if it was a module
		Toml toml = loadIfExists(new File(root, lc.packageConfigPath()));
		LoadedPackage.Builder packageBuilder = new LoadedPackage.Builder();
		packageBuilder.name(PackageReference.formatName(toml.getString("name", ""),
				toml.getString("version", "")));
		setIfNotNullSet("authors", packageBuilder::authors, String.class, toml);
		setIfNotNullString("description", packageBuilder::description, toml);

		Toml dependencyToml = toml.getTable("Dependencies");
		if (dependencyToml != null) {
			//noinspection unchecked
			packageBuilder.dependencies((Set<PackageReference>) (Set<?>) makeDependencyReferences(dependencyToml));
		}

		return (LoadedPackage) loadModuleInto(packageBuilder, toml, root);
	}

	/**
	 * Makes the {@link PackageReference} array from the given dependencies.
	 * See the documentation for how the toml should be structured.
	 * // TODO: MAKE SAID DOCUMENTATION
	 *
	 * @param dependencies The dependency table from the toml file
	 * @return The package references
	 */
	private Set<LoadedPackageReference> makeDependencyReferences(Toml dependencies) {
		Set<LoadedPackageReference> references = new HashSet<>();

		// Loop through dependencies
		Map<String, Object> dependencyMap = dependencies.toMap();
		for (String key : dependencyMap.keySet()) {
			Object object = dependencyMap.get(key);

			// Get dependency type
			if (object instanceof Map<?,?> dependency) {  // Map, so just one with some information
				//noinspection unchecked
				references.add(makeDependencyReference((Map<String, Object>) dependency, key));

			} else if (object instanceof ArrayList<?> versions) {  // List so multiple versions of dependency
				for (Object version : versions) {
					//noinspection unchecked
					references.add(makeDependencyReference((Map<String, Object>) version, key));
				}

			} else if (object instanceof String) {  // String so just version
				// We will put this in a map for the function to work with
				references.add(makeDependencyReference(new HashMap<>() {{ put("version", object); }}, key));

			} else {
				throw new IllegalArgumentException("Toml given has illegal types, dependency=" + key);
			}
		}
		return references;
	}

	/**
	 * Makes a singular reference for {@link #makeDependencyReferences(Toml)}, by using the map, and entering
	 * default information.
	 * @param realName The actual name of the dependency (e.g. in toml with Dependencies.cat, cat would be this)
	 * @param dependency The dependency information
	 * @return The created reference
	 */
	private LoadedPackageReference makeDependencyReference(Map<String, Object> dependency, String realName) {
		String version = (String) dependency.getOrDefault("version", "");
		String referName = (String) dependency.getOrDefault("name", realName);
		return new LoadedPackageReference(realName, referName, version);
	}

	/**
	 * Loads a module from a folder, this function loads things from the module.toml file, see
	 * {@link #loadModuleInto(LoadedModule.Builder, Toml, File)} for loading the file
	 * and submodule information.
	 *
	 * @param folder The root folder of the module
	 * @return The built module
	 */
	protected LoadedModule loadModule(File folder) throws IOException {
		// Load toml and read any information in, then load files and submodules
		Toml toml = loadIfExists(new File(folder, lc.moduleConfigPath()));
		LoadedModule.Builder moduleBuilder = new LoadedModule.Builder();
		moduleBuilder.name(toml.getString("name", folder.getName()));

		return loadModuleInto(moduleBuilder, toml, folder);
	}

	/**
	 * Loads the file and submodule information from the disk, it also applies any items in the supplied toml to a new
	 * level on the stack which is popped at the end.
	 * This will only load files and submodules that fit the pattern in loading config, however it can find files in
	 * subdirectories that still fit the pattern.
	 * This will also put the given toml into the module.
	 *
	 * @param moduleBuilder The builder of the current module that is being loaded
	 * @param toml          The toml of the current module being loaded
	 * @param folder        The folder of the current module being loaded
	 * @return The built module
	 */
	protected LoadedModule loadModuleInto(LoadedModule.Builder moduleBuilder, Toml toml, File folder) throws IOException {
		lc.push();
		applyConfig(toml, lc);

		moduleBuilder.toml(toml);

		// Use these to check if something should be read into the compiler as a file or a module
		Pattern[] filePatterns = Arrays.stream(lc.fileRegexs()).map(Pattern::compile).toArray(Pattern[]::new);
		Pattern[] modulePatterns = Arrays.stream(lc.moduleRegexs()).map(Pattern::compile).toArray(Pattern[]::new);

		Set<LoadedFile> newFiles = new HashSet<>();
		Set<LoadedModule> newModules = new HashSet<>();

		// For each path check if it is valid and then load them
		try (Stream<Path> paths = Files.walk(folder.toPath())) {
			paths.forEach(
					path -> {

						// Convert to relative path
						String pathString = path.toString();
						assert pathString.startsWith(folder.toString());
						pathString = pathString.substring(folder.toString().length());
						if (pathString.isEmpty()) {
							return;
						}
						pathString = pathString.substring(1);  // Remove slash

						final String finalStringPath = pathString;  // For predicate to work

						try {

							// Now we can do our checks and load any children
							File file = path.toFile();
							if (file.isFile() &&
									Arrays.stream(filePatterns).anyMatch(matcher -> matcher.matcher(finalStringPath).matches())) {
								LoadedFile newFile = loadFile(file, lc);
								newFiles.add(newFile);

							} else if (file.isDirectory() &&
									Arrays.stream(modulePatterns).anyMatch(matcher -> matcher.matcher(finalStringPath).matches())) {
								LoadedModule newModule = loadModule(file);
								newModules.add(newModule);
							}

						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
			);
		}

		// Put the found items in the builder and build it
		moduleBuilder.files((Set<com.greenjon902.greenJam.api.core.File>) (Set<?>) newFiles);
		moduleBuilder.modules((Set<Module>) (Set<?>) newModules);
		LoadedModule module = moduleBuilder.build();

		lc.pop();
		return module;
	}

	/**
	 * Loads a files information, which is only its name at the momment.
	 *
	 * @param folder The path of the file
	 * @param lc     The current loading config
	 * @return The built file
	 */
	private LoadedFile loadFile(File folder, LoadingConfig lc) throws IOException {
		LoadedFile.Builder fileBuilder = new LoadedFile.Builder();
		fileBuilder.name(folder.getName());

		return fileBuilder.build();
	}

	/**
	 * Updates the config with any relevant items set in the toml.
	 * Note: This does not push a new level to the stack
	 *
	 * @param toml The toml to use
	 * @param lc   The current loading config
	 */
	private void applyConfig(Toml toml, LoadingConfig lc) { // TODO: Load these from a file
		setIfNotNullString("Loader.package-config-path", lc::packageConfigPath, toml);  // I know this is pointless, but it's important to me
		setIfNotNullString("Loader.module-config-path", lc::moduleConfigPath, toml);
		setIfNotNullString("Loader.file-regex", lc::fileRegex, toml);
		setIfNotNullArray("Loader.file-regexs", lc::fileRegexs, String.class, toml);
		setIfNotNullString("Loader.module-regex", lc::moduleRegex, toml);
		setIfNotNullArray("Loader.module-regexs", lc::moduleRegexs, String.class, toml);
	}


	/**
	 * Holds the current settings for how the package should be loaded.
	 * This inherits StackedClassBase so modifications can be reverted when switching to the next modules.
	 * <p>
	 * See the documentation on package and module config for the use of each function.
	 * // TODO: Make said documentation
	 */
	protected static class LoadingConfig extends StackedClassBase {
		/**
		 * Makes a loading config with the default values.
		 *
		 * @return The created loading config
		 */
		private static LoadingConfig getDefault() {
			// Make config with default values
			LoadingConfig lc = new LoadingConfig();
			lc.packageConfigPath("jam.toml");
			lc.moduleConfigPath("mod.toml");
			lc.fileRegex("[^/]+?.jam");
			lc.moduleRegex("[^/]+?");
			return lc;
		}

		public LoadingConfig() {
			super(4);
		}

		public String packageConfigPath() {
			return (String) get(0);
		}
		public void packageConfigPath(String string) {
			set(0, string);
		}

		public String moduleConfigPath() {
			return (String) get(1);
		}
		public void moduleConfigPath(String string) {
			set(1, string);
		}

		public String[] fileRegexs() {
			return (String[]) get(2);
		}
		public void fileRegexs(String[] string) {set(2, string);}
		public void fileRegex(String string) {set(2, new String[] {string});}

		public String[] moduleRegexs() {
			return (String[]) get(3);
		}
		public void moduleRegexs(String[] string) {
			set(3, string);
		}
		public void moduleRegex(String string) {set(3, new String[] {string});}
	}

	/**
	 * Tries to locate the path of a package from a package reference and the current JAMPATH. It uses the
	 * {@link PackageReference#formatName()} as the folder name to check it exists.
	 * @param packageReference The reference of the package to search for
	 * @return The path to the package or null if it wasn't found
	 */
	public File locatePackageFromReference(PackageReference packageReference) {
		String name = packageReference.formatName();

		// Gets paths of where the packages are stored
		String[] paths = System.getProperty("JAMPATH", "").split(":");

		for (String path : paths) {
			File potentialPath = new File(path, name);
			if (potentialPath.exists()) {
				return potentialPath;
			}
		}
		return null;
	}
}