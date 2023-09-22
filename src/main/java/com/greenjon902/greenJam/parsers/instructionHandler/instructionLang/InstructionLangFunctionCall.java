package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import java.util.Arrays;
import java.util.Objects;

public record InstructionLangFunctionCall(InstructionLangTreeLeaf function, InstructionLangTreeLeaf... args) implements
		InstructionLangTreeLeaf {
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		InstructionLangFunctionCall that = (InstructionLangFunctionCall) object;
		return Objects.equals(function, that.function) && Arrays.equals(args, that.args);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(function);
		result = 31 * result + Arrays.hashCode(args);
		return result;
	}

	@Override
	public String toString() {
		return "InstructionLangFunctionCall{" +
				"function=" + function +
				", args=" + Arrays.toString(args) +
				'}';
	}
}
