package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MapValuePackageReference extends MapValueBase implements PackageReference {
	public MapValuePackageReference(Map<String, Object> values) {
		super(values);
	}

	@Override
	public @NotNull String realName() {
		return (String) values.get("realName");
	}

	@Override
	public @NotNull String version() {
		return (String) values.get("version");
	}

	@Override
	public @NotNull String referName() {
		return (String) values.get("referName");
	}

	@Override
	public boolean equals(Object o) {  // TODO: Get equals_ in its own interface (for mapvaluebase)
		return equals_(o);
	}

	@Override
	public int hashCode() {
		return hashCode_();
	}
}
