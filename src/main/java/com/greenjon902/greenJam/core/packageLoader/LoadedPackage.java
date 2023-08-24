package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.Package;
import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedPackage extends LoadedModule implements Package {
	private final String[] authors;
	private final String description;
	protected boolean compare_only_as_module = false;

	protected LoadedPackage(Builder builder) {
		super(builder);
		this.authors = builder.authors;
		this.description = builder.description;
	}

	@Override
	public @NotNull String[] authors() {
		return authors;
	}

	@Override
	public @NotNull String description() {
		return description;
	}



	public static class Builder extends LoadedModule.Builder {
		private String[] authors = new String[0];
		private String description = "";

		public Builder(Toml toml) {
			super(toml);
		}

		public Builder() {
			super();
		}

		public Builder authors(String[] authors) {this.authors = authors; return this;}
		public Builder description(String description) {this.description = description; return this;}

		@Override
		public LoadedPackage build() {
			return new LoadedPackage(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!super.equals(o)) return false;

		if (compare_only_as_module) return true;  // Module comparison done so must be correct

		if (getClass() != o.getClass()) return false;
		LoadedPackage that = (LoadedPackage) o;
		return Arrays.equals(authors, that.authors) && Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(super.hashCode(), description);
		result = 31 * result + Arrays.hashCode(authors);
		return result;
	}
}
