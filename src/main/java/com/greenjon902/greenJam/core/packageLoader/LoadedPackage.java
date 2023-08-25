package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.Package;
import com.greenjon902.greenJam.core.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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


	public static class Builder extends LoadedModule.Builder {
		private Set<String> authors = Collections.emptySet();
		private String description = "";
		private Set<PackageReference> dependencies = Collections.emptySet();

		public Builder authors(Set<String> authors) {this.authors = authors; return this;}
		public Builder description(String description) {this.description = description; return this;}
		public Builder dependencies(Set<PackageReference> dependencies) {this.dependencies = dependencies; return this;}

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
}
