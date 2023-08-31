package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.MapValueModule;
import com.greenjon902.greenJam.testUtils.MapValuePackageReference;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TestPackage extends TestModule {
	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = super.getArgVariations();
		map.put("authors", new Set[] {Collections.emptySet(), Set.of("GreenJon902"), Set.of("GreenJon902", "other")});
		map.put("description", new String[] {"", "testing"});
		map.put("dependencies", new Set[] {
				Collections.emptySet(),
				Set.of(new MapValuePackageReference(Map.of(
						"realName", "a", "referName", "a", "version", "1"
				))),
				Set.of(new MapValuePackageReference(Map.of(
						"realName", "a", "referName", "a", "version", "1")),
						new MapValuePackageReference(Map.of(
						"realName", "b", "referName", "b", "version", "1"
				)))
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
