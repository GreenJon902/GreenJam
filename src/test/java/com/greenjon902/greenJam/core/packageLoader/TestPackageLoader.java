package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.Package;
import com.greenjon902.greenJam.core.PackageItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

// TODO: Test for changing glob strings

public class TestPackageLoader {
	private static java.io.File get_file(String path) {
		return new java.io.File(TestPackageLoader.class.getClassLoader().getResource(path).getFile());
	}

	@Test
	public void test_simple_package() throws IOException {
		Package p = PackageLoader.load(get_file("com/greenjon902/greenJam/core/packageLoader/simple_package"));
		Assertions.assertEquals("Simple Package", p.display_name());
		Assertions.assertEquals("A simple example of a package with files and modules", p.description());
		Assertions.assertArrayEquals(new String[] {"GreenJon902"}, p.authors());
		Assertions.assertEquals(p, p.getPackage());

		File[] files = p.files();
		Assertions.assertEquals(2, files.length);
		Arrays.sort(files, Comparator.comparing(PackageItem::name));  // So ordering is correct
		Assertions.assertEquals("main.jam", files[0].name());
		Assertions.assertEquals("test.jam", files[1].name());
		Assertions.assertEquals(p, files[0].getPackage());
		Assertions.assertEquals(p, files[1].getPackage());

		Module[] modules = p.modules();
		Assertions.assertEquals(1, modules.length);
		Assertions.assertEquals("foo", modules[0].name());
		Assertions.assertEquals(p, modules[0].getPackage());

		File[] modules_files = modules[0].files();
		Assertions.assertEquals(1, modules_files.length);
		Assertions.assertEquals("bar.jam", modules_files[0].name());
		Assertions.assertEquals(p, modules_files[0].getPackage());
	}
}
