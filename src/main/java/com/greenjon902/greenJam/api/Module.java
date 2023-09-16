package com.greenjon902.greenJam.api;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

/**
 * A module is a folder inside a package, it contains files and submodules. Some configs can change within a
 * submodule.
 */
public interface Module extends PackageItem {
	/**
	 * Gets the children modules.
	 *
	 * @implSpec The set should be immutable
	 * @return A shallow clone of the modules list
	 */
	@NotNull Set<Module> modules();

	/**
	 * Gets the children files.
	 *
	 * @implSpec The set should be immutable
	 * @return A shallow clone of the file list
	 */
	@NotNull Set<File> files();

	@Override
	default void writeFields(StringBuilder sb) {
		sb.append("modules=").append(Arrays.toString(modules().stream().sorted(Comparator.comparing(Module::hashCode_)).toArray()));
		sb.append(", files=").append(Arrays.toString(files().stream().sorted(Comparator.comparing(File::hashCode_)).toArray()));
		sb.append(", ");
		PackageItem.super.writeFields(sb);
	}

	@Override
	default boolean equals_(Object o, boolean sameClass) {
		if (!PackageItem.super.equals_(o, sameClass)) return false;
		if (!(o instanceof Module that)) return false;
		return Objects.equals(modules(), that.modules()) && Objects.equals(files(), that.files());
	}

	@Override
	default int hashCode_() {
		return Objects.hash(PackageItem.super.hashCode_(), modules(), files());
	}
}
