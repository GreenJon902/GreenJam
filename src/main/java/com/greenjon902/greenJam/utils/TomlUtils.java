package com.greenjon902.greenJam.utils;

import com.moandjiezana.toml.Toml;

import java.util.function.Consumer;
import java.util.function.Function;

public class TomlUtils {
	/**
	 * Runs the consumer with the value from the toml if it exists.
	 * @param key The name of the value
	 * @param setter The consumer to run
	 * @param toml The toml
	 * @return Whether it was set
	 */
	public static boolean set_if_not_null_string(String key, Consumer<String> setter, Toml toml) {
		return set_if_not_null(key, toml::getString, setter, toml);
	}

	/**
	 * Runs the consumer with the value from the getter if it exists in the toml.
	 * @param key The name of the value
	 * @param getter The function to get the value from. This is run with the key
	 * @param setter The consumer to run
	 * @param toml The toml
	 * @return Whether it was set
	 * @param <T> The type of the value that we are getting
	 */
	public static <T> boolean set_if_not_null(String key, Function<String, T> getter, Consumer<T> setter, Toml toml) {
		if (toml.contains(key)) {
			setter.accept(getter.apply(key));
			return true;
		}
		return false;
	}
}
