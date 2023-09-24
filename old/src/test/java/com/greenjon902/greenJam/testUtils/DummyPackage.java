package org.greenJam.testUtils;

import org.greenJam.api.File;
import org.greenJam.api.Module;
import org.greenJam.api.Package;
import org.greenJam.api.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * These should equal themselves, but fail against each other.<br>
 * <pre>
 *     this == this
 *     this != new DummyPackage()
 * </pre>
 */
public class DummyPackage implements Package {
	@Override
	public @NotNull Set<Module> modules() {
		return null;
	}

	@Override
	public @NotNull Set<File> files() {
		return null;
	}

	@Override
	public @NotNull String name() {
		return null;
	}

	@Override
	public @NotNull Set<String> authors() {
		return null;
	}

	@Override
	public @NotNull String description() {
		return null;
	}

	@Override
	public @NotNull Set<PackageReference> dependencies() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}
