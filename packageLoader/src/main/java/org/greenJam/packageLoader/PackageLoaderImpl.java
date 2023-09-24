package org.greenJam.packageLoader;

import com.moandjiezana.toml.Toml;
import org.greenJam.api.Module;
import org.greenJam.api.Package;
import org.greenJam.api.PackageList;
import org.greenJam.api.PackageReference;
import org.greenJam.api.PackageLoader;
import org.greenJam.packageLoader.basedPackageHelpers.BasedPackage;
import org.greenJam.packageLoader.rawConfig.*;
import org.greenJam.utils.StackedClassBase;
import org.greenJam.utils.inputStream.FileInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.greenJam.utils.TomlUtils.loadIfExists;

/**
 * See {@link #loadAndDependants()}
 */
public class PackageLoaderImpl implements PackageLoader {
	private final LoadingConfig lc;
	public final File root;
	public final PackageList packageList;

	/**
	 * See {@link #PackageLoaderImpl(File, LoadingConfig, PackageList)}
	 */
	public PackageLoaderImpl(File root, PackageList packageList) {
		this(root, LoadingConfig.getDefault(), packageList);
	}

	/**
	 * Create a package loader for a specific package.
	 * @param root The root of the first package to load
	 * @param lc The loading config to use
	 * @param packageList The package list to load into
	 */
	private PackageLoaderImpl(File root, LoadingConfig lc, PackageList packageList) {
		this.root = root;
		this.lc = lc;
		this.packageList = packageList;
	}

	@Override
	public Package loadAndDependants() throws IOException {
		// Load the package info and save it
		LoadedPackage rootPackage = loadSinglePackage();

		// Set to a crash package to stop this package getting loaded in the case of a circular dependant,
		// we will add properly later
		packageList.add(rootPackage.rawConfig().name, rootPackage.rawConfig().version, new CrashPackage());

		// Now we can check if any new dependencies or bases need to be loaded.
		List<LoadedPackageReference> baseReferences = makeBasesReferences(rootPackage.rawConfig().bases);
		Set<PackageReference> references = new HashSet<>(rootPackage.dependencies());  // HashSet so its modifiable
		references.addAll(baseReferences);

		for (PackageReference reference : references) {
			if (!packageList.hasPackage(reference)) {
				if (reference instanceof LoadedPackageReference loadedReference) {
					File source = locatePackageFromReference(loadedReference);

					if (source == null) {
						throw new RuntimeException("Could not find package with formatted name \"" +
								loadedReference.formatName() + "\" in JAMPATH");
					}
					// Not loaded yet, but checks have passed, so now we need to load it
					new PackageLoaderImpl(source, packageList).loadAndDependants();  // But we don't care about this one's return value

				} else {
					throw new RuntimeException("Expected reference to be of type LoadedPackageReference, not " +
							reference.getClass().getSimpleName());
				}
			}
		}


		// Now the bases are loaded, we can build the package with bases
		Package package_ = rootPackage;
		if (!baseReferences.isEmpty()) { // If no bases, then don't do anything
			Package[] bases = baseReferences.stream().map(packageList::get).toArray(Package[]::new);

			package_ = new BasedPackage(false, rootPackage, bases);
		}
		packageList.add(rootPackage.rawConfig().name, rootPackage.rawConfig().version, package_, true);


		// Now the package is fully made, we can check for and do an override
		PackageLinkRawConfig override;
		if ((override = rootPackage.rawConfig().override) != null) {
			packageList.add(override.name, override.version, package_, true);
		}


		return package_;
	}

	/**
	 * Loads a singular package's info (with its files and modules) from the package config file. Then load it as if
	 * it was a module (ignoring module config), see
	 * {@link #loadModuleInto(LoadedModule.Builder, ModuleRawConfig, File)}.
	 * <br><br>
	 * Note: This does not add it to the {@link PackageList}!
	 * Note: This also does not compute bases or overrides.
	 *
	 * @return The built package
	 */
	public LoadedPackage loadSinglePackage() throws IOException {
		// Load toml and read any information in, then load data as if it was a module
		Toml toml = loadIfExists(new File(root, lc.packageConfigPath()));
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		LoadedPackage.Builder packageBuilder = new LoadedPackage.Builder();
		packageBuilder.name(PackageReference.formatName(rawConfig.name, rawConfig.version));
		packageBuilder.authors(rawConfig.authors);
		packageBuilder.description(rawConfig.description);

		//noinspection unchecked
		packageBuilder.dependencies((Set<PackageReference>) (Set<?>) makeDependencyReferences(rawConfig.dependencies));

		return (LoadedPackage) loadModuleInto(packageBuilder, rawConfig, root);
	}

