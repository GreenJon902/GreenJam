package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.PackageItem;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link PackageItem} that acts like a record, but uses the correct equals and hashCode functions.
 */
public class ArrayValuePackageItem implements PackageItem {
	protected final Object[] values;

	public ArrayValuePackageItem(Object... values) {
		this.values = values;
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
		return (String) values[0];
	}

	/**
	 * A subclass that is technically a different class, but apart from that is identical.
	 */
	public static class ArrayValuePackageItem2 extends ArrayValuePackageItem {
		public ArrayValuePackageItem2(Object... values) {
			super(values);
		}
	}
}
