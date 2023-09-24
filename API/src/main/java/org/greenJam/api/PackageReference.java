package org.greenJam.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface PackageReference extends FieldStringWriter, InterfaceComparable {

	/**
	 * Gets the actual name of the package, this is the one that would be put as the key in a dependency.
	 * @return The name
	 */
	@NotNull String realName();

	/**
	 * Gets the version of the package.
	 * @return The version
	 */
	@NotNull String version();

	/**
	 * Gets the name that the user would call this package. <br>
	 * E.g. package "a" uses package "b", but calls it "c".
	 * @return The name
	 */
	@NotNull String referName();

	/**
	 * Formats a name and version in the style that is used for installed dependency storage.
	 */
	default @NotNull String formatName() {
		return formatName(realName(), version());
	}

	/**
	 * Formats a name and version in the style that is used for installed dependency storage.
	 * @param name The name of the package
	 * @param version The version of this installation
	 * @return The formatted string
	 */
	static @NotNull String formatName(@NotNull String name, @Nullable String version) {
		if (version != null && !version.isEmpty()) {
			name = name + "-" + version;
		}
		return name;
	}

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("realName='").append(realName()).append('\'');
		sb.append("referName='").append(referName()).append('\'');
		sb.append("version='").append(version()).append('\'');
	}

	@Override
	default boolean equals_(Object o, boolean sameClass) {
		if (this == o) return true;
		if (sameClass && (this.getClass() != o.getClass())) return false;
		if (!(o instanceof PackageReference that)) return false;
		return Objects.equals(realName(), that.realName()) && Objects.equals(referName(), that.referName()) &&
				Objects.equals(version(), that.version());
	}

	@Override
	default int hashCode_() {
		return Objects.hash(realName(), referName(), version());
	}
}
