package com.greenjon902.greenJam.core.packageLoader.basedPackageHelpers;

import com.greenjon902.greenJam.api.core.File;
import com.greenjon902.greenJam.utils.FieldStringWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * See {@link BasedPackage}.
 */
public class BasedFile extends BasedPackageItem implements File, FieldStringWriter {
	private final File file;
	private final File superFile;

	public BasedFile(File file, File superFile) {
		this.file = file;
		this.superFile = superFile;
	}

	@Override
	public @Nullable File super_() {
		return superFile;
	}

	@Override
	public @NotNull String name() {
		return file.name();
	}
}
