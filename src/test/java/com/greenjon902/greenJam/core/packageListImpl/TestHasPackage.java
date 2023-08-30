package com.greenjon902.greenJam.core.packageListImpl;

import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.core.PackageListImpl;
import com.greenjon902.greenJam.testUtils.DummyPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestHasPackage {
	public static String NAME = "testName";
	public static String VERSION = "testVersion";
	public static Package PACKAGE = new DummyPackage();

	PackageListImpl pl;

	@BeforeEach
	public void setup() {
		pl = new PackageListImpl();
	}

	public void add(String n, String v) {
		String name = NAME + n;
		String version = VERSION + v;
		pl.add(name, version, PACKAGE);
	}

	@Test
	public void Should_ReturnTrue_When_HasThePackage() {
		add("", "");
		Assertions.assertTrue(pl.hasPackage(NAME, VERSION));
	}

	@Test
	public void Should_ReturnFalse_When_HasNoPackages() {
		Assertions.assertFalse(pl.hasPackage(NAME, VERSION));
	}

	@Test
	public void Should_ReturnFalse_When_HasOtherPackageVersion() {
		add("", "1");
		Assertions.assertFalse(pl.hasPackage(NAME, VERSION));
	}

	@Test
	public void Should_ReturnFalse_When_HasOtherPackageName() {
		add("1", "");
		Assertions.assertFalse(pl.hasPackage(NAME, VERSION));
	}
}