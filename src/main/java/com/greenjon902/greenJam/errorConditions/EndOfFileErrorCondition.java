package com.greenjon902.greenJam.errorConditions;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.ErrorCondition;
import com.greenjon902.greenJam.common.StringInputStream;

public class EndOfFileErrorCondition extends ErrorCondition {
	@Override
	public boolean check(Contexts contexts, StringInputStream string, AstNode returnedTree) {
		return string.isEnd();
	}

	@Override
	public String toString() {
		return "EndOfFileErrorCondition{}";
	}

	@Override
	public String format() {
		return toString();
	}
}
