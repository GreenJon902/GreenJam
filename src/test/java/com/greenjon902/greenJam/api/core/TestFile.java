package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.MapValueFile;

import java.util.Map;

import static com.greenjon902.greenJam.testUtils.Null.NULL;

public class TestFile extends TestPackageItem {
	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = super.getArgVariations();
		map.put("super_", new MapValueFile[] {
				new MapValueFile(Map.of(
						"name", "a",
						"super_", NULL
				)),
				new MapValueFile(Map.of(
						"name", "a",
						"super_", new MapValueFile(Map.of(
								"name", "a",
								"super_", NULL
						))
				))
		});
		return map;
	}

	@Override
	protected File createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValueFile(args);
		} else {
			return new MapValueFile.MapValueFile2(args);
		}
	}
}
