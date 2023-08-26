package com.greenjon902.greenJam.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class PackageList {
	private static PackageList INSTANCE;

	/**
	 * Gets the instance of {@link PackageList}, or thread-safely creates one if it doesn't exist.
	 * @return The instance
	 */
	public static PackageList getInstance() {
		// Double locking for speed as this will get called a lot by other threads
		if (INSTANCE == null) {
			synchronized (PackageList.class) {
				if (INSTANCE == null) {
					INSTANCE = new PackageList();
				}
			}
		}
		return INSTANCE;
	}

	private final Map<String, Map<String, Package>> packages = new ConcurrentHashMap<>();

	/**
	 * Returns true if the given reference refers to a know package that has been {@link #add(String, String, Package)
	 * added}.
	 * @param reference The reference to look for
	 * @return If the package has been added
	 */
	public boolean hasPackage(PackageReference reference) {
		if (!packages.containsKey(reference.realName())) return false;
		return packages.get(reference.realName()).containsKey(reference.version());
	}

	/**
	 * Adds a new package under the name and version if it doesn't already exist.
	 * @param name The name of the package
	 * @param version The version of the package
	 * @param package_ The package being added
	 * @throws IllegalStateException If a package with that name and version has already been added
	 */
	public void add(String name, String version, Package package_) throws IllegalStateException {
		// Check if exists
		if (!packages.containsKey(name)) packages.put(name, new ConcurrentHashMap<>());
		if (packages.get(name).containsKey(version)) throw new IllegalStateException("Package \"" +
				PackageReference.formatName(name, version) + "\" already exists");
		// Doesn't exist so we can add it
		packages.get(name).put(version, package_);
	}

	/**
	 * Prints the loaded packages to console.
	 */
	public String toString() {
		return "PackageList{" + packages + "}";
	}

	/**
	 * Clears all the loaded packages.
	 */
	public void clear() {
		packages.clear();
	}

	/**
	 * Runs the given {@link BiConsumer} with the first arguement being the given packages, and the second being the
	 * internal packages list.
	 * @param expected The expected packages
	 * @param assertionFunction The function to run
	 */
	public void assertEquals(Map<String, Map<String, Package>> expected,
									BiConsumer<Map<String, Map<String, Package>>,
											Map<String, Map<String, Package>>> assertionFunction) {
		assertionFunction.accept(expected, packages);
	}
}
