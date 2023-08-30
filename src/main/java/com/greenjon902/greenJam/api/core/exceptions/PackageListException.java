package com.greenjon902.greenJam.api.core.exceptions;

import com.greenjon902.greenJam.api.core.PackageList;

public abstract class PackageListException extends RuntimeException {
	private final PackageList packageList;

	public PackageListException(PackageList packageList) {
		this.packageList = packageList;
	}
}
