package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.PackageList;
import com.greenjon902.greenJam.core.PackageReference;
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

// TODO: Load other packages (dependencies, bases, overwrides)
// TODO: load a reader for each file
// TODO: Use regexes group system to pull only the desired part from the file name, maybe save other parts to a field?

/**
 * See {@link #loadSinglePackage(File)}
 */
public class PackageLoader {
	/**
	 * Loads the package stored at root, and then recursively loads their requirements (until all packages are found) into
	 * the package list.
	 *
	 * @param root The root of the first package to load
	 * @return The package stored at root
	 */
	public static LoadedPackage loadedPackagesFor(File root) throws IOException {
		//Toml toml = loadIfExists(new File(root.getParent(), "jon-1.2.4/jam.toml"));
		//System.out.println(toml.toMap());
		//makeDependencyReferences(toml.getTable("Dependencies"));

		//LoadedPackage rootPackage = loadSinglePackage(root);


		return null;
	}

	/**
	 * Loads a singular package's info (with its files and modules) from the package config file. Then load it as if
	 * it was a module (ignoring module config), see
	 * {@link #loadModuleInto(LoadedModule.Builder, Toml, File, LoadingConfig)}.
	 * <br><br>
	 * Note: This does not add it to the {@link PackageList}!
	 *
	 * @param root The root folder of the package
	 * @return The built package
	 */
	public static LoadedPackage loadSinglePackage(File root) throws IOException {
		LoadingConfig lc = defaultConfig();

		// Load toml and read any information in, then load data as if it was a module
		Toml toml = loadIfExists(new File(root, lc.packageConfigPath()));
		LoadedPackage.Builder packageBuilder = new LoadedPackage.Builder();
		packageBuilder.name(PackageList.formatName(toml.getString("name", ""),
				toml.getString("version", "")));
		setIfNotNullSet("authors", packageBuilder::authors, String.class, toml);
		setIfNotNullString("description", packageBuilder::description, toml);

		Toml dependencyToml = toml.getTable("Dependencies");
		if (dependencyToml != null) {
			//noinspection unchecked
			packageBuilder.dependencies((Set<PackageReference>) (Set<?>) makeDependencyReferences(dependencyToml));
		}

		return (LoadedPackage) loadModuleInto(packageBuilder, toml, root, lc);
	}

	/**
	 * Makes the {@link com.greenjon902.greenJam.core.PackageReference} array from the given dependencies.
	 * See the documentation for how the toml should be structured.
	 * // TODO: MAKE SAID DOCUMENTATION
	 *
	 * @param dependencies The dependency table from the toml file
	 * @return The package references
	 */
	private static Set<LoadedPackageReference> makeDependencyReferences(Toml dependencies) {
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
	private static LoadedPackageReference makeDependencyReference(Map<String, Object> dependency, String realName) {
		String version = (String) dependency.getOrDefault("version", "");
		String referName = (String) dependency.getOrDefault("name", realName);
		return new LoadedPackageReference(realName, referName, version);
	}

	/**
	 * Loads a module from a folder, this function loads things from the module.toml file, see
	 * {@link #loadModuleInto(LoadedModule.Builder, Toml, File, LoadingConfig)} for loading the file
	 * and submodule information.
	 *
	 * @param folder The root folder of the module
	 * @param lc     The current loading config
	 * @return The built module
	 */
	private static LoadedModule loadModule(File folder, LoadingConfig lc) throws IOException {
		// Load toml and read any information in, then load files and submodules
		Toml toml = loadIfExists(new File(folder, lc.moduleConfigPath()));
		LoadedModule.Builder moduleBuilder = new LoadedModule.Builder();
		moduleBuilder.name(folder.getName());

		return loadModuleInto(moduleBuilder, toml, folder, lc);
	}

	/**
	 * Loads the file and submodule information from the disk, it also applies any items in the supplied toml to a new
	 * level on the stack which is popped at the end.
	 * This will only load files and submodules that fit the pattern in loading config, however it can find files in
	 * subdirectories that still fit the pattern.
	 * This will also set the submodules' and files' parents to the correct module.
	 *
	 * @param moduleBuilder The builder of the current module that is being loaded
	 * @param toml          The toml of the current module being loaded
	 * @param folder        The folder of the current module being loaded
	 * @param lc            The current loading config
	 * @return The built module
	 */
	protected static LoadedModule loadModuleInto(LoadedModule.Builder moduleBuilder, Toml toml, File folder, LoadingConfig lc) throws IOException {
		lc.push();
		applyConfig(toml, lc);

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
								LoadedModule newModule = loadModule(file, lc);
								newModules.add(newModule);
							}

						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
			);
		}

		// Put the found items in the builder and build it
		moduleBuilder.files((Set<com.greenjon902.greenJam.core.File>) (Set<?>) newFiles);
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
	private static LoadedFile loadFile(File folder, LoadingConfig lc) throws IOException {
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
	private static void applyConfig(Toml toml, LoadingConfig lc) { // TODO: Load these from a file
		setIfNotNullString("Loader.package-config-path", lc::packageConfigPath, toml);  // I know this is pointless, but it's important to me
		setIfNotNullString("Loader.module-config-path", lc::moduleConfigPath, toml);
		setIfNotNullString("Loader.file-regex", lc::fileRegex, toml);
		setIfNotNullArray("Loader.file-regexs", lc::fileRegexs, String.class, toml);
		setIfNotNullString("Loader.module-regex", lc::moduleRegex, toml);
		setIfNotNullArray("Loader.module-regexs", lc::moduleRegexs, String.class, toml);
	}

	/**
	 * Makes a loading config with the default values.
	 *
	 * @return The created loading config
	 */
	protected static LoadingConfig defaultConfig() {
		// Make config with default values
		LoadingConfig lc = new LoadingConfig();
		lc.packageConfigPath("jam.toml");
		lc.moduleConfigPath("mod.toml");
		lc.fileRegex("[^/]+?.jam");
		lc.moduleRegex("[^/]+?");
		return lc;
	}


	/**
	 * Holds the current settings for how the package should be loaded.
	 * This inherits StackedClassBase so modifications can be reverted when switching to the next modules.
	 * <p>
	 * See the documentation on package and module config for the use of each function.
	 * // TODO: Make said documentation
	 */
	protected static class LoadingConfig extends StackedClassBase {
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
}