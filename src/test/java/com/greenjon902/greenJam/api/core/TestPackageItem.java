package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.CombinationCalculator;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem.MapValuePackageItem2;
import com.greenjon902.greenJam.testUtils.TestPrint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TestPackageItem {
	/**
	 * Creates the example data for each field that could be used. The key of the map is the field name (as used in
	 * {@link MapValuePackageItem}), and the value is an array of possible values.
	 * @return A mutable map
	 */
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = new HashMap<>();
		map.put("name", new String[] {"a", "b"});
		return map;
	}

	/**
	 * Because we end up generating so many tests, by default we run one then skip the number of what returned by this
	 * function.
	 * @return Number to skip
	 */
	public int defaultInterval() {
		return 1;
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
		TestPrint.print("Comparing for " + name);
		TestPrint.print(a);
		TestPrint.print(b);
		TestPrint.print("shouldEqual=" + shouldEqual);
		TestPrint.print("checkClass=" + checkClass);
		TestPrint.print();

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
	 * For optimization reasons, we can set an interval, so it will only use every nth test. However, if the property
	 * {@systemProperty testFull} is true then everything is run.
	 * @return The stream
	 */
	protected Stream<Arguments> testEquals() {
		Map<String, Object[]> argVariations = getArgVariations();
		HashMap<String, Object>[] argCombinations = CombinationCalculator.calculate(argVariations);

		// First make all argument combinations ---
		List<Arguments> combinations = new ArrayList<>();  // ArgsA, ArgsB, isEqual, argVariationsA, argVariationsB

		for (int ia=0; ia<argCombinations.length; ia++) { // Get which two lists we are comparing
			for (int ib=0; ib<argCombinations.length; ib++) {

				assert argCombinations[ia].keySet().equals(argCombinations[ib].keySet());
				combinations.add(Arguments.of(
						argCombinations[ia],
						argCombinations[ib],
						false, ia, ib));
			}
		}

		// Now add in class changes, convert to the correct format, and only choose items in the interval ---
		List<Arguments> arguments = new ArrayList<>();

		int interval = defaultInterval();
		if (System.getProperty("testFull", "false").equals("true")) {
			interval = 1;
		}

		for (int i=0; i<combinations.size(); i+=interval) {
			Arguments combination = combinations.get(i);

			HashMap<String, Object> a = (HashMap<String, Object>) combination.get()[0];
			HashMap<String, Object> b = (HashMap<String, Object>) combination.get()[1];
			boolean originalShouldEqual = (boolean) combination.get()[2];
			int ia = (int) combination.get()[3];
			int ib = (int) combination.get()[4];

			// For class checks there are 4
			for (int n=0; n<4; n++) {
				boolean checkSameClass = (n & 1) != 1;
				boolean actuallySameClass = ((n & 2)) != 2;
				boolean shouldEqual = originalShouldEqual && !(checkSameClass && !actuallySameClass);

				arguments.add(Arguments.of(  // Finally make the arguments
						"[" + i + "] " + formatBool(originalShouldEqual) + "(" + formatBool(checkSameClass) +
								formatBool(actuallySameClass) + "), " + ia + ", " + ib,
						createInstance(true, a),
						createInstance(actuallySameClass, b),
						shouldEqual,
						checkSameClass
				));
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
	protected PackageItem createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValuePackageItem(args);
		} else {
			return new MapValuePackageItem2(args);
		}
	}
}
