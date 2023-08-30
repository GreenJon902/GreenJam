package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.ArrayValueFile;
import com.greenjon902.greenJam.testUtils.ArrayValueModule;

import java.util.Collections;
import java.util.Set;

public class TestModule extends TestPackageItem {
	@Override
	public Object[][] getArgVariations() {
		return new Object[][]{
				new Object[]{"a", Collections.emptySet(), Collections.emptySet()},
				new Object[]{"b",
						Set.of(new ArrayValueModule("a", Collections.emptySet(), Collections.emptySet())),
						Set.of(new ArrayValueFile("a", null))},
				new Object[]{"c",
						Set.of(new ArrayValueModule("b", Collections.emptySet(), Collections.emptySet()),
								new ArrayValueModule("c", Collections.emptySet(), Collections.emptySet())),
						Set.of(new ArrayValueFile("a", null), new ArrayValueFile("b", null))},
				new Object[]{"d",
						Set.of(new ArrayValueModule("b", Set.of(new ArrayValueModule(
								"a", Collections.emptySet(), Collections.emptySet()
								)), Collections.emptySet()),
								new ArrayValueModule("b", Collections.emptySet(), Collections.emptySet())),
						Set.of(new ArrayValueFile("a", new ArrayValueFile("a", null)))},
		};
	}

	@Override
	protected Module createInstance(boolean same, Object[] args) {
		assert args.length == 3;
		if (same) {
			return new ArrayValueModule(args);
		} else {
			return new ArrayValueModule.ArrayValueModule2(args);
		}
	}
}
