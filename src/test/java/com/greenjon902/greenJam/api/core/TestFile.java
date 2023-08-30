package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.ArrayValueFile;

public class TestFile extends TestPackageItem {
	@Override
	public Object[][] getArgVariations() {
		return new Object[][]{
				new Object[]{"a", null},
				new Object[]{"b", new ArrayValueFile("a", null)},
				new Object[]{"c", new ArrayValueFile("b", new ArrayValueFile("a", null))}
		};
	}

	@Override
	protected File createInstance(boolean same, Object[] args) {
		assert args.length == 2;
		if (same) {
			return new ArrayValueFile(args);
		} else {
			return new ArrayValueFile.ArrayValueFile2(args);
		}
	}
}
