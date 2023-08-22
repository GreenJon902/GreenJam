package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.NotNull;

/**
 * A module is a folder inside a package, it contains files and submodules. Some configs can change within a
 * submodule.
 */
public interface Module extends PackageItem {
	/**
	 * Gets the children modules.
	 * @return A shallow clone of the modules list
	 */
	@NotNull Module[] modules();

	/**
	 * Gets the children files.
	 * @return A shallow clone of the file list
	 */
	@NotNull File[] files();
}
