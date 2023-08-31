package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.PackageItem;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem.MapValuePackageItem2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
		PackageItem a = new MapValuePackageItem(Map.of("name", "test"));
		PackageItem b = new MapValuePackageItem(Map.of("name", "test"));
		Assertions.assertEquals(a.getClass(), b.getClass());
	}

	@Test
	public void RecordPackageItem__Should_ClassNotEqual_WhenIsOtherClass() {
		PackageItem a = new MapValuePackageItem(Map.of("name", "test"));
		PackageItem b = new MapValuePackageItem2(Map.of("name", "test"));
		Assertions.assertNotEquals(a.getClass(), b.getClass());
	}
}
