package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.google.gson.annotations.SerializedName;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PackageRawConfig extends ModuleRawConfig {
	public String description;
	public String version;
	public List<String> authors;
	/**
	 * Maps from the {@link PackageReference#realName()} to the {@link DependencyList}.
	 */
	@SerializedName("Dependencies")
	public Map<String, DependencyList> dependencies;

	public PackageRawConfig(String name, LoaderRawConfig loader, String description, String version, List<String> authors, Map<String, DependencyList> dependencies) {
		super(name, loader);
		this.description = description;
		this.version = version;
		this.authors = authors;
		this.dependencies = dependencies;
	}

	@SuppressWarnings({"unused"})
	public PackageRawConfig() {  // Puts in default values that were missing from toml
		this("", new LoaderRawConfig(), "", "", Collections.emptyList(), Collections.emptyMap());
	}

	@Override
	public void writeFields(StringBuilder sb) {
		sb.append("description='").append(description).append('\'');
		sb.append(", version='").append(version).append('\'');
		sb.append(", authors=").append(authors);
		sb.append(", dependencies=").append(dependencies);
		sb.append(", ");
		super.writeFields(sb);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("RawConfig{");
		writeFields(sb);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		PackageRawConfig rawConfig = (PackageRawConfig) o;
		return Objects.equals(description, rawConfig.description) && Objects.equals(version, rawConfig.version) && Objects.equals(authors, rawConfig.authors) && Objects.equals(dependencies, rawConfig.dependencies);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), description, version, authors, dependencies);
	}
}
