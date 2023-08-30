package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.PackageItem;
import org.jetbrains.annotations.NotNull;

public class RecordPackageItem implements PackageItem {
	private final String name;

	public RecordPackageItem(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		return equals_(o);
	}

	@Override
	public int hashCode() {
		return hashCode_();
	}

	@Override
	public @NotNull String name() {
		return name;
	}
}
