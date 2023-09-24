package org.greenJam.api.exceptions;
public class CannotPopStackedClassException extends RuntimeException {
	@Override
	public String getMessage() {
		return "Cannot pop the default values of StackedClassBase";
	}
}
