package org.greenJam.testUtils;

import org.greenJam.api.File;
import org.greenJam.api.Module;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * See {@link MapValuePackageItem}.
 */
public class MapValueModule extends MapValuePackageItem implements Module {
	public MapValueModule(Map<String, Object> values) {
		super(values);
	}

	@Override
	public @NotNull Set<Module> modules() {
		return (Set<Module>) values.get("modules");
	}

	@Override
	public @NotNull Set<File> files() {
		return (Set<File>) values.get("files");
	}


	/**
	 * See {@link MapValuePackageItem2}
	 */
	public static class MapValueModule2 extends MapValueModule {
		public MapValueModule2(Map<String, Object>  values) {
			super(values);
		}
	}
}
