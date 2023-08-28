package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import com.greenjon902.greenJam.core.PackageListImpl;

import java.util.Map;

public interface PackageList {
	/**
	 * Gets the instance of {@link PackageList}, or thread-safely creates one if it doesn't exist.
	 *
	 * @implSpec This should be thread safe
	 * @return The instance
	 */
	static PackageList getInstance() {
		return PackageListImpl.getInstance();
	}

	/**
	 * Returns true if the given reference refers to a know package that has been {@link #add(String, String, Package)
	 * added}.
	 *
	 * @param reference The reference to look for
	 * @return If the package has been added
	 */
	boolean hasPackage(PackageReference reference);

	/**
	 * Adds a new package under the name and version if it doesn't already exist.
	 *
	 * @param name     The name of the package
	 * @param version  The version of the package
	 * @param package_ The package being added
	 * @throws IllegalStateException If a package with that name and version has already been added
	 */
	void add(String name, String version, Package package_) throws IllegalStateException;

	/**
	 * Clears all the loaded packages.
	 */
	void clear();

	/**
	 * Gets all the loaded packages and returns them in a map structure or name.version = package.
	 * @return The packages
	 */
	Map<String, Map<String, Package>> getPackages();

	/**
	 * Gets a package by the name and version.
	 * @param name The name of the package
	 * @param version The version of the package
	 * @return The package
	 */
	Package get(String name, String version);
}
