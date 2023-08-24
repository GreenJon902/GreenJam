package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.NotNull;

/**
 * A package is the largest part of a program, it encompasses all modules and files.
 */
public interface Package extends Module {
	/**
	 * This name is not used for anything, apart from logging. It should be this format "name-version"
	 */
	@Override
	@NotNull String name();

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
}
