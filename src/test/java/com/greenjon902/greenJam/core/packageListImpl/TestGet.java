package com.greenjon902.greenJam.core.packageListImpl;

import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.api.core.exceptions.NoSuchPackageException;
import com.greenjon902.greenJam.core.PackageListImpl;
import com.greenjon902.greenJam.testUtils.DummyPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGet {
	public static String NAME = "testName";
	public static String VERSION = "testVersion";
	public static Package[] PACKAGES = {new DummyPackage(), new DummyPackage()};

	PackageListImpl pl;

	@BeforeEach
	public void setup() {
		pl = new PackageListImpl();
	}

	public void add(int i, String n, String v) {
		String name = NAME + n;
		String version = VERSION + v;
		pl.add(name, version, PACKAGES[i]);
	}

	public void verify(int i, Package actual) {
		Package expected = PACKAGES[i];
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void Should_ReturnThePackage_When_ItExists() {
		add(0, "", "");

		Package p = pl.get(NAME, VERSION);
		verify(0, p);
	}

	@Test
	public void Should_ReturnThePackage_When_ItExists_And_OtherPackageWithDifferentVersionsExist() {
		add(0, "", "");
		add(1, "", "1");

		Package p = pl.get(NAME, VERSION);
		verify(0, p);
	}

	@Test
	public void Should_Crash_When_NoPackageExists() {
		NoSuchPackageException e = Assertions.assertThrows(
				NoSuchPackageException.class,
				() -> pl.get(NAME, VERSION)
		);
		Assertions.assertEquals(NAME, e.name);
		Assertions.assertEquals(VERSION, e.version);
	}

	@Test
	public void Should_Crash_When_PackageWithWrongVersion() {
		add(1, "", "1");
		NoSuchPackageException e = Assertions.assertThrows(
				NoSuchPackageException.class,
				() -> pl.get(NAME, VERSION)
		);
		Assertions.assertEquals(NAME, e.name);
		Assertions.assertEquals(VERSION, e.version);
	}

	@Test
	public void Should_Crash_When_PackageWithWrongName() {
		add(0, "1", "");

		NoSuchPackageException e = Assertions.assertThrows(
				NoSuchPackageException.class,
				() -> pl.get(NAME, VERSION)
		);
		Assertions.assertEquals(NAME, e.name);
		Assertions.assertEquals(VERSION, e.version);
	}
}