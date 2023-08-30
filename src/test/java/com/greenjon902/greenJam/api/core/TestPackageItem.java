package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.RecordPackageItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPackageItem {
	public static final PackageItem a1 = new RecordPackageItem("a");
	public static final PackageItem a2 = new RecordPackageItem("a");
	public static final PackageItem a3 = new RecordPackageItem("a") {};  // Different class
	public static final PackageItem b1 = new RecordPackageItem("b");
	public static final PackageItem b3 = new RecordPackageItem("b") {};  // Different class


	@ParameterizedTest(name = "{0}")
	@MethodSource
	public void testEquals(String name, PackageItem expected, PackageItem actual, boolean shouldEqual, boolean checkTypes) {
		if (checkTypes) { // Only use equals_ for when we have to, as with this one we get less information
			Assertions.assertEquals(shouldEqual, expected.equals_(actual, true));

		} else if (shouldEqual) {
			Assertions.assertEquals(expected, actual);
		} else {
			Assertions.assertNotEquals(expected, actual);
		}
	}

	public Stream<Arguments> testEquals() {
		return Stream.of(
				Arguments.of("[0] a1 a1 ✓✖", a1, a1, true, false),
				Arguments.of("[1] a1 a2 ✓✖", a1, a2, true, false),
				Arguments.of("[2] a1 a3 ✓✖", a1, a3, true, false),
				Arguments.of("[3] a1 b1 ✖✖", a1, b1, false, false),
				Arguments.of("[4] a1 b3 ✖✖", a1, b3, false, false),

				Arguments.of("[5] a1 a1 ✓✓", a1, a1, true, true),
				Arguments.of("[6] a1 a2 ✓✓", a1, a2, true, true),
				Arguments.of("[7] a1 a3 ✖✓", a1, a3, false, true),
				Arguments.of("[8] a1 b1 ✖✓", a1, b1, false, true),
				Arguments.of("[9] a1 b3 ✖✓", a1, b3, false, true)
		);
	}
}
