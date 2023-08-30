package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.PackageItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A quick set of tests to ensure that the utils are working as expected.
 */
public class TestTestUtils {
	@Test
	public void DummyPackage__Should_Equal_WhenSameInstance() {
		DummyPackage a = new DummyPackage();
		Assertions.assertEquals(a, a);
	}

	@Test
	public void DummyPackage__Should_NotEqual_WhenDifferentInstances() {
		DummyPackage a = new DummyPackage();
		DummyPackage b = new DummyPackage();
		Assertions.assertNotEquals(a, b);
	}

	@Test
	public void RecordPackageItem__Should_ClassEqual_WhenBothSameClass() {
		PackageItem a = new RecordPackageItem("test");
		PackageItem b = new RecordPackageItem("test");
		Assertions.assertEquals(a.getClass(), b.getClass());
	}

	@Test
	public void RecordPackageItem__Should_ClassNotEqual_WhenOneIsAnonymousClass() {
		PackageItem a = new RecordPackageItem("test");
		PackageItem b = new RecordPackageItem("test") {};
		Assertions.assertNotEquals(a.getClass(), b.getClass());
	}
}
