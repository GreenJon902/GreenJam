package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.Package;
import com.greenjon902.greenJam.utils.StackedClassBase;
import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.stream.Stream;

import static com.greenjon902.greenJam.utils.TomlUtils.*;

// TODO: Load other packages (dependencies, bases, overwrides)
// TODO: load a reader for each file

/**
 * See {@link #load(File)}
 */
public class PackageLoader {
	/**
	 * Loads the package info (with its files and modules) from the package config file. Then load it as if it was
	 * a module (ignoring module config), see
	 * {@link #load_module_into(LoadedModule.Builder, Toml, File, LoadingConfig)}.
	 *
	 * @param root The root folder of the package
	 * @return The built package
	 */
	@NotNull
	public static Package load(File root) throws IOException {
		LoadingConfig lc = default_config();

		// Load toml and read any information in, then load data as if it was a module
		Toml toml = load_if_exists(new File(root, lc.package_config_path()));
		LoadedPackage.Builder packageBuilder = new LoadedPackage.Builder();
		packageBuilder.name(root.getName());
		set_if_not_null_string("display-name", packageBuilder::display_name, toml);
		set_if_not_null_array("authors", packageBuilder::authors, String.class, toml);
		set_if_not_null_string("description", packageBuilder::description, toml);

		return (LoadedPackage) load_module_into(packageBuilder, toml, root, lc);
	}

	/**
	 * Loads a module from a folder, this function loads things from the module.toml file, see
	 * {@link #load_module_into(LoadedModule.Builder, Toml, File, LoadingConfig)} for loading the file
	 * and submodule information.
	 *
	 * @param folder The root folder of the module
	 * @param lc The current loading config
	 * @return The built module
	 * @throws IOException When an IO exception occurs
	 */
	private static LoadedModule load_module(File folder, LoadingConfig lc) throws IOException {
		// Load toml and read any information in, then load files and submodules
		LoadedModule.Builder moduleBuilder = new LoadedModule.Builder();
		moduleBuilder.name(folder.getName());
		Toml toml = load_if_exists(new File(folder, lc.module_config_path()));

		return load_module_into(moduleBuilder, toml, folder, lc);
	}

	/**
	 * Loads the file and submodule information from the disk, it also applies any items in the supplied toml to a new
	 * level on the stack which is popped at the end.
	 * This will only load files and submodules that fit the pattern in loading config, however it can find files in
	 * subdirectories that still fit the pattern.
	 * This will also set the submodules' and files' parents to the correct module.
	 *
	 * @param moduleBuilder The builder of the current module that is being loaded
	 * @param toml The toml of the current module being loaded
	 * @param folder The folder of the current module being loaded
	 * @param lc The current loading config
	 * @return The built module
	 * @throws IOException When an IO exception occurs
	 */
	private static LoadedModule load_module_into(LoadedModule.Builder moduleBuilder, Toml toml, File folder, LoadingConfig lc) throws IOException {
		lc.push();
		apply_config(toml, lc);

		// Use these to check if something should be read into the compiler as a file or a module
		PathMatcher file_matcher = FileSystems.getDefault().getPathMatcher("glob:" + folder.getAbsolutePath() + "/" + lc.file_glob());
		PathMatcher module_matcher = FileSystems.getDefault().getPathMatcher("glob:" + folder.getAbsolutePath() + "/"  + lc.module_glob());

		ArrayList<LoadedFile> newFiles = new ArrayList<>();
		ArrayList<LoadedModule> newModules = new ArrayList<>();

		// For each path check if it is valid and then load them
		try (Stream<Path> paths = Files.walk(folder.toPath())) {
			paths.forEach(
					path -> {
						File file = path.toFile();
						if (file.isFile() && file_matcher.matches(path)) {
							try {
								LoadedFile newFile = load_file(file, lc);
								newFiles.add(newFile);
							} catch (IOException e) {
								throw new RuntimeException(e);
							}

						} else if (file.isDirectory() && module_matcher.matches(path)) {
							try {
								LoadedModule newModule = load_module(file, lc);
								newModules.add(newModule);
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}
					}
			);
		}

		// Put the found items in the builder and build it
		moduleBuilder.files(newFiles.toArray(LoadedFile[]::new));
		moduleBuilder.modules(newModules.toArray(LoadedModule[]::new));
		LoadedModule module = moduleBuilder.build();

		// Set the parents of all the found items. We need to do this after building as ModuleBuilder#files takes a
		// Module, not as ModuleBuilder.
		newFiles.forEach(newFile -> newFile.setParent(module));
		newModules.forEach(newModule -> newModule.setParent(module));

		lc.pop();
		return module;
	}

	/**
	 * Loads a files information, which is only its name at the momment.
	 *
	 * @param folder The path of the file
	 * @param lc The current loading config
	 * @return The built file
	 * @throws IOException When an IO exception occurs
	 */
	private static LoadedFile load_file(File folder, LoadingConfig lc) throws IOException {
		LoadedFile.LoadedFileBuilder fileBuilder = new LoadedFile.LoadedFileBuilder();
		fileBuilder.name(folder.getName());

		return fileBuilder.build();
	}

	/**
	 * Updates the config with any relevant items set in the toml.
	 * Note: This does not push a new level to the stack
	 *
	 * @param toml The toml to use
	 * @param lc The current loading config
	 */
	private static void apply_config(Toml toml, LoadingConfig lc) { // TODO: Load these from a file
		set_if_not_null_string("package-config-path", lc::package_config_path, toml);  // I know this is pointless, but it's important to me
		set_if_not_null_string("module-config-path", lc::module_config_path, toml);
		set_if_not_null_string("file-glob", lc::file_glob, toml);
		set_if_not_null_string("module-glob", lc::module_glob, toml);
	}

	/**
	 * Makes a loading config with the default values.
	 * @return The created loading config
	 */
	private static LoadingConfig default_config() {
		// Make config with default values
		LoadingConfig lc = new LoadingConfig();
		lc.package_config_path("jam.toml");
		lc.module_config_path("mod.toml");
		lc.file_glob("*.jam");
		lc.module_glob("*");
		return lc;
	}
}

/**
 * Holds the current settings for how the package should be loaded.
 * This inherits StackedClassBase so modifications can be reverted when switching to the next modules.
 *
 * See the documentation on package and module config for the use of each function.
 * // TODO: Make said documentation
 */
class LoadingConfig extends StackedClassBase {
	public LoadingConfig() {
		super(4);
	}

	public String package_config_path() {return (String) get(0);}
	public void package_config_path(String string) {set(0, string);}

	public String module_config_path() {return (String) get(1);}
	public void module_config_path(String string) {set(1, string);}

	public String file_glob() {return (String) get(2);}
	public void file_glob(String string) {set(2, string);}

	public String module_glob() {return (String) get(3);}
	public void module_glob(String string) {set(3, string);}
}