	/**
	 * Makes the {@link PackageReference} list from the given bases.
	 * @param bases The list of base information
	 * @return The package references
	 */
	private List<LoadedPackageReference> makeBasesReferences(List<PackageLinkRawConfig> bases) {
		List<LoadedPackageReference> references = new ArrayList<>();

		// Loop through dependencies
		for (PackageLinkRawConfig base : bases) {
			references.add(new LoadedPackageReference(base.name, base.version));
		}
		return references;
	}

	/**
	 * Makes the {@link PackageReference} set from the given dependencies.
	 * @param dependencies The dependency table from the toml file
	 * @return The package references
	 */
	private Set<LoadedPackageReference> makeDependencyReferences(Map<String, PackageRawConfig.DependencySet> dependencies) {
		Set<LoadedPackageReference> references = new HashSet<>();

		// Loop through dependencies
		for (String key : dependencies.keySet()) {
			PackageRawConfig.DependencySet versions = dependencies.get(key);

			for (PackageLinkRawConfig version : versions) {
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
	private LoadedPackageReference makeDependencyReference(PackageLinkRawConfig dependency, String realName) {
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
	 * Checks whether a regex in the list matches to the given relative path. If it does, it does the substitution and
	 * returns it.
	 * @param regexs The {@link RegexRawConfig}s
	 * @param path The relative path
	 * @return The new path or null
	 */
	private static @Nullable String tryMatch(RegexRawConfig[] regexs, String path) {
		for (RegexRawConfig regex : regexs) {
			Matcher matcher = Pattern.compile(regex.regex).matcher(path);
			if (matcher.matches()) {
				return matcher.replaceFirst(regex.substitution);
			}
		}
		return null;
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

		Set<LoadedFile> newFiles = new HashSet<>();
		Set<LoadedModule> newModules = new HashSet<>();

		Path folderPath = folder.toPath();

		// For each path check if it is valid and then load them
		try (Stream<Path> paths = Files.walk(folderPath)) {
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

						String relativePath = folderPath.relativize(path).toString();

						try {
							// Now we can do our checks and load any children
							File file = path.toFile();
							if (file.isFile()) {
								String name = tryMatch(lc.fileRegex(), relativePath);
								if (name != null) {
									LoadedFile newFile = loadFile(file, name);
									newFiles.add(newFile);
								}

							} else if (file.isDirectory()) {
								String name = tryMatch(lc.moduleRegex(), relativePath);
								if (name != null) {
									LoadedModule newModule = loadModule(file);
									newModules.add(newModule);
								}
							}

						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
			);
		}

		// Put the found items in the builder and build it
		moduleBuilder.files((Set<org.greenJam.api.File>) (Set<?>) newFiles);
		moduleBuilder.modules((Set<Module>) (Set<?>) newModules);
		LoadedModule module = moduleBuilder.build();

		lc.pop();
		return module;
	}

	/**
	 * Loads a files information, which is only its name at the momment.
	 *
	 * @param file The path of the file
	 * @param name The name to use for the file
	 * @return The built file
	 */
	private LoadedFile loadFile(File file, String name) throws IOException {
		LoadedFile.Builder fileBuilder = new LoadedFile.Builder();
		fileBuilder.name(name);
		fileBuilder.stream(new FileInputStream(file));

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
	private <T, C extends Collection<T>> void applyHelper(Consumer<T[]> consumer, @NotNull AdaptableCollectionBase<T, C> value, IntFunction<T[]> generator) {
		if (!value.isEmpty()) {
			consumer.accept(value.toArray(generator));
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
		applyHelper(lc::fileRegex, rawConfig.loader.fileRegex, RegexRawConfig[]::new);
		applyHelper(lc::moduleRegex, rawConfig.loader.moduleRegex, RegexRawConfig[]::new);
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
			lc.fileRegex(new RegexRawConfig[]{new RegexRawConfig("([^/]+?)\\.jam")});
			lc.moduleRegex(new RegexRawConfig[]{new RegexRawConfig("([^/]+?)")});
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

		public RegexRawConfig[] fileRegex() {
			return (RegexRawConfig[]) get(2);
		}
		public void fileRegex(RegexRawConfig[] regex) {set(2, regex);}

		public RegexRawConfig[] moduleRegex() {
			return (RegexRawConfig[]) get(3);
		}
		public void moduleRegex(RegexRawConfig[] regex) {
			set(3, regex);
		}
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