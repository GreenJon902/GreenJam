package com.greenjon902.greenJam.api;

import com.greenjon902.greenJam.testUtils.MapValueFile;
import com.greenjon902.greenJam.utils.inputStream.NullInputStream;
import com.greenjon902.greenJam.utils.inputStream.StringInputStream;

import java.util.Map;

import static com.greenjon902.greenJam.testUtils.Null.NULL;

public class TestFile_Equals extends TestPackageItem_Equals {
	@Override
	public Map<String, Object[]> getArgVariations()  {
		Map<String, Object[]> map = super.getArgVariations();
		map.put("super_", new MapValueFile[] {
				new MapValueFile(Map.of(
						"name", "a",
						"super_", NULL,
						"stream", new StringInputStream("a")
				)),
				new MapValueFile(Map.of(
						"name", "a",
						"super_", new MapValueFile(Map.of(
								"name", "a",
								"super_", NULL,
								"stream", new StringInputStream("b"
						))),
						"stream", new StringInputStream("c")
				))
		});
		map.put("stream", new InputStream[] {
				new NullInputStream(),
				new StringInputStream("testing"),
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
