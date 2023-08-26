package com.greenjon902.greenJam.core.packageLoader;

import com.google.gson.annotations.SerializedName;
import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.api.core.Module;
import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedModule extends LoadedPackageItem implements Module {
	private final Set<Module> modules;
	private final Set<File> files;
	private final RawConfig rawConfig;

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
	public @NotNull RawConfig rawConfig() {
		return rawConfig;
	}

	public static class Builder extends LoadedPackageItem.Builder {
		private Set<Module> modules = Collections.emptySet();
		private Set<File> files = Collections.emptySet();
		private RawConfig rawConfig = new RawConfig();

		public void modules(Set<Module> modules) {this.modules = modules;}
		public void files(Set<File> files) {this.files = files;}
		public void rawConfig(RawConfig rawConfig) {this.rawConfig = rawConfig;}

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

		if (o instanceof LoadedPackage loadedPackage && loadedPackage.compareOnlyAsModule) return true;

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

	/**
	 * The toml file but converted into a class for easier accessing.
	 */
	public static class RawConfig implements FieldStringWriter {  // Default values
		// TODO: Move all these raw config things out to a seperate file, make it not horrible to deal with.
		public String name;
		@SerializedName("Loader")
		public Loader loader;

		public RawConfig(String name, Loader loader) {
			this.name = name;
			this.loader = loader;

		}

		@SuppressWarnings({"ConstantValue", "unused"})
		protected RawConfig() {  // Puts in default values that were missing from toml
			this("", new Loader());
		}

		@Override
		public void writeFields(StringBuilder sb) {
			sb.append("name='").append(name).append('\'');
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("RawConfig{");
			writeFields(sb);
			sb.append('}');
			return sb.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			RawConfig rawConfig = (RawConfig) o;
			return Objects.equals(name, rawConfig.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(name);
		}

		/**
		 * Information that is useful to the {@link com.greenjon902.greenJam.api.core.packageLoader.PackageLoader}.
		 */
		public static class Loader {
			@SerializedName("package-config-path")
			public @NotNull String packageConfigPath;
			@SerializedName("module-config-path")
			public @NotNull String moduleConfigPath;
			@SerializedName("file-regex")
			public @NotNull String fileRegex;
			@SerializedName("file-regexs")
			public @NotNull List<String> fileRegexs;
			@SerializedName("module-regex")
			public @NotNull String moduleRegex;
			@SerializedName("module-regexs")
			public @NotNull List<String> moduleRegexs;

			public Loader(@NotNull String packageConfigPath, @NotNull String moduleConfigPath, @NotNull String fileRegex, @NotNull List<String> fileRegexs, @NotNull String moduleRegex, @NotNull List<String> moduleRegexs) {
				this.packageConfigPath = packageConfigPath;
				this.moduleConfigPath = moduleConfigPath;
				this.fileRegex = fileRegex;
				this.fileRegexs = fileRegexs;
				this.moduleRegex = moduleRegex;
				this.moduleRegexs = moduleRegexs;
			}

			public Loader() {
				this("", "", "", Collections.emptyList(), "", Collections.emptyList());
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Loader loader = (Loader) o;
				return Objects.equals(packageConfigPath, loader.packageConfigPath) && Objects.equals(moduleConfigPath, loader.moduleConfigPath) && Objects.equals(fileRegex, loader.fileRegex) && Objects.equals(fileRegexs, loader.fileRegexs) && Objects.equals(moduleRegex, loader.moduleRegex) && Objects.equals(moduleRegexs, loader.moduleRegexs);
			}

			@Override
			public int hashCode() {
				return Objects.hash(packageConfigPath, moduleConfigPath, fileRegex, fileRegexs, moduleRegex, moduleRegexs);
			}

			@Override
			public String toString() {
				return "Loader{" +
						"packageConfigPath='" + packageConfigPath + '\'' +
						", moduleConfigPath='" + moduleConfigPath + '\'' +
						", fileRegex='" + fileRegex + '\'' +
						", fileRegexs=" + fileRegexs +
						", moduleRegex='" + moduleRegex + '\'' +
						", moduleRegexs=" + moduleRegexs +
						'}';
			}
		}
	}

}
