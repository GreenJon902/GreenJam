package org.greenJam.api.exceptions;

import org.greenJam.api.PackageList;
import org.greenJam.api.PackageReference;

public abstract class NamedPackageListException extends PackageListException {
	public final String name;
	public final String version;

	public NamedPackageListException(PackageList packageList, String name, String version) {
		super(packageList);
		this.name = name;
		this.version = version;
	}

	public String formattedName() {
		return PackageReference.formatName(name, version);
	}
}
