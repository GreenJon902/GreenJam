package org.greenJam.packageLoader.basedPackageHelpers;

import org.greenJam.api.Package;
import org.greenJam.api.PackageReference;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

/**
 * A package that has bases, this class is used to create store a combined version of the given packages.
 * It attempts to bind packages in the most efficient way possible by reusing anything that can be reused.
 */
public class BasedPackage extends BasedModule implements Package {
	private final Set<String> authors;
	private final String description;
	private final Set<PackageReference> dependencies;

	/**
	 * See {@link BasedPackage(Package, Package...)}
	 * @param dummy Do differentiate between this and @link BasedPackage(Package, Package...)}
	 */
	public BasedPackage(boolean dummy, Package package1, Package... packages2) {
		this(new ArrayList<Package>() {{ // Just need to join packages together
			add(package1);
			addAll(List.of(packages2));
		}}.toArray(Package[]::new));
	}

	/**
	 * Create a new based package.
	 * @param packages The packages, where lower indexes means higher precedence
	 */
	public BasedPackage(Package... packages) {
		super(packages);
		description = packages[0].description();

		authors = Collections.unmodifiableSet(mergeSet(Package::authors, packages));
		dependencies = Collections.unmodifiableSet(mergeSet(Package::dependencies, packages));
	}

	/**
	 * Merges sets using values got from the getter.
	 * @param getter The getter to use
	 * @param packages The packages to use
	 * @return The combined set
	 * @param <T> The type that we are combining
	 */
	private <T> Set<T> mergeSet(Function<Package, Collection<T>> getter, Package... packages) {
		Set<T> set = new HashSet<>();
		for (Package package_ : packages) {
			set.addAll(getter.apply(package_));
		}
		return set;
	}

	@Override
	public @NotNull Set<String> authors() {
		return authors;
	}

	@Override
	public @NotNull String description() {
		return description;
	}

	@Override
	public @NotNull Set<PackageReference> dependencies() {
		return dependencies;
	}
}
