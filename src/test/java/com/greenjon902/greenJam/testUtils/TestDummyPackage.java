package com.greenjon902.greenJam.testUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * A quick set of tests to ensure that the {@link DummyPackage} util is working as expected.
 */
public class TestDummyPackage {
	@Test
	public void Should_Equal_WhenSameInstance() {
		DummyPackage a = new DummyPackage();
		Assertions.assertEquals(a, a);
	}

	@Test
	public void Should_NotEqual_WhenDifferentInstances() {
		DummyPackage a = new DummyPackage();
		DummyPackage b = new DummyPackage();
		Assertions.assertNotEquals(a, b);
	}
}
