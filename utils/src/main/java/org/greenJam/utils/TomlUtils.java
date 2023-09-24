package org.greenJam.utils;

import com.moandjiezana.toml.Toml;

import java.io.File;

public class TomlUtils {
	public static Toml loadIfExists(File file) {
		Toml toml = new Toml();
		if (file.exists()) {
			toml = toml.read(file);
		}
		return toml;
	}
}
