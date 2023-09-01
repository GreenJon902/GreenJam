package com.greenjon902.greenJam.api.core.exceptions;

import com.greenjon902.greenJam.api.core.PackageList;
import com.greenjon902.greenJam.api.core.PackageReference;

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
