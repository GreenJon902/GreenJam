package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.NotNull;

/**
 * The base class for any item that exists inside a package (including the package itself).
 */
public interface PackageItem {
	/**
	 * Gets the name of this item.
	 * @return The name
	 */
	@NotNull String name();
}
