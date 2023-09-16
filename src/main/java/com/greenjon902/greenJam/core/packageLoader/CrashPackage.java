package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.File;
import com.greenjon902.greenJam.api.Module;
import com.greenjon902.greenJam.api.Package;
import com.greenjon902.greenJam.api.PackageReference;
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
