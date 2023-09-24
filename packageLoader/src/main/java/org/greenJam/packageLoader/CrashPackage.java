package org.greenJam.packageLoader;

import org.greenJam.api.File;
import org.greenJam.api.Module;
import org.greenJam.api.Package;
import org.greenJam.api.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A placeholder package that crashes if accessed.
 */
public class CrashPackage implements Package {
	@Override
	public @NotNull Set<Module> modules() {
		throw new RuntimeException();
	}

	@Override
	public @NotNull Set<File> files() {
		throw new RuntimeException();
	}

	@Override
	public @NotNull String name() {
		throw new RuntimeException();
	}

	@Override
	public @NotNull Set<String> authors() {
		throw new RuntimeException();
	}

	@Override
	public @NotNull String description() {
		throw new RuntimeException();
	}

	@Override
	public @NotNull Set<PackageReference> dependencies() {
		throw new RuntimeException();
	}
}
