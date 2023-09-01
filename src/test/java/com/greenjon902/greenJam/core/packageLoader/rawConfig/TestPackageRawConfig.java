package com.greenjon902.greenJam.core.packageLoader.rawConfig;

import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


// TODO: Find better way of making comparison data
public class TestPackageRawConfig {
	@Test
	public void testSimple() {
		PackageRawConfig expected = new PackageRawConfig();

		expected.name = "jon";
		expected.version = "1.2.3";

		Toml toml = new Toml().read(
				"""
						name = "jon"
						version = "1.2.3\""""
		);
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		Assertions.assertEquals(expected, rawConfig);
	}

	@Test
	public void testLoadRawConfigWithComplexDependencies() {
		PackageRawConfig expected = new PackageRawConfig();
		expected.dependencies = new HashMap<>() {{
			put("omega", new PackageRawConfig.DependencySet(new PackageLinkRawConfig("", "1.0.0")));
			put("aj", new PackageRawConfig.DependencySet(new PackageLinkRawConfig("", "1.0.0")));
			put("aj2", new PackageRawConfig.DependencySet(new PackageLinkRawConfig("aj3", "1.0.0")));
			put("cat", new PackageRawConfig.DependencySet(
				new PackageLinkRawConfig("", "1.0.0"),
				new PackageLinkRawConfig("sussy_cat", "1.0.0-sus")
			));
			put("dave", new PackageRawConfig.DependencySet(new PackageLinkRawConfig("", "")));
		}};

		Toml toml = new Toml().read(
				"""
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

	@Test
	public void testLoadRawConfigWithRegexType1() {
		PackageRawConfig expected = new PackageRawConfig();

		expected.loader.fileRegex = new LoaderRawConfig.RegexSet(new RegexRawConfig("test1"));

		Toml toml = new Toml().read(
				"""
						[Loader]
						file-regex = "test1"
						"""
		);
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		Assertions.assertEquals(expected, rawConfig);
	}

	@Test
	public void testLoadRawConfigWithRegexType2() {
		PackageRawConfig expected = new PackageRawConfig();

		expected.loader.fileRegex = new LoaderRawConfig.RegexSet(new RegexRawConfig("test1"), new RegexRawConfig("test2"));

		Toml toml = new Toml().read(
				"""
						[Loader]
						file-regex = ["test1", "test2"]
						"""
		);
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		Assertions.assertEquals(expected, rawConfig);
	}

	@Test
	public void testLoadRawConfigWithRegexType3() {
		PackageRawConfig expected = new PackageRawConfig();

		expected.loader.fileRegex = new LoaderRawConfig.RegexSet(new RegexRawConfig("test1", "test2"),
				new RegexRawConfig("test3", "test4"));

		Toml toml = new Toml().read(
				"""
						[Loader]
						file-regex = { "test1" = "test2", "test3" = "test4" }
						"""
		);
		PackageRawConfig rawConfig = toml.to(PackageRawConfig.class);

		Assertions.assertEquals(expected, rawConfig);
	}
}
