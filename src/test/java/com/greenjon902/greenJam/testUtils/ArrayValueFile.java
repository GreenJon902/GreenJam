package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.File;
import org.jetbrains.annotations.Nullable;

/**
 * See {@link ArrayValuePackageItem}.
 */
public class ArrayValueFile extends ArrayValuePackageItem implements File {
	public ArrayValueFile(Object... values) {
		super(values);
	}

	@Override
	public @Nullable File super_() {
		return (File) values[1];
	}

	/**
	 * See {@link com.greenjon902.greenJam.testUtils.ArrayValuePackageItem.ArrayValuePackageItem2}
	 */
	public static class ArrayValueFile2 extends ArrayValueFile {
		public ArrayValueFile2(Object... values) {
			super(values);
		}
	}
}
