package com.greenjon902.greenJam.core;

import com.greenjon902.greenJam.api.core.Package;
import com.greenjon902.greenJam.api.core.PackageList;
import com.greenjon902.greenJam.api.core.packageLoader.PackageReference;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageListImpl implements PackageList {

	private static volatile PackageList INSTANCE;
	public static PackageList getInstance() {
		// Double locking for speed as this will get called a lot by other threads
		if (PackageListImpl.INSTANCE == null) {
			synchronized (PackageList.class) {
				if (PackageListImpl.INSTANCE == null) {
					PackageListImpl.INSTANCE = new PackageListImpl();
				}
			}
		}
		return PackageListImpl.INSTANCE;
	}

	private final Map<String, Map<String, Package>> packages = new ConcurrentHashMap<>();

	@Override
	public boolean hasPackage(PackageReference reference) {
		if (!packages.containsKey(reference.realName())) return false;
		return packages.get(reference.realName()).containsKey(reference.version());
	}

	@Override
	public void add(String name, String version, Package package_, boolean force) throws IllegalStateException {
		// Check if exists
		if (!packages.containsKey(name)) packages.put(name, new ConcurrentHashMap<>());
		if (!force && packages.get(name).containsKey(version)) throw new IllegalStateException("Package \"" +
				PackageReference.formatName(name, version) + "\" already exists");
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
	public void clear() {
		packages.clear();
	}

	@Override
	public Map<String, Map<String, Package>> getPackages() {
		return Collections.unmodifiableMap(packages);  // I don't think this protects much (cause 2d map) but it conveys
		                                               // the point.
	}

	@Override
	public Package get(String name, String version) {
		if (!packages.containsKey(name)) throw new IllegalArgumentException("No package loaded called " + name);
		Map<String, Package> versions = packages.get(name);
		if (!versions.containsKey(version)) throw new IllegalArgumentException("Package called " + name +
				" has not loaded version " + version);
		return versions.get(version);
	}
}
