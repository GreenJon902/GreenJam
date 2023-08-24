package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.Nullable;

public class PackageList {
	/**
	 * Formats a name and version in the style that is used for installed dependency storage.
	 * @param name The name of the package
	 * @param version The version of this installation
	 * @return The formatted string
	 */
	public static String formatName(String name, @Nullable String version) {
		if (version != null && !version.isEmpty()) {
			name = name + "-" + version;
		}
		return name;
	}
}
