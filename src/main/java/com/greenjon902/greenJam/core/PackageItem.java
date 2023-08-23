package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base class for any item that exists inside a package (including the package itself).
 */
public interface PackageItem {
	/**
	 * Gets the name of this item.
	 * @return The name
	 */
	@NotNull String name();

	/**
	 * Gets the parent module.
	 * @return The parent or null if it doesn't exist.
	 */
	@Nullable Module parent();

	/**
	 * Recursively calls getPackage on parents, the actual package interface overrides this and returns itself.
	 * @return The package or if no package was found.
	 */
	default @Nullable Package getPackage() {
		Module parent = parent();
		if (parent == null) return null;
		return parent.getPackage();
	}
}
