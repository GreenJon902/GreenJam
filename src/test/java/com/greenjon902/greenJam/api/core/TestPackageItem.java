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
	public Object[][] getArgVariations() {
		return new Object[][]{
				new Object[]{"a"},
				new Object[]{"b"}
		};
	}


	/**
	 * Data comes from {@link #testEquals()}
	 * @param name The name to call this test
	 * @param a The first object to compare
	 * @param b The second object to compare
	 * @param shouldEqual Should they be equal
	 * @param checkClass Should we check the class is the same
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource
	public <T extends PackageItem> void testEquals(String name, T a, T b, boolean shouldEqual, boolean checkClass) {
		System.out.println("Comparing for " + name);
		System.out.println(a);
		System.out.println(b);
		System.out.println("shouldEqual=" + shouldEqual);
		System.out.println("checkClass=" + checkClass);
		System.out.println();

		if (checkClass) { // Only use equals_ for when we have to, as with this one we get less information
			Assertions.assertEquals(shouldEqual, a.equals_(b, true));

		} else if (shouldEqual) {
			Assertions.assertEquals(a, b);
		} else {
			Assertions.assertNotEquals(a, b);
		}
	}

	/**
	 * Generates a stream of arguments, that will be able to check same and different classes, all the same or all
	 * different attributes, or if an individual attribute is different. It also does the reverse of each one too.
	 * This should work for subclasses too as it uses {@link #getArgVariations()} for data.
	 * @return The stream
	 */
	private Stream<Arguments> testEquals() {
		Object[][] argVariations = getArgVariations();

		// First make all argument combinations ---
		List<Arguments> combinations = new ArrayList<>();  // ArgsA, ArgsB, isEqual, argVariationsA, argVariationsB, diff

		for (int ia=0; ia<argVariations.length; ia++) { // Get which two list we are comparing
			for (int ib=0; ib<argVariations.length; ib++) {
				assert argVariations[ia].length == argVariations[ib].length;

				for (int idiff=-1; idiff<argVariations[ia].length + 1; idiff++) { // Get which item is changing
					// -1 is all same, length + 1 is all different, in between "idiff" is index of what's different

					Object[] a = argVariations[ia];

					Object[] b;
					String sidiff;
					if (idiff == -1) {
						sidiff = "All same";
						b = argVariations[ia];
					} else if (idiff < argVariations[ia].length) {
						sidiff = idiff + " different";
						b = argVariations[ia].clone();
						b[idiff] = argVariations[ib][idiff];
					} else {
						sidiff = "All different";
						b = argVariations[ib];
					}

					boolean shouldEqual = (idiff == -1) || (ia == ib);
					combinations.add(Arguments.of(a, b, shouldEqual, ia, ib, sidiff));
				}
			}
		}

		// Now add in class changes and convert to the correct format ---
		List<Arguments> arguments = new ArrayList<>();
		int i = 0;
		for (Arguments combination : combinations) {
			Object[] a = (Object[]) combination.get()[0];
			Object[] b = (Object[]) combination.get()[1];
			boolean originalShouldEqual = (boolean) combination.get()[2];
			int ia = (int) combination.get()[3];
			int ib = (int) combination.get()[4];
			String idiff = (String) combination.get()[5];

			// For class checks there are 4
			for (int n=0; n<4; n++) {
				boolean checkSameClass = (n & 1) != 1;
				boolean actuallySameClass = ((n & 2)) != 2;
				boolean shouldEqual = originalShouldEqual && !(checkSameClass && !actuallySameClass);

				arguments.add(Arguments.of(  // Finally make the arguments
						"[" + i + "] " + formatBool(originalShouldEqual) + "(" + formatBool(checkSameClass) +
								formatBool(actuallySameClass) + "), " + ia + ", " + ib + ", " + idiff,
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
