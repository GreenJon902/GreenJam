package com.greenjon902.greenJam.api.core.exceptions;

import com.greenjon902.greenJam.api.core.PackageList;

public class NoSuchPackageException extends NamedPackageListException {
	public NoSuchPackageException(PackageList packageList, String name, String version) {
		super(packageList, name, version);
	}

	@Override
	public String getMessage() {
		return "No loaded package called " + formattedName();
	}
}
