package org.greenJam.api.exceptions;

import org.greenJam.api.PackageList;

public abstract class PackageListException extends RuntimeException {
	private final PackageList packageList;

	public PackageListException(PackageList packageList) {
		this.packageList = packageList;
	}
}
