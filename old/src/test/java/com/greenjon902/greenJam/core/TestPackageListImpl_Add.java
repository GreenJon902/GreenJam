package org.greenJam.core;

import org.greenJam.api.Package;
import org.greenJam.api.exceptions.PackageAlreadyAddedException;
import org.greenJam.testUtils.DummyPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPackageListImpl_Add {
	public static String NAME = "testName";
	public static String VERSION = "testVersion";
	public static Package[] PACKAGES = {new DummyPackage(), new DummyPackage()};  // Will not compare to each other

	PackageListImpl pl;

	@BeforeEach
	public void setup() {
		pl = new PackageListImpl();
	}

	public void verify(int i) {
		Package expected = PACKAGES[i];
		Package actual = pl.get(NAME, VERSION);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void Should_AddThePackage_When_ForceIsFalse_And_NoPackageLoaded() {
		pl.add(NAME, VERSION, PACKAGES[0], false);
		verify(0);
	}

	@Test
	public void Should_AddThePackage_When_ForceIsTrue_And_NoPackageLoaded() {
		pl.add(NAME, VERSION, PACKAGES[0], true);
		verify(0);
	}

	@Test
	public void Should_Crash_When_ForceIsFalse_And_PackageLoaded() {
		pl.add(NAME, VERSION, PACKAGES[0], false);
		PackageAlreadyAddedException e = Assertions.assertThrows(
				PackageAlreadyAddedException.class,
				() -> pl.add(NAME, VERSION, PACKAGES[1], false)
		);
		Assertions.assertEquals(NAME, e.name);
		Assertions.assertEquals(VERSION, e.version);
	}

	@Test
	public void Should_AddThePackage_When_ForceIsTrue_And_PackageLoaded() {
		pl.add(NAME, VERSION, PACKAGES[0], false);
		pl.add(NAME, VERSION, PACKAGES[1], true);
		verify(1);
	}
}