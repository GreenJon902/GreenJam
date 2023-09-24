package org.greenJam.utils;

import org.greenJam.api.exceptions.CannotPopStackedClassException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStackedClassBase {
	private static void test(Object[] array, StackedClassBase stackedClassBase) {
		Assertions.assertArrayEquals(array, stackedClassBase.get());
		for (int i=0; i<array.length; i++ ) {
			Assertions.assertEquals(array[i], stackedClassBase.get(i));
		}
	}

	@Test
	public void Should_ActAsExpected_When_UsedAsExpected() {
		StackedClassBase stackedClassBase = new StackedClassBase(4);
		Object[] array = new Object[4];

		test(array, stackedClassBase);

		// Test assignment and getting
		stackedClassBase.set(2, 5);
		array[2] = 5;
		test(array, stackedClassBase);

		stackedClassBase.set(2, 3);
		array[2] = 3;
		test(array, stackedClassBase);

		// Test pushing
		Object[] array2 = array.clone();
		stackedClassBase.push();
		test(array2, stackedClassBase);

		stackedClassBase.set(1, 1);
		array2[1] = 1;
		test(array2, stackedClassBase);

		// Test setting to null
		stackedClassBase.set(2, null);
		array2[2] = null;
		test(array2, stackedClassBase);

		// Test push after set to null
		Object[] array3 = array2.clone();
		stackedClassBase.push();
		test(array3, stackedClassBase);

		// Test popping
		stackedClassBase.pop();
		test(array2, stackedClassBase);

		stackedClassBase.pop();
		test(array, stackedClassBase);
	}

	@Test
	public void Should_Crash_When_PoppedToBelowDefaults() {
		StackedClassBase stackedClassBase = new StackedClassBase(4);
		Assertions.assertThrows(
				CannotPopStackedClassException.class,
				stackedClassBase::pop
		);

	}
}
