package com.greenjon902.greenJam.api.packageLoader;

import com.greenjon902.greenJam.core.packageLoader.LoadedPackage;

import java.io.File;
import java.io.IOException;

public interface PackageLoader {
	/**
	 * Loads the package stored at root, and then recursively loads their requirements (until all packages are found) into
	 * the package list.
	 *
	 * @param root The root of the first package to load
	 * @return The package stored at root
	 */
	LoadedPackage loadPackagesFor(File root) throws IOException;
}
