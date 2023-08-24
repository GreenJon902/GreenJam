package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;
import com.greenjon902.greenJam.core.Module;
import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedModule extends LoadedPackageItem implements Module {
	private final Module[] modules;
	private final File[] files;
	private final Toml toml;

	protected LoadedModule(Builder builder) {
		super(builder);
		this.modules = builder.modules;
		this.files = builder.files;
		this.toml = builder.toml;
	}

	@Override
	public @NotNull Module[] modules() {
		return modules.clone();
	}

	@Override
	public @NotNull File[] files() {
		return files.clone();
	}

	/**
	 * Gets the toml that this was loaded with. If there was none then this will be an empty toml object.
	 * @return The toml
	 */
	public @NotNull Toml toml() {
		return toml;
	}

	public static class Builder extends LoadedPackageItem.Builder {
		private Module[] modules = new Module[0];
		private File[] files = new File[0];
		private final Toml toml;

		/**
		 * Creates a new builder.
 		 * @param toml See {@link LoadedModule#toml()}
		 */
		public Builder(Toml toml) {
			this.toml = toml;
		}

		public Builder() {
			this(new Toml());
		}

		public LoadedPackageItem.Builder modules(Module[] modules) {this.modules = modules; return this;}
		public LoadedPackageItem.Builder files(File[] files) {this.files = files; return this;}

		@Override
		public LoadedModule build() {
			return new LoadedModule(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LoadedModule that)) return false;
		if (!super.equals(o)) return false;

		if (o instanceof LoadedPackage loadedPackage && loadedPackage.compare_only_as_module) return true;

		return Arrays.equals(modules, that.modules) && Arrays.equals(files, that.files) &&
				Objects.equals(toml.toMap(), that.toml.toMap());
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Arrays.hashCode(modules);
		result = 31 * result + Arrays.hashCode(files);
		result = 31 * result + toml.toMap().hashCode();
		return result;
	}
}
