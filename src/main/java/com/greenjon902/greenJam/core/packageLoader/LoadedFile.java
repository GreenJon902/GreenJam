package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.File;
import org.jetbrains.annotations.Nullable;

/**
 * See {@link LoadedPackageItem}
 */
public class LoadedFile extends LoadedPackageItem implements File {

	protected LoadedFile(Builder builder) {
		super(builder);
	}

	/**
	 * See {@link File#super_()}.
	 * @return Null, see {@link com.greenjon902.greenJam.core.packageLoader.basedPackageHelpers.BasedFile} for actual
	 * super value
	 */
	@Override
	public @Nullable File super_() {
		return null;
	}

	public static class Builder extends LoadedPackageItem.Builder {
		// For setting of super, see BasedFile

		@Override
		public LoadedFile build() {
			return new LoadedFile(this);
		}
	}
}
