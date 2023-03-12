package com.greenjon902.greenJam.errorConditions;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.ErrorCondition;
import com.greenjon902.greenJam.common.StringInputStream;

import java.util.Objects;

public class AndErrorCondition extends ErrorCondition {
	private final String group1;
	private final String group2;

	public AndErrorCondition(String group1, String group2) {
		this.group1 = group1;
		this.group2 = group2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AndErrorCondition that = (AndErrorCondition) o;
		return group1.equals(that.group1) && group2.equals(that.group2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(group1, group2);
	}

	@Override
	public String toString() {
		return "AndErrorCondition{" +
				"group1='" + group1 + '\'' +
				", group2='" + group2 + '\'' +
				'}';
	}

	@Override
	public String format() {
		return null;
	}

	@Override
	public boolean check(Contexts contexts, StringInputStream string, AstNode returnedTree) {
		return contexts.error.getError(group1).check(contexts, string, returnedTree) &&
				contexts.error.getError(group2).check(contexts, string, returnedTree);
	}
}
