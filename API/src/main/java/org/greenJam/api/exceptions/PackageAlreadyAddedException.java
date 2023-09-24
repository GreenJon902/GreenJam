package org.greenJam.api.exceptions;

import org.greenJam.api.PackageList;

public class PackageAlreadyAddedException extends NamedPackageListException {
	public PackageAlreadyAddedException(PackageList packageList, String name, String version) {
		super(packageList, name, version);
	}

	@Override
	public String getMessage() {
		return "Package called " + formattedName() + " already is loaded";
	}
}
