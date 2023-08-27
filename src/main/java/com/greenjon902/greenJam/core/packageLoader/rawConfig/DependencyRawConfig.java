package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;

import java.util.Objects;

public class DependencyRawConfig {
	/**
	 * The {@link PackageReference#referName()} name of this dependency.
	 */
	public String name;
	public String version;

	public DependencyRawConfig(String name, String version) {
		this.name = name;
		this.version = version;
	}

	@SuppressWarnings({"unused", "ConstantValue"})
	public DependencyRawConfig() {  // Puts in default values that were missing from toml
		this("", "");
	}

	@Override
	public String toString() {
		return "Dependency{" +
				"name='" + name + '\'' +
				", version='" + version + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DependencyRawConfig that = (DependencyRawConfig) o;
		return Objects.equals(name, that.name) && Objects.equals(version, that.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, version);
	}
}
