package com.greenjon902.greenJam.core;

import com.greenjon902.greenJam.api.Package;
import com.greenjon902.greenJam.api.PackageList;
import com.greenjon902.greenJam.api.exceptions.NoSuchPackageException;
import com.greenjon902.greenJam.api.exceptions.PackageAlreadyAddedException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageListImpl implements PackageList {
	private final Map<String, Map<String, Package>> packages = new ConcurrentHashMap<>();

	@Override
	public boolean hasPackage(String name, String version) {
		if (!packages.containsKey(name)) return false;
		return packages.get(name).containsKey(version);
	}

	@Override
	public void add(String name, String version, Package package_, boolean force) throws IllegalStateException {
		// Check if exists
		if (!packages.containsKey(name)) packages.put(name, new ConcurrentHashMap<>());
		if (!force && packages.get(name).containsKey(version)) throw new PackageAlreadyAddedException(this, name, version);
		// Doesn't exist so we can add it
		packages.get(name).put(version, package_);
	}

	/**
	 * Prints the loaded packages to console.
	 */
	public String toString() {
		return "PackageList{" + packages + "}";
	}

	@Override
	public Map<String, Map<String, Package>> getPackages() {
		return Collections.unmodifiableMap(packages);  // I don't think this protects much (cause 2d map) but it conveys
		                                               // the point.
	}

	@Override
	public Package get(String name, String version) {
		if (!packages.containsKey(name)) throw new NoSuchPackageException(this, name, version);
		Map<String, Package> versions = packages.get(name);
		if (!versions.containsKey(version)) throw new NoSuchPackageException(this, name, version);
		return versions.get(version);
	}
}
