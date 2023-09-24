package org.greenJam.api;

import org.greenJam.testUtils.EqualsTesterBase;
import org.greenJam.testUtils.MapValuePackageItem;
import org.greenJam.testUtils.MapValuePackageItem.MapValuePackageItem2;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPackageItem_Equals extends EqualsTesterBase {
	@Override
	public Map<String, Object[]> getArgVariations() {
		Map<String, Object[]> map = new HashMap<>();
		map.put("name", new String[] {"a", "b"});
		return map;
	}

	@Override
	protected PackageItem createInstance(boolean same, Map<String, Object> args) {
		if (same) {
			return new MapValuePackageItem(args);
		} else {
			return new MapValuePackageItem2(args);
		}
	}
}
