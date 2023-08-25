package com.greenjon902.greenJam.utils;

import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.utils.TomlUtils.*;

public class TestTomlUtils {
	@Test
	public void testSetIfNotNull() {
		Toml toml = new Toml();
		TestFunc<String> testFunc = new TestFunc<>();

		setIfNotNull("a", toml::getString, testFunc::set, toml);
		Assertions.assertNull(testFunc.testing);

		toml = new Toml().read("a=\"I changed!\"");

		setIfNotNull("a", toml::getString, testFunc::set, toml);
		Assertions.assertEquals("I changed!", testFunc.testing);
	}

	@Test
	public void testSetIfNotNullString() {
		Toml toml = new Toml();
		TestFunc<String> testFunc = new TestFunc<>();

		setIfNotNullString("a", testFunc::set, toml);
		Assertions.assertNull(testFunc.testing);

		toml = new Toml().read("a=\"I changed!\"");
		
		setIfNotNullString("a", testFunc::set, toml);
		Assertions.assertEquals("I changed!", testFunc.testing);
	}

	@Test
	public void testSetIfNotNullArray() {
		Toml toml = new Toml();
		TestFunc<String[]> testFunc = new TestFunc<>();

		setIfNotNullArray("a", testFunc::set, String.class, toml);
		Assertions.assertNull(testFunc.testing);

		toml = new Toml().read("a=[\"I \", \"changed!\"]");

		setIfNotNullArray("a", testFunc::set, String.class, toml);
		Assertions.assertArrayEquals(new String[] {"I ", "changed!"}, testFunc.testing);
	}
}

class TestFunc<T> {
	public T testing = null;

	public void set(T s) {
		testing = s;
	}
}