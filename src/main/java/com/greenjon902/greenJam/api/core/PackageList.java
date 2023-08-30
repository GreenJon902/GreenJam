package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;

import java.util.Map;

public interface PackageList {
	/**
	 * Returns true if the given reference refers to a know package that has been {@link #add(String, String, Package)
	 * added}.
	 *
	 * @param reference The reference to look for
	 * @return If the package has been added
	 */
	boolean hasPackage(PackageReference reference);

	/**
	 * See {@link #add(String, String, Package, boolean)}
	 */
	default void add(String name, String version, Package package_) throws IllegalStateException {
		add(name, version, package_, false);
	}

	/**
	 * Adds a new package under the name and version if it doesn't already exist.
	 *
	 * @param name     The name of the package
	 * @param version  The version of the package
	 * @param package_ The package being added
	 * @throws IllegalStateException If a package with that name and version has already been added
	 */
	void add(String name, String version, Package package_, boolean force) throws IllegalStateException;


	/**
	 * Gets all the loaded packages and returns them in a map structure or name.version = package.
	 * @return The packages
	 * @implSpec The returned maps should be unmodifiable
	 */
	Map<String, Map<String, Package>> getPackages();

	/**
	 * Gets a package by the name and version.
	 * @param name The name of the package
	 * @param version The version of the package
	 * @return The package
	 */
	Package get(String name, String version);

	/**
	 * Gets a package by the reference.
	 * @param reference The package reference to use
	 * @return The package
	 */
	default Package get(PackageReference reference) {
		return get(reference.realName(), reference.version());
	}
}
