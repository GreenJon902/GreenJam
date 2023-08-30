package com.greenjon902.greenJam.api.core.exceptions;

import com.greenjon902.greenJam.api.core.PackageList;

public class PackageAlreadyAddedException extends NamedPackageListException {
	public PackageAlreadyAddedException(PackageList packageList, String name, String version) {
		super(packageList, name, version);
	}

	@Override
	public String getMessage() {
		return "Package called " + formattedName() + " already is loaded";
	}
}
