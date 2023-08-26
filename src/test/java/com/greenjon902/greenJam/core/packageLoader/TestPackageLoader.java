package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.Package;
import com.greenjon902.greenJam.core.PackageList;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Resource information:
 * <br><br>
 * Simple_package just has files and submodules, and a package config file.
 * <br>
 * Package_with_subfolder_files is a duplicate of the simple package, but we add ext/*.jam, and add the appropriate
 * sections to the package config.
 * <br>
 * Package_with_changing_regex is different.
 * <br>
 * Dependency_tree_resources contains many different packages that require each other to be loaded, but
 * there are circular requirements which must not cause infinite recursion. Some also require multiple versions or
 * locally rename dependants.
 * <br>
 * Name_overwritting_module is just a module with a toml file that changes its name to overwritten.
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

	private Properties properties;  // So tests can modify them for special operations

	@BeforeEach
	public void setup() {
		properties = System.getProperties();
	}

	@AfterEach
	public void teardown() {
		PackageList.getInstance().clear();
		System.setProperties(properties);
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
	public void testLoadNameOverwrittingModule() throws IOException {
		Module m = PackageLoader.loadModule(getFile("com/greenjon902/greenJam/core/packageLoader/name_overwritting_module"), PackageLoader.defaultConfig());

		Assertions.assertEquals("overwritten", m.name());
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
		System.setProperty("JAMPATH", getFile("com/greenjon902/greenJam/core/packageLoader/dependency_tree_resources").toString());

		Map<String, Map<String, Package>> packages = new HashMap<>() {{
			put("", new HashMap<>() {{
				put("", new LoadedPackage.Builder() {{
					description("The main package for this test");
					authors(Set.of("GreenJon902"));
					dependencies(Set.of(
							new LoadedPackageReference("aj", "1.0.0"),
							new LoadedPackageReference("omega", "1.0.0"),
							new LoadedPackageReference("cat", "1.0.0"),
							new LoadedPackageReference("jon", "1.2.3")
					));
				}}.build());
			}});
			put("aj", new HashMap<>() {{
				put("1.0.0", new LoadedPackage.Builder() {{
					name("aj-1.0.0");
					description("Probably Bda");
					authors(Set.of("Mumkins", "me"));
					dependencies(Set.of(
							new LoadedPackageReference("ultra", "1.0.0"),
							new LoadedPackageReference("omega", "1.0.0"),
							new LoadedPackageReference("cat", "1.0.0"),
							new LoadedPackageReference("jon", "1.2.4")
					));
				}}.build());
			}});
			put("omega", new HashMap<>() {{
				put("1.0.0", new LoadedPackage.Builder() {{
					name("omega-1.0.0");
					description("Big nose");
					dependencies(Set.of(
							new LoadedPackageReference("aj", "1.0.0"),
							new LoadedPackageReference("jon", "1.2.3")
					));
				}}.build());
			}});
			put("ultra", new HashMap<>() {{
				put("1.0.0", new LoadedPackage.Builder() {{
					name("ultra-1.0.0");
					dependencies(Set.of(
							new LoadedPackageReference("dave", "")
					));
				}}.build());
			}});
			put("dave", new HashMap<>() {{
				put("", new LoadedPackage.Builder() {{
					name("dave");
					authors(Set.of("Dave1", "Dave2", "Dave3", "Dave4", "Dave4's Tutor"));
				}}.build());
			}});
			put("jon", new HashMap<>() {{
				put("1.2.3", new LoadedPackage.Builder() {{
					name("jon-1.2.3");
					dependencies(Set.of(
							new LoadedPackageReference("aj", "1.0.0"),
							new LoadedPackageReference("omega", "1.0.0"),
							new LoadedPackageReference("cat", "1.0.0"),
							new LoadedPackageReference("dave", "")
					));
				}}.build());
				put("1.2.4", new LoadedPackage.Builder() {{
					name("jon-1.2.4");
					dependencies(Set.of(
							new LoadedPackageReference("aj", "1.0.0"),
							new LoadedPackageReference("omega", "1.0.0"),
							new LoadedPackageReference("cat", "1.0.0"),
							new LoadedPackageReference("cat", "sussy_cat", "1.0.0-sus")
					));
				}}.build());
			}});
			put("cat", new HashMap<>() {{
				put("1.0.0", new LoadedPackage.Builder() {{
					name("cat-1.0.0");
					dependencies(Set.of(
							new LoadedPackageReference("aj", "1.0.0"),
							new LoadedPackageReference("jon", "1.2.3")
					));
				}}.build());
				put("1.0.0-sus", new LoadedPackage.Builder() {{
					name("cat-1.0.0-sus");
					description("uwu");
					dependencies(Set.of(
							new LoadedPackageReference("jon", "hot", "1.2.3")
					));
				}}.build());
			}});
		}};
		Package expected_main = packages.get("").get("");

		Package main = PackageLoader.loadedPackagesFor(getFile("com/greenjon902/greenJam/core/packageLoader/dependency_tree_resources/main"));

		Assertions.assertEquals(expected_main, main);
		PackageList.getInstance().assertEquals(packages, Assertions::assertEquals);
	}
}
