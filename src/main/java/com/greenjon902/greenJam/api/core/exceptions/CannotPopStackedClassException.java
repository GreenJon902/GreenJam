package com.greenjon902.greenJam.api.core.exceptions;

import com.greenjon902.greenJam.utils.StackedClassBase;

public class CannotPopStackedClassException extends RuntimeException {
	private final StackedClassBase stackedClassBase;

	public CannotPopStackedClassException(StackedClassBase stackedClassBase) {
		this.stackedClassBase = stackedClassBase;
	}

	@Override
	public String getMessage() {
		return "Cannot pop the default values of StackedClassBase";
	}
}
