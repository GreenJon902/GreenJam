package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The base class for any item that exists inside a package (including the package itself).
 *
 * @implSpec These items should be immutable after creation. But these items should support being duplicated and used
 * as children multiple times. (So overrides and bases don't need to compile things multiple times)
 */
public interface PackageItem extends FieldStringWriter {
	/**
	 * Gets the name of this item.
	 * @return The name
	 */
	@NotNull String name();

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("name='").append(name()).append('\'');
	}

	/**
	 * See {@link #equals_(Object, boolean)}
	 */
	default boolean equals_(Object o) {
		return equals_(o, false);
	}

	/**
	 * The default equals implementation.
	 * @param o The other object
	 * @param sameClass Do we require the class to be the same
	 * @return True if it is equals
	 */
	default boolean equals_(Object o, boolean sameClass) {
		if (this == o) return true;
		if (sameClass && (this.getClass() != o.getClass())) return false;
		if (!(o instanceof PackageItem that)) return false;
		return Objects.equals(name(), that.name());
	}

	/**
	 * The default hashCode implementation.
	 * @return the hash code
	 */
	default int hashCode_() {
		return Objects.hash(name());
	}
}
