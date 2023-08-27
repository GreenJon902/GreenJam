package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Information that is useful to the {@link com.greenjon902.greenJam.api.core.packageLoader.PackageLoader}.
 */
public class LoaderRawConfig {
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

	public LoaderRawConfig(@NotNull String packageConfigPath, @NotNull String moduleConfigPath, @NotNull String fileRegex, @NotNull List<String> fileRegexs, @NotNull String moduleRegex, @NotNull List<String> moduleRegexs) {
		this.packageConfigPath = packageConfigPath;
		this.moduleConfigPath = moduleConfigPath;
		this.fileRegex = fileRegex;
		this.fileRegexs = fileRegexs;
		this.moduleRegex = moduleRegex;
		this.moduleRegexs = moduleRegexs;
	}

	public LoaderRawConfig() {// TODO: Load defaults from a file
		this("", "", "", Collections.emptyList(), "", Collections.emptyList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoaderRawConfig loader = (LoaderRawConfig) o;
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
