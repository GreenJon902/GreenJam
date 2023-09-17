package com.greenjon902.greenJam.api.exceptions;

public class InstructionLangParserSyntaxError extends RuntimeException {

	private final String message;

	public InstructionLangParserSyntaxError(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
