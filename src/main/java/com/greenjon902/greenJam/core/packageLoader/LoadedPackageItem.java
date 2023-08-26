package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.PackageItem;
import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The implementation of package item for the package loader, inheritors of these only store information that is entered
 * via the builders from the package loader which gets it from the disk.
 */
public class LoadedPackageItem implements PackageItem, FieldStringWriter {
	private final String name;


	protected LoadedPackageItem(Builder builder) {
		this.name = builder.name;
	}

	@Override
	public @NotNull String name() {
		return name;
	}


	public static class Builder {
		private String name = "";

		public Builder name(String name) {this.name = name; return this;}

		public LoadedPackageItem build() {
			return new LoadedPackageItem(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LoadedPackageItem that)) return false;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public void writeFields(StringBuilder sb) {
		sb.append("name='").append(name).append('\'');
	}

}
