package com.greenjon902.greenJam.api.exceptions;

import com.greenjon902.greenJam.api.PackageList;

public abstract class PackageListException extends RuntimeException {
	private final PackageList packageList;

	public PackageListException(PackageList packageList) {
		this.packageList = packageList;
	}
}
