package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.CombinationCalculator;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem;
import com.greenjon902.greenJam.testUtils.MapValuePackageItem.MapValuePackageItem2;
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
		Map<String, Object[]> argVariations = getArgVariations();
		HashMap<String, Object>[] argCombinations = CombinationCalculator.calculate(argVariations);

		// First make all argument combinations ---
		List<Arguments> combinations = new ArrayList<>();  // ArgsA, ArgsB, isEqual, argVariationsA, argVariationsB, diff

		for (int ia=0; ia<argCombinations.length; ia++) { // Get which two list we are comparing
			for (int ib=0; ib<argCombinations.length; ib++) {
				List<String> keys = argCombinations[ia].keySet().stream().toList();

				assert argCombinations[ia].keySet().equals(argCombinations[ib].keySet());

				for (int idiff=-1; idiff<keys.size() + 1; idiff++) { // Get which item is changing
					// -1 is all same, length + 1 is all different, in between "idiff" is index of what's different

					HashMap<String, Object> a = argCombinations[ia];

					HashMap<String, Object> b;
					String sidiff;
					boolean diffIsSame = false;  // Is the different actually the same object
					if (idiff == -1) {
						sidiff = "All same";
						b = argCombinations[ia];
						diffIsSame = true;  // No diff so just make same
					} else if (idiff < keys.size()) {
						if (a.get(keys.get(idiff)) == argCombinations[ib].get(keys.get(idiff))) {
							diffIsSame = true;
						}

						sidiff = idiff + " different";
						b = (HashMap<String, Object>) argCombinations[ia].clone();
						b.put(keys.get(idiff), argCombinations[ib].get(keys.get(idiff)));
					} else {
						sidiff = "All different";
						b = argCombinations[ib];
					}

					boolean shouldEqual = (idiff == -1) || (ia == ib) || diffIsSame;
					combinations.add(Arguments.of(a, b, shouldEqual, ia, ib, sidiff));
				}
			}
		}

		// Now add in class changes and convert to the correct format ---
		List<Arguments> arguments = new ArrayList<>();
		int i = 0;
		for (Arguments combination : combinations) {
			HashMap<String, Object> a = (HashMap<String, Object>) combination.get()[0];
			HashMap<String, Object> b = (HashMap<String, Object>) combination.get()[1];
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
	protected PackageItem createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValuePackageItem(args);
		} else {
			return new MapValuePackageItem2(args);
		}
	}
}
