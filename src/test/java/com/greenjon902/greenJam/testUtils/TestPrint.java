package com.greenjon902.greenJam.testUtils;

import java.util.Arrays;
import java.util.Objects;

public class TestPrint {
	public static void print(Object... objects) {
		if (System.getProperty("testDebug", "false").equals("true")) {
			System.out.println(String.join(" ", Arrays.stream(objects).map(Objects::toString).toList()));
		}
	}
}
