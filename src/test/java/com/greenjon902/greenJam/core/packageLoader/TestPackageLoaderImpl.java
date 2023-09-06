package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.Module;
import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.api.core.*;
import com.greenjon902.greenJam.api.core.packageLoader.PackageLoader;
import com.greenjon902.greenJam.core.PackageListImpl;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.ModuleRawConfig;
import com.greenjon902.greenJam.utils.FileInputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;


// TODO: Find better way of making comparison data

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
public class TestPackageLoaderImpl {
	private static java.io.File getFile(String path) throws FileNotFoundException {
		URL resource = TestPackageLoaderImpl.class.getClassLoader().getResource(path);
		if (resource == null) throw new FileNotFoundException("Could not find " + path);
		return new java.io.File(resource.getFile());
	}

	private static InputStream getJamFileStream(String path) throws IOException {
		java.io.File file = getFile(path);
		return new FileInputStream(file.getAbsoluteFile());
	}

	/**
	 * Checks that the module version of the simple package was loaded correctly. You can also tell it to do
	 * subfolder with files as that is based on it.
	 */
	public static void checkSimpleModuleContents(Module module, boolean withSubfolderFiles, String rootName) throws IOException {
		String path = withSubfolderFiles ? "com/greenjon902/greenJam/core/packageLoader/package_with_subfolder_files/" :
				"com/greenjon902/greenJam/core/packageLoader/simple_package/";

		Set<File> files1 = new HashSet<>();
		Set<Module> modules = new HashSet<>();
		Set<File> files2 = new HashSet<>();
		Set<File> files3 = new HashSet<>();

		files1.add(new LoadedFile.Builder() {{
			name("main");
			stream(getJamFileStream(path + "main.jam"));
		}}.build());
		files1.add(new LoadedFile.Builder() {{
			name("test");
			stream(getJamFileStream(path + "test.jam"));
		}}.build());
		if (withSubfolderFiles) {
			files1.add(new LoadedFile.Builder() {{
				name("x_actualSkills");
				stream(getJamFileStream(path + "ext/x_actualSkills.jam"));
			}}.build());
		}

		files2.add(new LoadedFile.Builder() {{
			name("bar");
			stream(getJamFileStream(path + "foo/bar.jam"));
		}}.build());
		if (withSubfolderFiles) {
			files2.add(new LoadedFile.Builder() {{
				name("lies");
				stream(getJamFileStream(path + "foo/ext/lies.jam"));
			}}.build());
		}

		if (withSubfolderFiles) {
			files3.add(new LoadedFile.Builder() {{
				name("baz");
				stream(getJamFileStream(path + "xxx_module_xxx/baz.jam"));
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
		System.setProperties(properties);
	}


	@Test
	public void testLoadModuleInto() throws IOException {
		PackageList packageList = new PackageListImpl();

		java.io.File simpleModule = getFile("com/greenjon902/greenJam/core/packageLoader/simple_package");
		PackageLoaderImpl pl = new PackageLoaderImpl(simpleModule, packageList);
		// Use that as it can pretend to be a module

		LoadedModule.Builder builder = new LoadedModule.Builder();
		Module module = pl.loadModuleInto(builder, new ModuleRawConfig(), simpleModule);
		Assertions.assertEquals("", module.name());  // Was never supplied

		// Next check method wants the name set
		module = pl.loadModuleInto(builder, new ModuleRawConfig(), simpleModule);
		checkSimpleModuleContents(module, false, "");
	}

	@Test
	public void testSimplePackage() throws IOException {
		PackageList packageList = new PackageListImpl();

		PackageLoaderImpl pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/simple_package"), packageList);
		LoadedPackage p = pl.loadSinglePackage();

		// Check some things here as not checked in check_simple_module_contents
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertEquals(Set.of("GreenJon902"), p.authors());

		p.compareOnlyAsModule = true; // As we just did package comparisons
		checkSimpleModuleContents(p, false, "simple_package");
	}

	@Test
	public void testPackageWithSubfolderFiles() throws IOException {
		PackageList packageList = new PackageListImpl();

		PackageLoaderImpl pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/package_with_subfolder_files"), packageList);
		LoadedPackage p = pl.loadSinglePackage();
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertEquals(Set.of("GreenJon902"), p.authors());

		p.compareOnlyAsModule = true; // As we just did package comparisons
		checkSimpleModuleContents(p, true, "package_with_subfolder_files");
	}

	@Test
	public void testPackageWithChangingRegex() throws IOException {
		PackageList packageList = new PackageListImpl();

		PackageLoaderImpl pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex"), packageList);
		Package p = pl.loadSinglePackage();

		LoadedPackage expected = new LoadedPackage.Builder() {{
			name("package_with_changing_regex");
			files(Set.of(
					new LoadedFile.Builder() {{
						name("a.jam");
						stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex/a.jam"));
					}}.build(),
					new LoadedFile.Builder() {{
						name("b.jam");
						stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex/b.jam"));
					}}.build()
			));
			modules(Set.of(
					new LoadedModule.Builder() {{
						name("mod");
						files(Set.of(
								new LoadedFile.Builder() {{
									name("c.jam");
									stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex/mod/c.jam"));
								}}.build(),
								new LoadedFile.Builder() {{
									name("e");
									stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex/mod/e.jam"));
								}}.build()
						));
					}}.build(),
					new LoadedModule.Builder() {{
						name("mod2");
						files(Set.of(
								new LoadedFile.Builder() {{
									name("f.jam");
									stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex/mod2/f.jam"));
								}}.build(),
								new LoadedFile.Builder() {{
									name("g");
									stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex/mod2/g.jam"));
								}}.build()
						));
					}}.build()
			));
		}}.build();

		Assertions.assertEquals(expected, p);
	}

	@Test
	public void testLoadPackageWithVersion() throws IOException {
		PackageList packageList = new PackageListImpl();

		PackageLoaderImpl pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/package_with_version"), packageList);
		Package p = pl.loadSinglePackage();

		Assertions.assertEquals("package_with_version-1.0.1", p.name());
	}

	@Test
	public void testLoadNameOverwrittingModule() throws IOException {
		PackageList packageList = new PackageListImpl();

		PackageLoaderImpl pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/name_overwritting_module"), packageList);
		Module m = pl.loadModule(pl.root);

		Assertions.assertEquals("overwritten", m.name());
	}

	@Test
	public void testLoadPackageWithSimpleDependencies() throws IOException {
		PackageList packageList = new PackageListImpl();

		PackageLoaderImpl pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/dependency_tree_resources/main"), packageList);
		Package p = pl.loadSinglePackage();

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
		PackageList packageList = new PackageListImpl();
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

		PackageLoader pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/dependency_tree_resources/main"), packageList);
		Package main = pl.loadAndDependants();

		Assertions.assertEquals(expected_main, main);
		Assertions.assertEquals(packages, packageList.getPackages());
	}

	@Test
	public void testDependencyOverride() throws IOException {
		PackageList packageList = new PackageListImpl();

		System.setProperty("JAMPATH", getFile("com/greenjon902/greenJam/core/packageLoader/dependency_override_resources").toString());

		Set<File> expected = Set.of(
				new LoadedFile.Builder() {{
					name("a");
					stream(getJamFileStream("com/greenjon902/greenJam/core/packageLoader/dependency_override_resources/main/a.jam"));
				}}.build()
		);

		PackageLoader pl = new PackageLoaderImpl(getFile("com/greenjon902/greenJam/core/packageLoader/dependency_override_resources/main"), packageList);
		Package main = pl.loadAndDependants();
		Assertions.assertEquals(expected, main.files());

		Package base = packageList.get("base", "1.0.0");
		Assertions.assertEquals(expected, base.files());
	}
}
