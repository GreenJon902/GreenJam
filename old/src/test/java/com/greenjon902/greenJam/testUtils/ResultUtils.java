package org.greenJam.testUtils;

import org.greenJam.utils.Result;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;

public class ResultUtils {
	public static <T> void checkOkAndCorrect(T expected, Result<T> result) {
		Assertions.assertTrue(result.isOk);
		Assertions.assertEquals(expected, result.unwrap());
	}

	public static <T> void checkOkAndListCorrect(List<T> expected, Result<List<T>> result) {
		Assertions.assertTrue(result.isOk);
		Assertions.assertArrayEquals(expected.toArray(), result.unwrap().toArray(),
				() -> "\nE: " + Arrays.toString(expected.toArray()) + "\nA: " + Arrays.toString(result.unwrap().toArray()) + "\n\n");
	}
}
