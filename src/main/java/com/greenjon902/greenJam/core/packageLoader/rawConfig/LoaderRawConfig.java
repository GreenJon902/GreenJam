package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Information that is useful to the {@link com.greenjon902.greenJam.api.core.packageLoader.PackageLoader}.
 */
public class LoaderRawConfig {
	@SerializedName("package-config-path")
	public @NotNull String packageConfigPath;
	@SerializedName("module-config-path")
	public @NotNull String moduleConfigPath;
	@SerializedName("file-regex")
	public @NotNull LoaderRawConfig.RegexSet fileRegex;
	@SerializedName("module-regex")
	public @NotNull LoaderRawConfig.RegexSet moduleRegex;

	public LoaderRawConfig(@NotNull String packageConfigPath, @NotNull String moduleConfigPath, @NotNull LoaderRawConfig.RegexSet fileRegex, @NotNull LoaderRawConfig.RegexSet moduleRegex) {
		this.packageConfigPath = packageConfigPath;
		this.moduleConfigPath = moduleConfigPath;
		this.fileRegex = fileRegex;
		this.moduleRegex = moduleRegex;
	}

	public LoaderRawConfig() {// TODO: Load defaults from a file
		this("", "", new RegexSet(), new RegexSet());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoaderRawConfig loader = (LoaderRawConfig) o;
		return Objects.equals(packageConfigPath, loader.packageConfigPath) && Objects.equals(moduleConfigPath, loader.moduleConfigPath) && Objects.equals(fileRegex, loader.fileRegex) && Objects.equals(moduleRegex, loader.moduleRegex);
	}

	@Override
	public int hashCode() {
		return Objects.hash(packageConfigPath, moduleConfigPath, fileRegex, moduleRegex);
	}

	@Override
	public String toString() {
		return "Loader{" +
				"packageConfigPath='" + packageConfigPath + '\'' +
				", moduleConfigPath='" + moduleConfigPath + '\'' +
				", fileRegex='" + fileRegex + '\'' +
				", moduleRegex='" + moduleRegex + '\'' +
				'}';
	}

	@JsonAdapter(RegexSet.RegexSetAdapter.class)
	public static class RegexSet extends AdaptableCollectionBase<RegexRawConfig, Set<RegexRawConfig>> {
		public RegexSet(RegexRawConfig... regexs) {
			super(Set.of(regexs));
		}

		public class RegexSetAdapter extends AdapterBase {
			/**
			 * Deserializes a {@link JsonReader}
			 * It can detect three types of regex declarations:
			 * <br><br>
			 * <pre>
			 * "&lt;field&gt;": "&lt;regex&gt;"
			 * "&lt;field&gt;": [ "&lt;regex&gt;" ]
			 * "&lt;field&gt;": { "&lt;regex&gt;": "&lt;substitution&gt;" }
			 * </pre>
			 */
			@Override
			public RegexSet read(JsonReader in) throws IOException {
				return switch (in.peek()) {
					case STRING ->
							new RegexSet(new RegexRawConfig(in.nextString()));
					case BEGIN_ARRAY ->
							new RegexSet(Arrays.stream(new Gson().getAdapter(String[].class).read(in))
							.map(RegexRawConfig::new)
							.toArray(RegexRawConfig[]::new));
					case BEGIN_OBJECT -> //noinspection unchecked
							new RegexSet(((Map<String, String>) new Gson().getAdapter(Map.class).read(in)).entrySet().stream()
							.map(entry -> {
								String key = entry.getKey();
								if (entry.getKey().startsWith("\"") && entry.getKey().endsWith("\"")) {
									key = key.substring(1, key.length() - 1);
								}
								return new RegexRawConfig(key, entry.getValue());
							})
							.toArray(RegexRawConfig[]::new));
					default -> throw new RuntimeException("Unexpected token " + in.peek());
				};
			}
		}
	}
}
