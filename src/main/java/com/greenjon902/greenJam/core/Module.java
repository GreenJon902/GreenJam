package com.greenjon902.greenJam.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A module is a folder inside a package, it contains files and submodules. Some configs can change within a
 * submodule.
 */
public interface Module extends PackageItem {
	/**
	 * Gets the children modules.
	 *
	 * @implNote The set should be immutable
	 * @return A shallow clone of the modules list
	 */
	@NotNull Set<Module> modules();

	/**
	 * Gets the children files.
	 *
	 * @implNote The set should be immutable
	 * @return A shallow clone of the file list
	 */
	@NotNull Set<File> files();
}
