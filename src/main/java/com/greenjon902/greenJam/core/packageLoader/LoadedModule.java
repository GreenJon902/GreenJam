package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import org.jetbrains.annotations.NotNull;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedModule extends LoadedPackageItem implements Module {
	private final Module[] modules;
	private final File[] files;

	protected LoadedModule(Builder builder) {
		super(builder);
		this.modules = builder.modules;
		this.files = builder.files;
	}

	@Override
	public @NotNull Module[] modules() {
		return modules.clone();
	}

	@Override
	public @NotNull File[] files() {
		return files.clone();
	}

	public static class Builder extends LoadedPackageItem.Builder {
		private Module[] modules = new Module[0];
		private File[] files = new File[0];

		public LoadedPackageItem.Builder modules(Module[] modules) {this.modules = modules; return this;}
		public LoadedPackageItem.Builder files(File[] files) {this.files = files; return this;}

		@Override
		public LoadedModule build() {
			return new LoadedModule(this);
		}
	}
}
