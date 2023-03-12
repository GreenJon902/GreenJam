package com.greenjon902.greenJam.errorConditions;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.ErrorCondition;
import com.greenjon902.greenJam.common.StringInputStream;

import java.util.Objects;

public class NextIsErrorCondition extends ErrorCondition {
	private final String string;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NextIsErrorCondition that = (NextIsErrorCondition) o;
		return string.equals(that.string);
	}

	@Override
	public int hashCode() {
		return Objects.hash(string);
	}

	@Override
	public String toString() {
		return "NextIsErrorCondition{" +
				"string='" + string + '\'' +
				'}';
	}

	@Override
	public String format() {
		return null;
	}

	public NextIsErrorCondition(String string) {
		this.string = string;
	}

	@Override
	public boolean check(Contexts contexts, StringInputStream stringInputStream, AstNode returnedTree) {
		return stringInputStream.nextIs(string);
	}
}
