package org.greenJam.api;

import org.greenJam.testUtils.MapValueFile;
import org.greenJam.testUtils.MapValueModule;
import org.greenJam.utils.inputStream.NullInputStream;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.greenJam.testUtils.Null.NULL;

public class TestModule_Equals extends TestPackageItem_Equals {
	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = super.getArgVariations();
		map.put("files", new Set[] {
				Collections.emptySet(),
				Set.of(new MapValueFile(Map.of("name", "a", "super_", NULL, "stream", new NullInputStream()))),
				Set.of(new MapValueFile(Map.of("name", "a", "super_", NULL, "stream", new NullInputStream())),
						new MapValueFile(Map.of("name", "b", "super_", NULL, "stream", new NullInputStream()))),
				Set.of(new MapValueFile(Map.of(
						"name", "a",
						"super_", new MapValueFile(Map.of(
								"name", "a", "super_", NULL, "stream", new NullInputStream()
						)),
						"stream", new NullInputStream())))
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
								"files", Set.of(new MapValueFile(Map.of(
										"name", "a",
										"stream", new NullInputStream()))),
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
