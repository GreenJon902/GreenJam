package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * See {@link MapValuePackageItem}.
 */
public class MapValuePackage extends MapValueModule implements Package {
	public MapValuePackage(Map<String, Object> values) {
		super(values);
	}

	@Override
	public @NotNull Set<String> authors() {
		return (Set<String>) values.get("authors");
	}

	@Override
	public @NotNull String description() {
		return (String) values.get("description");
	}

	@Override
	public @NotNull Set<PackageReference> dependencies() {
		return (Set<PackageReference>) values.get("dependencies");
	}


	/**
	 * See {@link MapValuePackageItem2}
	 */
	public static class MapValuePackage2 extends MapValuePackage {
		public MapValuePackage2(Map<String, Object>  values) {
			super(values);
		}
	}
}
