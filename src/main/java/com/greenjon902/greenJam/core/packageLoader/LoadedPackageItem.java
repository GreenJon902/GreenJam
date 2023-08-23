package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.Module;
import com.greenjon902.greenJam.core.PackageItem;
import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The implementation of package item for the package loader, inheritors of these only store information that is entered
 * via the builders from the package loader which gets it from the disk.
 */
public class LoadedPackageItem implements PackageItem {
	private final String name;
	private Module parent;


	protected LoadedPackageItem(Builder builder) {
		this.name = builder.name;
		this.parent = builder.parent;
	}

	@Override
	public @NotNull String name() {
		return name;
	}

	@Override
	public @Nullable Module parent() {
		return parent;
	}

	/**
	 * This should only be used during loading. See usage for why we need this
	 * {@link PackageLoader#load_module_into(LoadedModule.Builder, Toml, File, PackageLoader.LoadingConfig)}
	 * @param module The parent module
	 */
	protected void setParent(Module module) {
		parent = module;
	}

	public static class Builder {
		private String name = "";
		private Module parent = null;

		public Builder name(String name) {this.name = name; return this;}
		public Builder parent(Module parent) {this.parent = parent; return this;}

		public LoadedPackageItem build() {
			return new LoadedPackageItem(this);
		}
	}
}
