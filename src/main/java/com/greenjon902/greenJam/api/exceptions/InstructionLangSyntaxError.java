package com.greenjon902.greenJam.api.exceptions;

import com.greenjon902.greenJam.api.InputStream;

public class InstructionLangSyntaxError extends RuntimeException {
	private final InputStream inputStream;

	public InstructionLangSyntaxError(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public String getMessage() {
		int location = inputStream.location();

		int line = 0;
		int linePos = 0;  // The index on this line
		inputStream.push();
		inputStream.seek(0);
		while (inputStream.hasNext(1)) {
			if (inputStream.next(1).equals("\n")) {
				line += 1;
				linePos = 0;
			}
			linePos += 1;
			if (inputStream.location() == location) {
				break;
			}
		}
		inputStream.pop();

		return "Could not recognize syntax at location " + inputStream.location() + " (" + line + "," + linePos + ") in file " + inputStream.name();
	}
}
