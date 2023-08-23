package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.File;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedFile extends LoadedPackageItem implements File {
	protected LoadedFile(Builder builder) {
		super(builder);
	}

	public static class Builder extends LoadedPackageItem.Builder {
		@Override
		public LoadedFile build() {
			return new LoadedFile(this);
		}
	}
}
