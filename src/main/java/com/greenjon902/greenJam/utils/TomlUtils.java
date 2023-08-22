package com.greenjon902.greenJam.utils;

import com.moandjiezana.toml.Toml;

import java.util.function.Consumer;
import java.util.function.Function;

public class TomlUtils {
	public static <T> boolean set_if_not_null_string(String key, Consumer<String> setter, Toml toml) {
		return set_if_not_null(key, toml::getString, setter, toml);
	}

	public static <T> boolean set_if_not_null(String key, Function<String, T> getter, Consumer<T> setter, Toml toml) {
		if (toml.contains(key)) {
			setter.accept(getter.apply(key));
			return true;
		}
		return false;
	}
}
