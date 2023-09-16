package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.greenjon902.greenJam.api.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PackageLinkRawConfig {
	/**
	 * If used as a dependency, this is the {@link PackageReference#referName()} of this dependency.<br>
	 * If used as a base, this is the {@link PackageReference#realName()} of this dependency.
	 * If used as an override, this is the {@link PackageReference#realName()} of the package.
	 */
	public @NotNull String name;
	public @NotNull String version;

	public PackageLinkRawConfig(@NotNull String name, @NotNull String version) {
		this.name = name;
		this.version = version;
	}

	@SuppressWarnings({"unused"})
	public PackageLinkRawConfig() {  // Puts in default values that were missing from toml
		this("", "");
	}

	@Override
	public String toString() {
		return "PackageLinkRawConfig{" +
				"name='" + name + '\'' +
				", version='" + version + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PackageLinkRawConfig that = (PackageLinkRawConfig) o;
		return Objects.equals(name, that.name) && Objects.equals(version, that.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}
}
