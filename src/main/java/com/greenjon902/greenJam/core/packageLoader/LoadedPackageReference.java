package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.PackageReference;

public record LoadedPackageReference(String realName, String referName, String version) implements PackageReference {
	public LoadedPackageReference(String name, String version) {
		this(name, name, version);
	}
}
