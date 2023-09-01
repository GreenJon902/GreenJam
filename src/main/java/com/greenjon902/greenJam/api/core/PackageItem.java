package com.greenjon902.greenJam.api.core;

import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The base class for any item that exists inside a package (including the package itself).
 *
 * @implSpec These items should be immutable after creation. But these items should support being duplicated and used
 * as children multiple times. (So bases don't need to compile things multiple times)
 */
public interface PackageItem extends FieldStringWriter, InterfaceComparable {
	/**
	 * Gets the name of this item.
	 * @return The name
	 */
	@NotNull String name();

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("name='").append(name()).append('\'');
	}

	@Override
	default boolean equals_(Object o, boolean sameClass) {
		if (this == o) return true;
		if (sameClass && (this.getClass() != o.getClass())) return false;
		if (!(o instanceof PackageItem that)) return false;
		return Objects.equals(name(), that.name());
	}

	@Override
	default int hashCode_() {
		return Objects.hash(name());
	}
}
