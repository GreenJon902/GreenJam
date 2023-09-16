package com.greenjon902.greenJam.api;

import com.greenjon902.greenJam.testUtils.EqualsTesterBase;
import com.greenjon902.greenJam.testUtils.MapValueInputStream;

import java.util.HashMap;
import java.util.Map;

public class TestInputStream_Equals extends EqualsTesterBase {
	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = new HashMap<>();
		map.put("name", new String[] {"a", "b"});
		map.put("string", new String[] {"", "testing123"});
		map.put("location", new Integer[] {0, 1, 100});
		return map;
	}

	@Override
	protected InputStream createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValueInputStream(args);
		} else {
			return new MapValueInputStream.MapValueInputStream2(args);
		}
	}
}
