package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.packageLoader.File;
import com.greenjon902.greenJam.api.packageLoader.Module;
import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedModule extends LoadedPackageItem implements Module {
	private final Set<Module> modules;
	private final Set<File> files;
	private final Toml toml;

	protected LoadedModule(Builder builder) {
		super(builder);
		this.modules = Collections.unmodifiableSet(builder.modules);
		this.files = Collections.unmodifiableSet(builder.files);
		this.toml = builder.toml;
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
	public @NotNull Toml toml() {
		return toml;
	}

	public static class Builder extends LoadedPackageItem.Builder {
		private Set<Module> modules = Collections.emptySet();
		private Set<File> files = Collections.emptySet();
		private Toml toml = new Toml();

		public LoadedPackageItem.Builder modules(Set<Module> modules) {this.modules = modules; return this;}
		public LoadedPackageItem.Builder files(Set<File> files) {this.files = files; return this;}
		public LoadedPackageItem.Builder toml(Toml toml) {this.toml = toml; return this;}

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




}
