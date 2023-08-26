package com.greenjon902.greenJam.core.packageLoader;

import com.greenjon902.greenJam.core.packageLoader.LoadedPackage.RawConfig.Dependency;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TestLoadedPackage {
	@Test
	public void testLoadRawConfigWithComplexDependencies() {
		LoadedPackage.RawConfig expected = new LoadedPackage.RawConfig();

		expected.name = "jon";
		expected.version = "1.2.3";
		expected.dependencies = new HashMap<>() {{
			put("omega", new Dependency("", "1.0.0"));
			put("aj", new Dependency("", "1.0.0"));
			put("aj2", new Dependency("aj3", "1.0.0"));
			put("cat", new LoadedPackage.RawConfig.DependencyList() {{
				add(new Dependency("", "1.0.0"));
				add(new Dependency("sussy_cat", "1.0.0-sus"));
			}});
			put("dave", new Dependency("", ""));
		}};

		Toml toml = new Toml().read(
				"name = \"jon\"\n" +
						"version = \"1.2.3\"\n" +
						"\n" +
						"[Dependencies]\n" +
						"aj = { version = \"1.0.0\" }\n" +
						"aj2 = { version = \"1.0.0\", name = \"aj3\" }\n" +
						"omega = \"1.0.0\"\n" +
						"\n" +
						"[Dependencies.dave]\n" +
						"\n" +
						"[[Dependencies.cat]]\n" +
						"version = \"1.0.0\"\n" +
						"\n" +
						"[[Dependencies.cat]]\n" +
						"name = \"sussy_cat\"\n" +
						"version = \"1.0.0-sus\""
		);
		LoadedPackage.RawConfig rawConfig = toml.to(LoadedPackage.RawConfig.class);

		Assertions.assertEquals(expected, rawConfig);
	}
}
