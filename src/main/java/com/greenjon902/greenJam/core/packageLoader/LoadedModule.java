package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.api.core.Module;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.ModuleRawConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedModule extends LoadedPackageItem implements Module {
	private final Set<Module> modules;
	private final Set<File> files;
	private final ModuleRawConfig rawConfig;

	protected LoadedModule(Builder builder) {
		super(builder);
		this.modules = Collections.unmodifiableSet(builder.modules);
		this.files = Collections.unmodifiableSet(builder.files);
		this.rawConfig = builder.rawConfig;
	}

	@Override
	public @NotNull Set<Module> modules() {
		return modules;
	}

	@Override
	public @NotNull Set<File> files() {
		return files;
	}

	/**
	 * Returns the toml object that was at the root of this module.
	 * @return The toml
	 */
	public @NotNull ModuleRawConfig rawConfig() {
		return rawConfig;
	}

	public static class Builder extends LoadedPackageItem.Builder {
		private Set<Module> modules = Collections.emptySet();
		private Set<File> files = Collections.emptySet();
		private ModuleRawConfig rawConfig = new ModuleRawConfig();

		public void modules(Set<Module> modules) {this.modules = modules;}
		public void files(Set<File> files) {this.files = files;}
		public void rawConfig(ModuleRawConfig rawConfig) {this.rawConfig = rawConfig;}

		@Override
		public LoadedModule build() {
			return new LoadedModule(this);
		}
	}

	@Override
	public boolean equals(Object o) {  // TODO: Fix these comparisons
		if (this == o) return true;
		if (o instanceof LoadedPackage that) return that.equals(this);
		return Module.super.equals_(o);
	}
}
