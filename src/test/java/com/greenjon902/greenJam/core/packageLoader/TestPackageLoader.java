package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.Package;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

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
	private static java.io.File get_file(String path) {
		return new java.io.File(TestPackageLoader.class.getClassLoader().getResource(path).getFile());
	}

	/**
	 * Checks that the module version of the simple package was loaded correctly. You can also tell it to do
	 * subfolder with files as that is based on it.
	 */
	public static void check_simple_module_contents(Module module, boolean with_subfolder_files, String root_name) {
		ArrayList<File> files1 = new ArrayList<>();
		ArrayList<Module> modules = new ArrayList<>();
		ArrayList<File> files2 = new ArrayList<>();
		ArrayList<File> files3 = new ArrayList<>();

		files1.add(new LoadedFile.Builder() {{
			name("main.jam");
		}}.build());
		files1.add(new LoadedFile.Builder() {{
			name("test.jam");
		}}.build());
		if (with_subfolder_files) {
			files1.add(new LoadedFile.Builder() {{
				name("x_actualSkills.jam");
			}}.build());
		}

		files2.add(new LoadedFile.Builder() {{
			name("bar.jam");
		}}.build());
		if (with_subfolder_files) {
			files2.add(new LoadedFile.Builder() {{
				name("lies.jam");
			}}.build());
		}

		if (with_subfolder_files) {
			files3.add(new LoadedFile.Builder() {{
				name("baz.jam");
			}}.build());
		}

		modules.add(new LoadedModule.Builder() {{
			name("foo");
			files(files2.toArray(File[]::new));
		}}.build());
		if (with_subfolder_files) {
			modules.add(new LoadedModule.Builder() {{
				name("xxx_module_xxx");
				files(files3.toArray(File[]::new));
			}}.build());
		}

		LoadedModule expected = new LoadedModule.Builder() {{
			name(root_name);
			files(files1.toArray(File[]::new));
			modules(modules.toArray(Module[]::new));
		}}.build();

		Assertions.assertEquals(expected, module);
	}


	@Test
	public void test_load_module_into() throws IOException {
		java.io.File simple_module = get_file("com/greenjon902/greenJam/core/packageLoader/simple_package");
		// Use that as it can pretend to be a module

		LoadedModule.Builder builder = new LoadedModule.Builder();
		Module module = PackageLoader.load_module_into(builder, new Toml(), simple_module, PackageLoader.default_config());
		Assertions.assertEquals("", module.name());  // Was never supplied

		// Next check method wants the name set
		module = PackageLoader.load_module_into(builder, new Toml(), simple_module, PackageLoader.default_config());
		check_simple_module_contents(module, false, "");
	}

	@Test
	public void test_simple_package() throws IOException {
		LoadedPackage p = (LoadedPackage) PackageLoader.load_single_package(get_file("com/greenjon902/greenJam/core/packageLoader/simple_package"));

		// Check some things here as not checked in check_simple_module_contents
		Assertions.assertEquals("Simple Package", p.display_name());
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertArrayEquals(new String[] {"GreenJon902"}, p.authors());

		p.compare_only_as_module = true; // As we just did package comparisons
		check_simple_module_contents(p, false, "simple_package");
	}

	@Test
	public void test_package_with_subfolder_files() throws IOException {
		LoadedPackage p = (LoadedPackage) PackageLoader.load_single_package(get_file("com/greenjon902/greenJam/core/packageLoader/package_with_subfolder_files"));
		Assertions.assertEquals("Simple Package", p.display_name());
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertArrayEquals(new String[] {"GreenJon902"}, p.authors());

		p.compare_only_as_module = true; // As we just did package comparisons
		check_simple_module_contents(p, true, "package_with_subfolder_files");
	}

	@Test
	public void test_package_with_changing_regex() throws IOException {
		Package p = PackageLoader.load_single_package(get_file("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex"));

		LoadedPackage expected = new LoadedPackage.Builder() {{
			name("package_with_changing_regex");
			display_name("package_with_changing_regex");
			files(new File[]{
					new LoadedFile.Builder() {{
						name("a.jam");
					}}.build(),
					new LoadedFile.Builder() {{
						name("b.jam");
					}}.build()
			});
			modules(new Module[]{
					new LoadedModule.Builder() {{
						name("mod");
						files(new File[]{
								new LoadedFile.Builder() {{
									name("c.jam");
								}}.build()
						});
					}}.build()
			});
		}}.build();

		Assertions.assertEquals(expected, p);
	}
}
