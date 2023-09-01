package com.greenjon902.greenJam.api.core;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

/**
 * A package is the largest part of a program, it encompasses all modules and files.
 */
public interface Package extends Module {
	/**
	 * This name is not used for anything, apart from logging. It should be this format "name-version"
	 */
	@Override
	@NotNull String name();

	/**
	 * Gets the authors of this package.
	 *
	 * @implSpec The set should be immutable
	 * @return The list of authors
	 */
	@NotNull Set<String> authors();

	/**
	 * Gets the description of this package.
	 * @return The description
	 */
	@NotNull String description();

	/**
	 * Gets the dependencies of this package.
	 *
	 * @implSpec The set should be immutable
	 * @return The dependencies as references (which are resolved using the package list)
	 */
	@NotNull Set<PackageReference> dependencies();

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("authors=").append(Arrays.toString(authors().stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
		sb.append(", description='").append(description()).append('\'');
		sb.append(", dependencies=").append(Arrays.toString(dependencies().stream().sorted(Comparator.comparing(Object::hashCode)).toArray()));
		sb.append(", ");
		Module.super.writeFields(sb);
	}

	@Override
	default boolean equals_(Object o, boolean sameClass) {
		if (!Module.super.equals_(o, sameClass)) return false;
		if (!(o instanceof Package that)) return false;
		return Objects.equals(authors(), that.authors()) && Objects.equals(description(), that.description()) &&
				Objects.equals(dependencies(), that.dependencies());
	}

	@Override
	default int hashCode_() {
		return Objects.hash(Module.super.hashCode_(), authors(), description(), dependencies());
	}
}
