package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.MapValueFile;
import com.greenjon902.greenJam.testUtils.MapValueModule;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.greenjon902.greenJam.testUtils.Null.NULL;

public class TestModule extends TestPackageItem {
	@Override
	public int defaultInterval() {
		return 201;  // Around 20000 full
	}

	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = super.getArgVariations();
		map.put("files", new Set[] {
				Collections.emptySet(),
				Set.of(new MapValueFile(Map.of("name", "a", "super_", NULL))),
				Set.of(new MapValueFile(Map.of("name", "a", "super_", NULL)),
						new MapValueFile(Map.of("name", "b", "super_", NULL))),
				Set.of(new MapValueFile(Map.of(
						"name", "a",
						"super_", new MapValueFile(Map.of(
								"name", "a", "super_", NULL
						)))))
		});
		map.put("modules", new Set[] {
				Collections.emptySet(),
				Set.of(new MapValueModule(Map.of(
						"name", "a", "files", Collections.emptySet(), "modules", Collections.emptySet()
				))),
				Set.of(new MapValueModule(Map.of("name", "b",
								"files", Collections.emptySet(),
								"modules", Collections.emptySet())),
						new MapValueModule(Map.of("name", "c",
								"files", Collections.emptySet(),
								"modules", Collections.emptySet()))),
				Set.of(new MapValueModule(Map.of("name", "b",
								"files", Set.of(new MapValueModule(Map.of(
										"name", "a",
										"files", Collections.emptySet(),
										"modules", Collections.emptySet()))),
								"modules", Collections.emptySet()))),
				Set.of(new MapValueModule(Map.of("name", "b",
						"files", Collections.emptySet(),
						"modules", Collections.emptySet())))
		});
		return map;
	}

	@Override
	protected Module createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValueModule(args);
		} else {
			return new MapValueModule.MapValueModule2(args);
		}
	}
}
