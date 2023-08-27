package com.greenjon902.greenJam.core.packageLoader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLoadedPackageItem {
	@Test
	public void testSuccessfulEquals() {
		LoadedPackageItem a = new LoadedPackageItem.Builder() {{
			name("test");
		}}.build();
		LoadedPackageItem b = new LoadedPackageItem.Builder() {{
			name("test");
		}}.build();
		Assertions.assertEquals(a, b);
	}

	@Test
	public void testUnsuccessfulEquals() {
		LoadedPackageItem a = new LoadedPackageItem.Builder() {{
			name("foo");
		}}.build();
		LoadedPackageItem b = new LoadedPackageItem.Builder() {{
			name("bar");
		}}.build();
		Assertions.assertNotEquals(a, b);
	}
}
