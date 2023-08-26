package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.api.core.File;

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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("LoadedFile{");
		writeFields(sb);
		sb.append('}');
		return sb.toString();
	}
}
