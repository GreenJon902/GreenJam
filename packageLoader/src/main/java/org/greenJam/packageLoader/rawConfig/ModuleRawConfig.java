package org.greenJam.packageLoader.rawConfig;

import com.google.gson.annotations.SerializedName;
import org.greenJam.api.FieldStringWriter;

import java.util.Objects;

public class ModuleRawConfig implements FieldStringWriter {  // Default values
	public String name;
	@SerializedName("Loader")
	public LoaderRawConfig loader;

	public ModuleRawConfig(String name, LoaderRawConfig loader) {
		this.name = name;
		this.loader = loader;

	}

	@SuppressWarnings({"unused"})
	public ModuleRawConfig() {  // Puts in default values that were missing from toml
		this("", new LoaderRawConfig());
	}

	@Override
	public void writeFields(StringBuilder sb) {
		sb.append("name='").append(name).append('\'');
		sb.append(", loader=").append(loader);
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
		ModuleRawConfig rawConfig = (ModuleRawConfig) o;
		return Objects.equals(name, rawConfig.name) && loader.equals(rawConfig.loader);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

}

