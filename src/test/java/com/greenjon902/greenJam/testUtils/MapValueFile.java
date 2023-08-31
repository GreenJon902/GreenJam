package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.File;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.greenjon902.greenJam.testUtils.Null.NULL;

/**
 * See {@link MapValuePackageItem}.
 */
public class MapValueFile extends MapValuePackageItem implements File {
	public MapValueFile(Map<String, Object> values) {
		super(values);
	}

	@Override
	public @Nullable File super_() {
		Object super_ = values.get("super_");
		if (super_ == NULL) {
			return null;
		} else {
			return (File) super_;
		}
	}

	/**
	 * See {@link MapValuePackageItem2}
	 */
	public static class MapValueFile2 extends MapValueFile {
		public MapValueFile2(Map<String, Object>  values) {
			super(values);
		}
	}
}
