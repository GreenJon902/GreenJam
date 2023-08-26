package com.greenjon902.greenJam.core.packageLoader;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedPackage extends LoadedModule implements Package {
	private final Set<String> authors;
	private final String description;
	private final Set<PackageReference> dependencies;

	protected boolean compareOnlyAsModule = false;

	protected LoadedPackage(Builder builder) {
		super(builder);
		this.authors = Collections.unmodifiableSet(builder.authors);
		this.description = builder.description;
		this.dependencies = Collections.unmodifiableSet(builder.dependencies);
	}

	@Override
	public @NotNull Set<String> authors() {
		return authors;
	}

	@Override
	public @NotNull String description() {
		return description;
	}

	@Override
	public @NotNull Set<PackageReference> dependencies() {
		return dependencies;
	}

	@Override
	public @NotNull RawConfig rawConfig() {
		return (RawConfig) super.rawConfig();
	}

	public static class Builder extends LoadedModule.Builder {
		private Set<String> authors = Collections.emptySet();
		private String description = "";
		private Set<PackageReference> dependencies = Collections.emptySet();

		public void authors(Set<String> authors) {this.authors = authors;}
		public void description(String description) {this.description = description;}
		public void dependencies(Set<PackageReference> dependencies) {this.dependencies = dependencies;}

		@Override
		public LoadedPackage build() {
			return new LoadedPackage(this);
		}
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!super.equals(o)) return false;

		if (compareOnlyAsModule) return true;  // Module comparison done so must be correct

		if (getClass() != o.getClass()) return false;

		LoadedPackage that = (LoadedPackage) o;
		return Objects.equals(authors, that.authors) && Objects.equals(description, that.description) && Objects.equals(dependencies, that.dependencies);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), authors, description, dependencies);
	}

	@Override
	public void writeFields(StringBuilder sb) {
		if (!compareOnlyAsModule) {
			sb.append("authors=").append(Arrays.toString(authors.stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
			sb.append(", description='").append(description).append('\'');
			sb.append(", dependencies=").append(Arrays.toString(dependencies.stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
			sb.append(", ");
		}
		super.writeFields(sb);
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("LoadedPackage");
		if (compareOnlyAsModule) {
			sb.append("(M)");
		}
		sb.append("{");
		writeFields(sb);
		sb.append('}');
		return sb.toString();
	}


	public static class RawConfig extends LoadedModule.RawConfig {
		public String description;
		public String version;
		public List<String> authors;
		/**
		 * Maps from the {@link PackageReference#realName()} to the {@link DependencyInfo}.
		 */
		@SerializedName("Dependencies")
		public Map<String, DependencyInfo> dependencies;

		public RawConfig(String name, Loader loader, String description, String version, List<String> authors, Map<String, DependencyInfo> dependencies) {
			super(name, loader);
			this.description = description;
			this.version = version;
			this.authors = authors;
			this.dependencies = dependencies;
		}

		@SuppressWarnings({"ConstantValue", "unused"})
		protected RawConfig() {  // Puts in default values that were missing from toml
			this("", new Loader(), "", "", Collections.emptyList(), Collections.emptyMap());
		}

		/**
		 * The base type of dependency information, this can be a {@link Dependency} (A single dependency), or a
		 * {@link DependencyList} (A list of dependencies). This is adapted by the {@link DependencyInfoAdapter}.
		 */
		@JsonAdapter(DependencyInfoAdapter.class)
		public interface DependencyInfo { }

		/**
		 * See {@link DependencyInfo}
		 */
		public static class Dependency implements DependencyInfo {
			/**
			 * The {@link PackageReference#referName()} name of this dependency.
			 */
			public String name;
			public String version;

			public Dependency(String name, String version) {
				this.name = name;
				this.version = version;
			}

			@SuppressWarnings({"unused", "ConstantValue"})
			protected Dependency() {  // Puts in default values that were missing from toml
				name = name == null ? "" : name;
				version = version == null ? "" : version;
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
				Dependency that = (Dependency) o;
				return Objects.equals(name, that.name) && Objects.equals(version, that.version);
			}

			@Override
			public int hashCode() {
				return Objects.hash(name, version);
			}
		}

		/**
		 * See {@link DependencyInfo}
		 */
		public static class DependencyList extends ArrayList<Dependency> implements DependencyInfo { }

		/**
		 * This class helps convert between a {@link Gson} object and a {@link DependencyInfo}.
		 * It can detect three types of dependency declarations:
		 * <br><br>
		 * <pre>
		 * "&lt;package&gt;": "&lt;version&gt;"
		 * "&lt;package&gt;": { "version": "&lt;version&gt;" }
		 * "&lt;package&gt;": [{ "version": "&lt;version&gt;" }]
		 * </pre>
		 */
		public static class DependencyInfoAdapter extends TypeAdapter<DependencyInfo> {
			@Override
			public void write(JsonWriter out, DependencyInfo value) {
				throw new RuntimeException("Write has not been implemented for DependencyItemAdapter");
			}

			@Override
			public DependencyInfo read(JsonReader in) throws IOException {
				DependencyInfo dependencyItem;
				dependencyItem = switch (in.peek()) {
					case BEGIN_OBJECT -> new Gson().getAdapter(Dependency.class).read(in);
					case BEGIN_ARRAY -> new Gson().getAdapter(DependencyList.class).read(in);
					case STRING -> new Dependency("", in.nextString());
					default -> throw new RuntimeException("Unexpected token " + in.peek());
				};
				return dependencyItem;
			}
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
			RawConfig rawConfig = (RawConfig) o;
			return Objects.equals(description, rawConfig.description) && Objects.equals(version, rawConfig.version) && Objects.equals(authors, rawConfig.authors) && Objects.equals(dependencies, rawConfig.dependencies);
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), description, version, authors, dependencies);
		}
	}
}
