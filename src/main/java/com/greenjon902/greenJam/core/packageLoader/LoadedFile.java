package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.api.core.InputStream;
import com.greenjon902.greenJam.utils.NullInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * See {@link LoadedPackageItem}
 */
public class LoadedFile extends LoadedPackageItem implements File {

	private final InputStream stream;

	protected LoadedFile(Builder builder) {
		super(builder);
		stream = builder.stream;
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

	@Override
	public @NotNull InputStream stream() {
		return this.stream;
	}

	public static class Builder extends LoadedPackageItem.Builder {
		private InputStream stream = new NullInputStream();

		public void stream(InputStream stream) {this.stream = stream;}
		// For setting of super, see BasedFile

		@Override
		public LoadedFile build() {
			return new LoadedFile(this);
		}
	}
}
