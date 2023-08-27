package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.packageLoader.rawConfig.DependencyList;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.DependencyRawConfig;
import com.greenjon902.greenJam.core.packageLoader.rawConfig.PackageRawConfig;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TestLoadedPackage {
	@Test
	public void testLoadRawConfigWithComplexDependencies() {
		PackageRawConfig expected = new PackageRawConfig();

		expected.name = "jon";
		expected.version = "1.2.3";
		expected.dependencies = new HashMap<>() {{
			put("omega", new DependencyList(new DependencyRawConfig("", "1.0.0")));
			put("aj", new DependencyList(new DependencyRawConfig("", "1.0.0")));
			put("aj2", new DependencyList(new DependencyRawConfig("aj3", "1.0.0")));
			put("cat", new DependencyList(
				new DependencyRawConfig("", "1.0.0"),
				new DependencyRawConfig("sussy_cat", "1.0.0-sus")
			));
			put("dave", new DependencyList(new DependencyRawConfig("", "")));
		}};

		Toml toml = new Toml().read(
				"""
						name = "jon"
						version = "1.2.3"

						[Dependencies]
						aj = { version = "1.0.0" }
						aj2 = { version = "1.0.0", name = "aj3" }
						omega = "1.0.0"

						[Dependencies.dave]

						[[Dependencies.cat]]
						version = "1.0.0"

						[[Dependencies.cat]]
						name = "sussy_cat"
						version = "1.0.0-sus\""""
		);
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		Assertions.assertEquals(expected, rawConfig);
	}
}
