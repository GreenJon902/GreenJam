package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A package is the largest part of a program, it encompasses all modules and files.
 */
public interface Package extends Module {
	/**
	 * Gets the display name of this package. This is used for logging, and requirements.
	 * @return The name
	 */
	@NotNull String display_name();

	/**
	 * Gets the authors of this package.
	 * @return A shallow copy of the list of authors
	 */
	@NotNull String[] authors();

	/**
	 * Gets the description of this package.
	 * @return The description
	 */
	@NotNull String description();

	@Override
	@Nullable
	default Module parent() {  // Packages don't get parents
		return null;
	}

	@Override
	@Nullable
	default Package getPackage() {  // We have found the package
		return this;
	}
}
