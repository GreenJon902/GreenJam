package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.PackageItem;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A {@link PackageItem} that acts like a record, but uses the correct equals and hashCode functions.
 */
public class MapValuePackageItem extends MapValueBase implements PackageItem {
	public MapValuePackageItem(Map<String, Object> values) {
		super(values);
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
		return (String) values.get("name");
	}

	/**
	 * A subclass that is technically a different class, but apart from that is identical.
	 */
	public static class MapValuePackageItem2 extends MapValuePackageItem {
		public MapValuePackageItem2(Map<String, Object>  values) {
			super(values);
		}
	}
}
