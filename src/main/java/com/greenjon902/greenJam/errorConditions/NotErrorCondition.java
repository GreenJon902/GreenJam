package com.greenjon902.greenJam.errorConditions;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.ErrorCondition;
import com.greenjon902.greenJam.common.StringInputStream;

import java.util.Objects;

public class NotErrorCondition extends ErrorCondition {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NotErrorCondition that = (NotErrorCondition) o;
		return other_group.equals(that.other_group);
	}

	@Override
	public int hashCode() {
		return Objects.hash(other_group);
	}

	@Override
	public String toString() {
		return "NotErrorCondition{" +
				"other_group='" + other_group + '\'' +
				'}';
	}

	@Override
	public String format() {
		return toString();
	}

	private final String other_group;

	public NotErrorCondition(String other_group) {
		this.other_group = other_group;
	}

	@Override
	public boolean check(Contexts contexts, StringInputStream string, AstNode returnedTree) {
		return !contexts.error.getError(other_group).check(contexts, string, returnedTree);
	}
}
