package org.greenJam.api.exceptions;

import org.greenJam.api.PackageList;

public class NoSuchPackageException extends NamedPackageListException {
	public NoSuchPackageException(PackageList packageList, String name, String version) {
		super(packageList, name, version);
	}

	@Override
	public String getMessage() {
		return "No loaded package called " + formattedName();
	}
}
