package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import org.jetbrains.annotations.NotNull;

/**
 * @param realName See {@link PackageReference#realName()}
 * @param referName See {@link PackageReference#referName()}
 * @param version See {@link PackageReference#version()}
 */
public record LoadedPackageReference(@NotNull String realName, @NotNull String referName, @NotNull String version)
		implements PackageReference {
	public LoadedPackageReference(String name, String version) {
		this(name, name, version);
	}
}
