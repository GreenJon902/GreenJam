package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.PackageItem;
import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;

/**
 * The implementation of package item for the package loader, inheritors of these only store information that is entered
 * via the builders from the package loader which gets it from the disk.
 */
public class LoadedPackageItem extends FieldStringWriter.Abstract implements PackageItem, FieldStringWriter {
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

		public void name(String name) {this.name = name;}

		public LoadedPackageItem build() {
			return new LoadedPackageItem(this);
		}
	}

	@Override
	public boolean equals(Object obj) {
		return equals_(obj);
	}

	@Override
	public int hashCode() {
		return hashCode_();
	}
}
