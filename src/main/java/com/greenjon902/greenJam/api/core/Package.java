package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

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
	 *
	 * @implSpec The set should be immutable
	 * @return The list of authors
	 */
	@NotNull Set<String> authors();

	/**
	 * Gets the description of this package.
	 * @return The description
	 */
	@NotNull String description();

	/**
	 * Gets the dependencies of this package.
	 *
	 * @implSpec The set should be immutable
	 * @return The dependencies as references (which are resolved using the package list)
	 */
	@NotNull Set<PackageReference> dependencies();
}
