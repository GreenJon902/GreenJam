package com.greenjon902.greenJam.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A file is a singular script inside a package.
 */
public interface File extends PackageItem {
	/**
	 * If this {@link Package} is a base, and this file overrides a file, this method
	 * will return the overwritten file.
	 * @return The file or null if it doesn't exist
	 */
	@Nullable File super_();

	/**
	 * The {@link InputStream} to read the file contents from.
	 * @return The stream
	 */
	@NotNull InputStream stream();

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("super_=").append(super_());
		sb.append(", stream=").append(stream());
		sb.append(", ");
		PackageItem.super.writeFields(sb);
	}

	@Override
	default boolean equals_(Object o, boolean sameClass) {
		if (!PackageItem.super.equals_(o, sameClass)) return false;
		if (!(o instanceof File that)) return false;
		return Objects.equals(super_(), that.super_()) && Objects.equals(stream(), that.stream());
	}

	@Override
	default int hashCode_() {
		return Objects.hash(PackageItem.super.hashCode_(), super_(), stream());
	}
}
