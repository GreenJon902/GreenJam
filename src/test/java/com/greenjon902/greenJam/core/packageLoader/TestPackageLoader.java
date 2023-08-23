package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.Package;
import com.greenjon902.greenJam.core.PackageItem;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

// TODO: Test for changing glob strings
// TODO: Better checking system, maybe based on a tree?

/**
 * Resource information:
 * <br><br>
 * Simple_package just has files and submodules, and a package config file.
 * <br>
 * Package_with_subfolder_files is a duplicate of the simple package, but we add ext/*.jam, and add the appropriate
 * sections to the package config.
 * <br><br>
 * See the tests for the names of these items.
 */
public class TestPackageLoader {
	private static java.io.File get_file(String path) {
		return new java.io.File(TestPackageLoader.class.getClassLoader().getResource(path).getFile());
	}

	/**
	 * See {@link #check_simple_package_contents(Module, Package, int, int, int)}, sets default values for the simple package.
	 */
	public static void check_simple_package_contents(Module m, Package p) {
		check_simple_package_contents(m, p, 2, 1, 1);
	}

	/**
	 * Checks the files and submodules of the simple package.
	 * We take a module as we may be taking just a submodule, but take package to check
	 * {@link PackageItem#getPackage()}.
	 */
	public static void check_simple_package_contents(Module m, Package p, int files_amount, int modules_amount, int first_file_amount) {
		Assertions.assertEquals(p, m.getPackage());

		File[] files = m.files();
		Assertions.assertEquals(files_amount, files.length);
		Arrays.sort(files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("main.jam", files[0].name());
		Assertions.assertEquals("test.jam", files[1].name());
		Assertions.assertEquals(p, files[0].getPackage());
		Assertions.assertEquals(p, files[1].getPackage());

		Module[] modules = m.modules();
		Assertions.assertEquals(modules_amount, modules.length);
		Arrays.sort(modules, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("foo", modules[0].name());
		Assertions.assertEquals(p, modules[0].getPackage());

		File[] modules_files = modules[0].files();
		Assertions.assertEquals(first_file_amount, modules_files.length);
		Arrays.sort(modules_files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("bar.jam", modules_files[0].name());
		Assertions.assertEquals(p, modules_files[0].getPackage());
	}

	/**
	 * See {@link #check_simple_package_contents(Module, Package)}.
	 */
	public static void check_package_with_subfolder_files_contents(Module m, Package p) {
		check_package_with_subfolder_files_contents(m, p, 3, 2, 2, 1);
	}

	/**
	 * See {@link #check_simple_package_contents(Module, Package, int, int, int)}.
	 */
	public static void check_package_with_subfolder_files_contents(Module m, Package p, int files_amount,
																   int modules_amount, int first_file_amount,
																   int second_file_amount) {
		check_simple_package_contents(m, p, files_amount, modules_amount, first_file_amount);

		File[] files = m.files();
		Assertions.assertEquals(files_amount, files.length);
		Arrays.sort(files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("x_actualSkills.jam", files[2].name());
		Assertions.assertEquals(p, files[2].getPackage());

		Module[] modules = m.modules();
		Assertions.assertEquals(modules_amount, modules.length);
		Arrays.sort(modules, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("xxx_module_xxx", modules[1].name());
		Assertions.assertEquals(p, modules[1].getPackage());

		File[] modules_files = modules[1].files();
		Assertions.assertEquals(second_file_amount, modules_files.length);
		Arrays.sort(modules_files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("baz.jam", modules_files[0].name());
		Assertions.assertEquals(p, modules_files[0].getPackage());
	}

	@Test
	public void test_load_module_into() throws IOException {
		java.io.File simple_module = get_file("com/greenjon902/greenJam/core/packageLoader/simple_package");
		// Use that as it can pretend to be a module

		LoadedModule.Builder builder = new LoadedModule.Builder();
		Module module = PackageLoader.load_module_into(builder, new Toml(), simple_module, PackageLoader.default_config());
		Assertions.assertEquals("", module.name());  // Was never supplied

		check_simple_package_contents(module, null);
	}

	@Test
	public void test_simple_package() throws IOException {
		Package p = PackageLoader.load_package(get_file("com/greenjon902/greenJam/core/packageLoader/simple_package"));
		Assertions.assertEquals("Simple Package", p.display_name());
		Assertions.assertEquals("simple_package", p.name());
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertArrayEquals(new String[] {"GreenJon902"}, p.authors());

		check_simple_package_contents(p, p);
	}

	@Test
	public void test_package_with_subfolder_files() throws IOException {
		Package p = PackageLoader.load_package(get_file("com/greenjon902/greenJam/core/packageLoader/package_with_subfolder_files"));
		Assertions.assertEquals("Simple Package", p.display_name());
		Assertions.assertEquals("package_with_subfolder_files", p.name());
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertArrayEquals(new String[] {"GreenJon902"}, p.authors());

		check_package_with_subfolder_files_contents(p, p);
	}

	@Test
	public void test_package_with_changing_regex() throws IOException {
		Package p = PackageLoader.load_package(get_file("com/greenjon902/greenJam/core/packageLoader/package_with_changing_regex"));
		Assertions.assertEquals("package_with_changing_regex", p.display_name());
		Assertions.assertEquals("package_with_changing_regex`", p.name());
		Assertions.assertEquals("", p.description());
		Assertions.assertArrayEquals(new String[0], p.authors());

		File[] files = p.files();
		Assertions.assertEquals(2, files.length);
		Arrays.sort(files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("a.jam", files[0].name());
		Assertions.assertEquals("b.jam", files[1].name());
		Assertions.assertEquals(p, files[0].getPackage());
		Assertions.assertEquals(p, files[1].getPackage());

		Module[] modules = p.modules();
		Assertions.assertEquals(1, modules.length);
		Arrays.sort(modules, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("mod", modules[0].name());
		Assertions.assertEquals(p, modules[0].getPackage());

		File[] modules_files = modules[0].files();
		Assertions.assertEquals(1, modules_files.length);
		Arrays.sort(modules_files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("c.jam", modules_files[0].name());
		Assertions.assertEquals(p, modules_files[0].getPackage());
	}
}
