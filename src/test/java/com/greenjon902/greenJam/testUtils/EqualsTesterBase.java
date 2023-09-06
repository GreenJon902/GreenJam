package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.InterfaceComparable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// TODO: Test more subclasses of each interface
public abstract class EqualsTesterBase {
	/**
	 * Creates the example data for each field that could be used. The key of the map is the field name (as used in
	 * {@link MapValuePackageItem}), and the value is an array of possible values.
	 * @return A mutable map
	 */
	public abstract Map<String, Object[]> getArgVariations();

	/**
	 * Creates an instance of the class. This requires support for two different objects.
	 * @param same Should it be object type one or object type 2
	 * @param args The arguments to supply the instance
	 * @return The instance
	 */
	protected abstract InterfaceComparable createInstance(boolean same, Map<String, Object> args);


	/**
	 * Data comes from {@link #testEquals()}
	 * @param name The name to call this test
	 * @param a The first object to compare
	 * @param b The second object to compare
	 * @param shouldEqual Should they be equal
	 * @param checkClass Should we check the class is the same
	 */
	public <T extends InterfaceComparable> void checkEquals(String name, T a, T b, boolean shouldEqual, boolean checkClass) {
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

				checkEquals("[" + n + "] " + formatBool(shouldEqual) + "(" + formatBool(checkSameClass) +
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
	 * Data comes from {@link #testEquals()}
	 * @param name The name to call this test
	 * @param a The first object to hash
	 * @param b The second object to hash
	 * @param shouldEqual Should their hashes be equal
	 */
	public <T extends InterfaceComparable> void checkHash(String name, T a, T b, boolean shouldEqual) {
		Supplier<String> message = () ->
				"Equals failed, info:" + "\n\n" +
						"Comparing for " + name + "\n" +
						a + "\n" +
						b + "\n" +
						"shouldEqual=" + shouldEqual + "\n";  // Supplier so only make string when have to

		int aHash = a.hashCode_();
		int bHash = b.hashCode_();

		if (shouldEqual) {
			Assertions.assertEquals(aHash, bHash, message);
		} else {
			Assertions.assertNotEquals(aHash, bHash, message);
		}
	}

	/**
	 * Generates a stream of arguments, that will be able to check same and different classes, all the same or all
	 * different attributes, or if an individual attribute is different. It also does the reverse of each one too.
	 * This should work for subclasses too as it uses {@link #getArgVariations()} for data.
	 */
	@Test
	protected void testHash() {
		Map<String, Object[]> argVariations = getArgVariations();
		HashMap<String, Object>[] argCombinations = CombinationCalculator.calculate(argVariations);

		// Make all argument combinations ---
		System.out.println("Making " + Math.pow(argCombinations.length, 2) * 2 + " combinations");
		int n = 0;
		for (int i=0; i<Math.pow(argCombinations.length, 2); i++) {
			int ia = i % argCombinations.length;
			int ib = i / argCombinations.length;

			assert argCombinations[ia].keySet().equals(argCombinations[ib].keySet());

			HashMap<String, Object> a = argCombinations[ia];
			HashMap<String, Object> b = argCombinations[ib];
			boolean shouldEqual = ia == ib;

			// The class changes so loop again
			for (int t=0; t<2; t++) {
				boolean otherClass = t == 1;

				checkHash("[" + n + "] " + formatBool(shouldEqual) + "(" +
								formatBool(otherClass) + "), " + ia + ", " + ib,
						createInstance(true, a),
						createInstance(otherClass, b),
						shouldEqual);
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
}
