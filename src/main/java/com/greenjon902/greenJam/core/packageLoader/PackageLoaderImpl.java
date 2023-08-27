package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.Module;
import com.greenjon902.greenJam.api.core.PackageList;
import com.greenjon902.greenJam.api.core.packageLoader.PackageLoader;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.DependencyList;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.DependencyRawConfig;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.ModuleRawConfig;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.PackageRawConfig;
import com.greenjon902.greenJam.utils.StackedClassBase;
import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.greenjon902.greenJam.utils.TomlUtils.loadIfExists;

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
		packageList.add(rootPackage.rawConfig().name, rootPackage.rawConfig().version, rootPackage);
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
	 * {@link #loadModuleInto(LoadedModule.Builder, ModuleRawConfig, File)}.
	 * <br><br>
	 * Note: This does not add it to the {@link PackageList}!
	 *
	 * @param root The root folder of the package
	 * @return The built package
	 */
	public LoadedPackage loadSinglePackage(File root) throws IOException {
		// Load toml and read any information in, then load data as if it was a module
		Toml toml = loadIfExists(new File(root, lc.packageConfigPath()));
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		LoadedPackage.Builder packageBuilder = new LoadedPackage.Builder();
		packageBuilder.name(PackageReference.formatName(rawConfig.name, rawConfig.version));
		packageBuilder.authors(new HashSet<>(rawConfig.authors));
		packageBuilder.description(rawConfig.description);

		//noinspection unchecked
		packageBuilder.dependencies((Set<PackageReference>) (Set<?>) makeDependencyReferences(rawConfig.dependencies));

		return (LoadedPackage) loadModuleInto(packageBuilder, rawConfig, root);
	}

	/**
	 * Makes the {@link PackageReference} array from the given dependencies.
	 * See the documentation for how the toml should be structured.
	 * // TODO: MAKE SAID DOCUMENTATION
	 *
	 * @param dependencies The dependency table from the toml file
	 * @return The package references
	 */
	private Set<LoadedPackageReference> makeDependencyReferences(Map<String, DependencyList> dependencies) {
		Set<LoadedPackageReference> references = new HashSet<>();

		// Loop through dependencies
		for (String key : dependencies.keySet()) {
			DependencyList versions = dependencies.get(key);

			for (DependencyRawConfig version : versions) {
				references.add(makeDependencyReference(version, key));
			}
		}
		return references;
	}

	/**
	 * Makes a singular reference for {@link #makeDependencyReferences(Map)}, by using the map, and entering
	 * default information.
	 * @param realName The actual name of the dependency (e.g. in toml with Dependencies.cat, cat would be this)
	 * @param dependency The dependency information
	 * @return The created reference
	 */
	private LoadedPackageReference makeDependencyReference(DependencyRawConfig dependency, String realName) {
		String version = dependency.version;
		String referName = dependency.name.isEmpty() ? realName : dependency.name;
		return new LoadedPackageReference(realName, referName, version);
	}

	/**
	 * Loads a module from a folder, this function loads things from the module.toml file, see
	 * {@link #loadModuleInto(LoadedModule.Builder, ModuleRawConfig, File)} for loading the file
	 * and submodule information.
	 *
	 * @param folder The root folder of the module
	 * @return The built module
	 */
	protected LoadedModule loadModule(File folder) throws IOException {
		// Load toml and read any information in, then load files and submodules
		Toml toml = loadIfExists(new File(folder, lc.moduleConfigPath()));
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		LoadedModule.Builder moduleBuilder = new LoadedModule.Builder();
		moduleBuilder.name(rawConfig.name.isEmpty() ? folder.getName() : rawConfig.name);

		return loadModuleInto(moduleBuilder, rawConfig, folder);
	}

	/**
	 * Loads the file and submodule information from the disk, it also applies any items in the supplied toml to a new
	 * level on the stack which is popped at the end.
	 * This will only load files and submodules that fit the pattern in loading config, however it can find files in
	 * subdirectories that still fit the pattern.
	 * This will also put the given toml into the module.
	 *
	 * @param moduleBuilder The builder of the current module that is being loaded
	 * @param rawConfig          The {@link ModuleRawConfig} of the current module being loaded
	 * @param folder        The folder of the current module being loaded
	 * @return The built module
	 */
	protected LoadedModule loadModuleInto(LoadedModule.Builder moduleBuilder, ModuleRawConfig rawConfig, File folder) throws IOException {
		lc.push();
		applyConfig(rawConfig, lc);

		moduleBuilder.rawConfig(rawConfig);

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
	 * Helper for {@link #applyConfig(ModuleRawConfig, LoadingConfig)}.
	 * This checks whether the string is empty, if it is not it gives it to the consumer.
	 *
	 * @param consumer The consumer to run
	 * @param value The string to check
	 */
	private void applyHelper(Consumer<String> consumer, @NotNull String value) {
		if (!value.isEmpty()) {
			consumer.accept(value);
		}
	}

	/**
	 * Helper for {@link #applyConfig(ModuleRawConfig, LoadingConfig)}.
	 * This checks whether the list is empty, if it is not it gives it to the consumer.
	 *
	 * @param consumer The consumer to run
	 * @param value The list to check
	 */
	private void applyHelper(Consumer<String[]> consumer, @NotNull List<String> value) {
		if (!value.isEmpty()) {
			consumer.accept(value.toArray(String[]::new));
		}
	}

	/**
	 * Updates the config with any relevant items set in the toml.
	 * Note: This does not push a new level to the stack
	 *
	 * @param rawConfig The {@link ModuleRawConfig} to use
	 * @param lc   The current loading config
	 */
	private void applyConfig(ModuleRawConfig rawConfig, LoadingConfig lc) {
		applyHelper(lc::packageConfigPath, rawConfig.loader.packageConfigPath);  // I know this one is pointless, but it's important to me
		applyHelper(lc::moduleConfigPath, rawConfig.loader.moduleConfigPath);
		applyHelper(lc::fileRegex, rawConfig.loader.fileRegex);
		applyHelper(lc::fileRegexs, rawConfig.loader.fileRegexs);
		applyHelper(lc::moduleRegex, rawConfig.loader.moduleRegex);
		applyHelper(lc::moduleRegexs, rawConfig.loader.moduleRegexs);
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