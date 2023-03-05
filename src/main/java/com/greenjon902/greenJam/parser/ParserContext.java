package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.SyntaxRule;

import java.util.Stack;

/**
 * A class that provides storage for a parser, this should be individual to each parser as it may not function properly
 * otherwise.
 * <p>
 * <p>
 * <p> The match stack is used to ensure the syntax matcher does not get stuck in a loop by ensuring it cannot match
 * the same group (while the string location is in the same place) twice.
 */
public class ParserContext {
	public final Stack<MatchRun> matchStack = new Stack<>();

	public void matchPush(int location, SyntaxRule group) {
		matchStack.push(new MatchRun(location, group));
	}

	public void matchPop() {
		matchStack.pop();
	}

	public boolean alreadyMatching(int location, SyntaxRule syntaxRule) {
		return matchStack.search(new MatchRun(location, syntaxRule)) != -1;
	}

	private record MatchRun(int location, SyntaxRule syntaxRule) {}
}
