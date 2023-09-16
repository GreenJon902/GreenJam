package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.Module;
import com.greenjon902.greenJam.api.Package;
import com.greenjon902.greenJam.api.PackageReference;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.PackageRawConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedPackage extends LoadedModule implements Package {
	private final Set<String> authors;
	private final String description;
	private final Set<PackageReference> dependencies;

	protected boolean compareOnlyAsModule = false;  // For testing purposes only

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
	public @NotNull PackageRawConfig rawConfig() {
		return (PackageRawConfig) super.rawConfig();
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
		if (compareOnlyAsModule && o instanceof Module && !(o instanceof Package)) return o.equals(this);
		return Package.super.equals_(o);
	}

	@Override
	public void writeFields(StringBuilder sb) {
		if (!compareOnlyAsModule) {
			sb.append("authors=").append(Arrays.toString(authors().stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
			sb.append(", description='").append(description()).append('\'');
			sb.append(", dependencies=").append(Arrays.toString(dependencies().stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
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


}
