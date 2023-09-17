package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.utils.Result;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ResultUtils {
	public static <T> void checkOkAndCorrect(T expected, Result<T> result) {
		Assertions.assertTrue(result.isOk);
		Assertions.assertEquals(expected, result.unwrap());
	}

	public static <T> void checkOkAndListCorrect(List<T> expected, Result<List<T>> result) {
		Assertions.assertTrue(result.isOk);
		Assertions.assertArrayEquals(expected.toArray(), result.unwrap().toArray());
	}
}
