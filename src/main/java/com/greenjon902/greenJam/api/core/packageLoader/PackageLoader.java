package com.greenjon902.greenJam.api.core.packageLoader;

import com.greenjon902.greenJam.api.core.Package;

import java.io.IOException;

public interface PackageLoader {
	/**
	 * Loads the package stored at root, and then recursively loads their requirements (until all packages are found) into
	 * the package list.
	 * <br><br>
	 * Note: This will not check if the root package is already loaded, however it does check when loading dependencies.
	 *
	 * @return The package stored at root
	 */
	Package loadAndDependants() throws IOException;
}
