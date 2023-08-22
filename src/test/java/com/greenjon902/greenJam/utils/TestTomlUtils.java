package com.greenjon902.greenJam.utils;

import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.utils.TomlUtils.set_if_not_null;
import static com.greenjon902.greenJam.utils.TomlUtils.set_if_not_null_string;

public class TestTomlUtils {
	@Test
	public void test_set_if_not_null() {
		Toml toml = new Toml();
		TestFunc testFunc = new TestFunc();

		set_if_not_null("a", toml::getString, testFunc::set, toml);
		Assertions.assertNull(testFunc.testing);

		toml = new Toml().read("a=\"I changed!\"");

		set_if_not_null("a", toml::getString, testFunc::set, toml);
		Assertions.assertEquals("I changed!", testFunc.testing);
	}

	@Test
	public void test_set_if_not_null_string() {
		Toml toml = new Toml();
		TestFunc testFunc = new TestFunc();

		set_if_not_null_string("a", testFunc::set, toml);
		Assertions.assertNull(testFunc.testing);

		toml = new Toml().read("a=\"I changed!\"");
		
		set_if_not_null_string("a", testFunc::set, toml);
		Assertions.assertEquals("I changed!", testFunc.testing);
	}
}

class TestFunc {
	public String testing = null;

	public void set(String s) {
		testing = s;
	}
}