package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.ArrayValuePackageItem;
import com.greenjon902.greenJam.testUtils.ArrayValuePackageItem.ArrayValuePackageItem2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPackageItem {
	public final Object[] args = new Object[]{"a"};
	public final Object[] differentArgs = new Object[]{"b"};


	/**
	 * Data comes from {@link #testEquals()}
	 * @param name The name to call this test
	 * @param objectA The first object to compare
	 * @param objectB The second object to compare
	 * @param shouldEqual Should they be equal
	 * @param checkClass Should we check the class is the same
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource
	public <T extends PackageItem> void testEquals(String name, T objectA, T objectB, boolean shouldEqual, boolean checkClass) {

		if (checkClass) { // Only use equals_ for when we have to, as with this one we get less information
			Assertions.assertEquals(shouldEqual, objectA.equals_(objectB, true));

		} else if (shouldEqual) {
			Assertions.assertEquals(objectA, objectB);
		} else {
			Assertions.assertNotEquals(objectA, objectB);
		}
	}

	/**
	 * Generates a stream of arguments, that will be able to check same and different classes, all the same or all
	 * different attributes, or if an individual attribute is different.
	 * This should function for subclasses too.
	 * @return The stream
	 */
	private Stream<Arguments> testEquals() {
		assert args.length == differentArgs.length;  // Check same

		// First make all argument combinations ---
		List<Arguments> combinations = new ArrayList<>();

		combinations.add(Arguments.of(args, args, true));  // All same
		combinations.add(Arguments.of(args, differentArgs, false));  // All different

		for (int i=0; i<args.length; i++) {  // One different
			Object[] other = args.clone();
			other[i] = differentArgs[i];
			combinations.add(Arguments.of(args, other, false));
		}

		// Now add in class changes and convert to the correct format ---
		List<Arguments> arguments = new ArrayList<>();
		int i = 0;
		for (Arguments combination : combinations) {
			Object[] a = (Object[]) combination.get()[0];
			Object[] b = (Object[]) combination.get()[1];
			boolean originalShouldEqual = (boolean) combination.get()[2];

			// For class checks there are 4
			for (int n=0; n<4; n++) {
				boolean checkSameClass = (n & 1) != 1;
				boolean actuallySameClass = ((n & 2)) != 2;
				boolean shouldEqual = originalShouldEqual && !(checkSameClass && !actuallySameClass);

				arguments.add(Arguments.of(  // Finally make the arguments
						"[" + i + "] " + formatBool(originalShouldEqual) + "(" + formatBool(checkSameClass) +
								formatBool(actuallySameClass) + ")",
						createInstance(true, a),
						createInstance(actuallySameClass, b),
						shouldEqual,
						checkSameClass
				));
				i++;
			}
		}

		return arguments.stream();
	}

	/**
	 * Formats a bool object as a tick or a cross.
	 * @param bool The value
	 * @return The result
	 */
	private String formatBool(boolean bool) {
		return bool ? "✓" : "✖";
	}

	/**
	 * Creates an instance of the class. This requires support for two different objects.
	 * @param same Should it be object type one or object type 2
	 * @param args The arguments to supply the instance
	 * @return The instance
	 */
	protected PackageItem createInstance(boolean same, Object[] args) {
		assert args.length == 1;
		if (same) {
			return new ArrayValuePackageItem(args);
		} else {
			return new ArrayValuePackageItem2(args);
		}
	}
}
