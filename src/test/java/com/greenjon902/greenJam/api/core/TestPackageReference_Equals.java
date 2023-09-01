package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.testUtils.EqualsTesterBase;
import com.greenjon902.greenJam.testUtils.MapValuePackageReference;

import java.util.HashMap;
import java.util.Map;

public class TestPackageReference_Equals extends EqualsTesterBase {
	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = new HashMap<>();
		map.put("realName", new String[] {"a", "b"});
		map.put("referName", new String[] {"a", "b"});
		map.put("version", new String[] {"", "1.0.0"});
		return map;
	}

	@Override
	protected InterfaceComparable createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValuePackageReference(args);
		} else {
			return new MapValuePackageReference.MapValuePackageReference2(args);
		}
	}
}
