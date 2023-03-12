package com.greenjon902.greenJam.errorConditions;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.ErrorCondition;
import com.greenjon902.greenJam.common.StringInputStream;

import java.util.Objects;

public class ReturningNullErrorCondition extends ErrorCondition {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash();
	}

	@Override
	public String toString() {
		return "ReturningNullErrorCondition{}";
	}

	@Override
	public String format() {
		return toString();
	}

	@Override
	public boolean check(Contexts contexts, StringInputStream string, AstNode returnedTree) {
		return returnedTree == null;
	}
}
