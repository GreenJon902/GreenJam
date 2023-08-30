package com.greenjon902.greenJam.testUtils;

import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.api.core.Module;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * See {@link ArrayValuePackageItem}.
 */
public class ArrayValueModule extends ArrayValuePackageItem implements Module {
	public ArrayValueModule(Object... values) {
		super(values);
	}

	@Override
	public @NotNull Set<Module> modules() {
		return (Set<Module>) values[1];
	}

	@Override
	public @NotNull Set<File> files() {
		return (Set<File>) values[2];
	}


	/**
	 * See {@link ArrayValuePackageItem2}
	 */
	public static class ArrayValueModule2 extends ArrayValueModule {
		public ArrayValueModule2(Object... values) {
			super(values);
		}
	}
}
