package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.CombinationCalculator;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem.MapValuePackageItem2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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
	public <T extends PackageItem> void check(String name, T a, T b, boolean shouldEqual, boolean checkClass) {
		Supplier<String> message = () ->
				"Equals failed, info:" + "\n\n" +
				"Comparing for " + name + "\n" +
				a + "\n" +
				b + "\n" +
				"shouldEqual=" + shouldEqual + "\n" +
				"checkClass=" + checkClass + "\n\n";  // Supplier so only make string when have to

		if (checkClass) { // Only use equals_ for when we have to, as with this one we get less information
			Assertions.assertEquals(shouldEqual, a.equals_(b, true), message);

		} else if (shouldEqual) {
			Assertions.assertEquals(a, b, message);
		} else {
			Assertions.assertNotEquals(a, b, message);
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
	@Test
	protected void testEquals() {
		Map<String, Object[]> argVariations = getArgVariations();
		HashMap<String, Object>[] argCombinations = CombinationCalculator.calculate(argVariations);

		// Make all argument combinations ---
		System.out.println("Making " + Math.pow(argCombinations.length, 2) * 4 + " combinations");
		int n = 0;
		for (int i=0; i<Math.pow(argCombinations.length, 2); i++) {
			int ia = i % argCombinations.length;
			int ib = i / argCombinations.length;

			assert argCombinations[ia].keySet().equals(argCombinations[ib].keySet());

			HashMap<String, Object> a = argCombinations[ia];
			HashMap<String, Object> b = argCombinations[ib];
			boolean shouldEqual = ia == ib;

			// There are 4 class checks
			for (int t=0; t<4; t++) {
				boolean checkSameClass = (t & 1) != 1;
				boolean actuallySameClass = ((t & 2)) != 2;
				boolean shouldEqualWithClass = shouldEqual && !(checkSameClass && !actuallySameClass);

				check("[" + n + "] " + formatBool(shouldEqual) + "(" + formatBool(checkSameClass) +
								formatBool(actuallySameClass) + "), " + ia + ", " + ib,
						createInstance(true, a),
						createInstance(actuallySameClass, b),
						shouldEqualWithClass,
						checkSameClass);
				n++;
			}
		}
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
