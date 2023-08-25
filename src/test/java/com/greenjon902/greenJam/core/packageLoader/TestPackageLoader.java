package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.Package;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

// TODO: Better checking system, maybe based on a tree?

/**
 * Resource information:
 * <br><br>
 * Simple_package just has files and submodules, and a package config file.
 * <br>
 * Package_with_subfolder_files is a duplicate of the simple package, but we add ext/*.jam, and add the appropriate
 * sections to the package config.
 * <br>
 * Package_with_changing_regex is different.
 * <br><br>
 * See the tests for the names of these items.
 */
public class TestPackageLoader {
	private static java.io.File getFile(String path) throws FileNotFoundException {
		URL resource = TestPackageLoader.class.getClassLoader().getResource(path);
		if (resource == null) throw new FileNotFoundException("Could not find " + path);
		return new java.io.File(resource.getFile());
	}

	/**
	 * Checks that the module version of the simple package was loaded correctly. You can also tell it to do
	 * subfolder with files as that is based on it.
	 */
	public static void checkSimpleModuleContents(Module module, boolean withSubfolderFiles, String rootName) {
		Set<File> files1 = new HashSet<>();
		Set<Module> modules = new HashSet<>();
		Set<File> files2 = new HashSet<>();
		Set<File> files3 = new HashSet<>();

		files1.add(new LoadedFile.Builder() {{
			name("main.jam");
		}}.build());
		files1.add(new LoadedFile.Builder() {{
			name("test.jam");
		}}.build());
		if (withSubfolderFiles) {
			files1.add(new LoadedFile.Builder() {{
				name("x_actualSkills.jam");
			}}.build());
		}

		files2.add(new LoadedFile.Builder() {{
			name("bar.jam");
		}}.build());
		if (withSubfolderFiles) {
			files2.add(new LoadedFile.Builder() {{
				name("lies.jam");
			}}.build());
		}

		if (withSubfolderFiles) {
			files3.add(new LoadedFile.Builder() {{
				name("baz.jam");
			}}.build());
		}

		modules.add(new LoadedModule.Builder() {{
			name("foo");
			files(files2);
		}}.build());
		if (withSubfolderFiles) {
			modules.add(new LoadedModule.Builder() {{
				name("xxx_module_xxx");
				files(files3);
			}}.build());
		}

		LoadedModule expected = new LoadedModule.Builder() {{
			name(rootName);
			files(files1);
			modules(modules);
		}}.build();

		Assertions.assertEquals(expected, module);
	}


	@Test
	public void testLoadModuleInto() throws IOException {
		java.io.File simpleModule = getFile("com/greenjon902/greenJam/core/packageLoader/simple_package");
		// Use that as it can pretend to be a module

		LoadedModule.Builder builder = new LoadedModule.Builder();
		Module module = PackageLoader.loadModuleInto(builder, new Toml(), simpleModule, PackageLoader.defaultConfig());
		Assertions.assertEquals("", module.name());  // Was never supplied

		// Next check method wants the name set
		module = PackageLoader.loadModuleInto(builder, new Toml(), simpleModule, PackageLoader.defaultConfig());
		checkSimpleModuleContents(module, false, "");
	}

	@Test
	public void testSimplePackage() throws IOException {
		LoadedPackage p = PackageLoader.loadSinglePackage(getFile("com/greenjon902/greenJam/core/packageLoader/simple_package"));

		// Check some things here as not checked in check_simple_module_contents
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertEquals(Set.of("GreenJon902"), p.authors());

		p.compareOnlyAsModule = true; // As we just did package comparisons
		checkSimpleModuleContents(p, false, "simple_package");
	}

	@Test
	public void testPackageWithSubfolderFiles() throws IOException {
		LoadedPackage p = PackageLoader.loadSinglePackage(getFile("com/greenjon902/greenJam/core/packageLoader/package_with_subfolder_files"));
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertEquals(Set.of("GreenJon902"), p.authors());

		p.compareOnlyAsModule = true; // As we just did package comparisons
		checkSimpleModuleContents(p, true, "package_with_subfolder_files");
	}

	@Test
	public void testPackageWithChangingRegex() throws IOException {
		Package p = PackageLoader.loadSinglePackage(getFile("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex"));

		LoadedPackage expected = new LoadedPackage.Builder() {{
			name("package_with_changing_regex");
			files(Set.of(
					new LoadedFile.Builder() {{
						name("a.jam");
					}}.build(),
					new LoadedFile.Builder() {{
						name("b.jam");
					}}.build()
			));
			modules(Set.of(
					new LoadedModule.Builder() {{
						name("mod");
						files(Set.of(
								new LoadedFile.Builder() {{
									name("c.jam");
								}}.build()
						));
					}}.build()
			));
		}}.build();

		Assertions.assertEquals(expected, p);
	}

	@Test
	public void testLoadPackageWithVersion() throws IOException {
		Package p = PackageLoader.loadSinglePackage(getFile("com/greenjon902/greenJam/core/packageLoader/package_with_version"));

		Assertions.assertEquals("package_with_version-1.0.1", p.name());
	}

	@Test
	public void testLoadPackageWithSimpleDependencies() throws IOException {
		Package p = PackageLoader.loadSinglePackage(getFile("com/greenjon902/greenJam/core/packageLoader/dependency_tree_resources/main"));

		LoadedPackage expected = new LoadedPackage.Builder() {{
			description("The main package for this test");
			authors(Set.of("GreenJon902"));
			dependencies(Set.of(
					new LoadedPackageReference("jon", "1.2.3"),
					new LoadedPackageReference("aj", "1.0.0"),
					new LoadedPackageReference("omega", "1.0.0"),
					new LoadedPackageReference("cat", "1.0.0")
			));
		}}.build();

		Assertions.assertEquals(expected, p);
	}

	/**
	 * Tests fully loading a package that has dependencies, some with sub-dependencies which may be new or may be also
	 * used elsewhere. Some may also be of different versions.
	 */
	@Test
	public void testLoadPackageWithDependencyTree() throws IOException {
		PackageLoader.loadedPackagesFor(getFile("com/greenjon902/greenJam/core/packageLoader/dependency_tree_resources/main"));
		// TODO: put doc at top for this dependency.
		//  This entails the version changes, circular dependents, reused dependents, and only once used dependents,
		//  and duplicate dependents
	}
}
