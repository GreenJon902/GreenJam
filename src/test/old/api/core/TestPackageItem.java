package com.greenjon902.greenJam.old.api.core;

import com.greenjon902.greenJam.api.core.PackageItem;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPackageItem {
	@Test
	public void testSuccessfulEquals() {
		PackageItem a = new TestItem("test");
		PackageItem b = new TestItem("test");
		Assertions.assertEquals(a, b);
	}

	@Test
	public void testUnsuccessfulEquals() {
		PackageItem a = new TestItem("foo");
		PackageItem b = new TestItem("bar");
		Assertions.assertNotEquals(a, b);
	}

	@Test
	public void testSuccessfulEqualsDiffTypes() {
		PackageItem a = new TestItem("test");
		PackageItem b = new TestItem2("test");
		Assertions.assertEquals(a, b);
	}

	@Test
	public void testUnsuccessfulEqualsDiffTypes() {
		PackageItem a = new TestItem("foo");
		PackageItem b = new TestItem2("bar");
		Assertions.assertNotEquals(a, b);
	}

	@Test
	public void testUnsuccessfulEqualsAsDiffTypes() {
		PackageItem a = new TestItem("test");
		PackageItem b = new TestItem2("test");
		Assertions.assertFalse(a.equals_(b, true));
	}
}

record TestItem(String name) implements PackageItem {

	@Override
	public @NotNull String name() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		return equals_(obj);
	}
}

record TestItem2(String name2) implements PackageItem {

	@Override
	public @NotNull String name() {
		return name2;
	}

	@Override
	public boolean equals(Object obj) {
		return equals_(obj);
	}
}