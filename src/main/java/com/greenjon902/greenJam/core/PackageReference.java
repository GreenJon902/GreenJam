package com.greenjon902.greenJam.core;

public interface PackageReference {

	/**
	 * Gets the actual name of the package, this is the one that would be put as the key in a dependency.
	 * @return The name
	 */
	String realName();

	/**
	 * Gets the version of the package.
	 * @return The version
	 */
	String version();

	/**
	 * Gets the name that the user would call this package. <br>
	 * E.g. package "a" uses package "b", but calls it "c".
	 * @return The name
	 */
	String referName();
}
