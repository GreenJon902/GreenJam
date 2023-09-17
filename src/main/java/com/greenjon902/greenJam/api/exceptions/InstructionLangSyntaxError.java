package com.greenjon902.greenJam.api.exceptions;

import com.greenjon902.greenJam.api.InputStream;

public class InstructionLangSyntaxError extends RuntimeException {
	private final InputStream inputStream;

	public InstructionLangSyntaxError(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public String getMessage() {
		return "Could not recognize syntax at location " + inputStream.location() + " in file " + inputStream.name();
	}
}
