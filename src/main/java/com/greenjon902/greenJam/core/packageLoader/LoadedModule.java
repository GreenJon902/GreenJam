package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.api.core.Module;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.ModuleRawConfig;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
		if (!(o instanceof LoadedModule that)) return false;
		if (!super.equals(o)) return false;

		if (!(this instanceof LoadedPackage)) {
			if (o instanceof LoadedPackage loadedPackage && !loadedPackage.compareOnlyAsModule) return false;
		}

		return Objects.equals(modules, that.modules) && Objects.equals(files, that.files);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), modules, files);
	}

	@Override
	public void writeFields(StringBuilder sb) {
		sb.append("modules=").append(Arrays.toString(modules.stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
		sb.append(", files=").append(Arrays.toString(files.stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
		sb.append(", ");
		super.writeFields(sb);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("LoadedModule{");
		writeFields(sb);
		sb.append('}');
		return sb.toString();
	}

}
