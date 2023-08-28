package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class PackageRawConfig extends ModuleRawConfig {
	public String description;
	public String version;
	public Set<String> authors;
	/**
	 * Maps from the {@link PackageReference#realName()} to the {@link DependencySet}.
	 */
	@SerializedName("Dependencies")
	public Map<String, DependencySet> dependencies;
	public List<PackageLinkRawConfig> bases;  // This is ordered so list
	@SerializedName("Override")
	public @Nullable PackageLinkRawConfig override;

	public PackageRawConfig(String name, LoaderRawConfig loader, String description, String version, Set<String> authors,
							Map<String, DependencySet> dependencies, List<PackageLinkRawConfig> bases,
							@Nullable PackageLinkRawConfig override) {
		super(name, loader);
		this.description = description;
		this.version = version;
		this.authors = authors;
		this.dependencies = dependencies;
		this.bases = bases;
		this.override = override;
	}

	@SuppressWarnings({"unused"})
	public PackageRawConfig() {  // Puts in default values that were missing from toml
		this("", new LoaderRawConfig(), "", "", Collections.emptySet(), Collections.emptyMap(), Collections.emptyList(),
				null);
	}

	@Override
	public void writeFields(StringBuilder sb) {
		sb.append("description='").append(description).append('\'');
		sb.append(", version='").append(version).append('\'');
		sb.append(", authors=").append(authors);
		sb.append(", dependencies=").append(dependencies);
		sb.append(", bases=").append(bases);
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
		return Objects.equals(description, rawConfig.description) && Objects.equals(version, rawConfig.version) &&
				Objects.equals(authors, rawConfig.authors) && Objects.equals(dependencies, rawConfig.dependencies) &&
				 Objects.equals(bases, rawConfig.bases);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), description, version, authors, dependencies, bases);
	}

	@JsonAdapter(DependencySet.DependencySetAdapter.class)
	public static class DependencySet extends AdaptableCollectionBase<PackageLinkRawConfig, Set<PackageLinkRawConfig>> {
		public DependencySet(PackageLinkRawConfig... dependencies) {
			super(Set.of(dependencies));
		}

		public class DependencySetAdapter extends AdapterBase {
			/**
			 * Deserializes a {@link JsonReader}
			 * It can detect three types of dependency declarations:
			 * <br><br>
			 * <pre>
			 * "&lt;package&gt;": "&lt;version&gt;"
			 * "&lt;package&gt;": { "version": "&lt;version&gt;", "name": "&lt;name&gt;" }
			 * "&lt;package&gt;": [{ "version": "&lt;version&gt;", "name": "&lt;name&gt;" }]
			 * </pre>
			 */
			@Override
			public DependencySet read(JsonReader in) throws IOException {
				return switch (in.peek()) {
					case BEGIN_OBJECT -> new DependencySet(new Gson().getAdapter(PackageLinkRawConfig.class).read(in));
					case BEGIN_ARRAY -> new DependencySet(new Gson().getAdapter(PackageLinkRawConfig[].class).read(in));
					case STRING -> new DependencySet(new PackageLinkRawConfig("", in.nextString()));
					default -> throw new RuntimeException("Unexpected token " + in.peek());
				};
			}
		}
	}
}
