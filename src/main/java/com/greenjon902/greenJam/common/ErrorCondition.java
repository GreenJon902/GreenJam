package com.greenjon902.greenJam.common;

public abstract class ErrorCondition implements Printable {
	public abstract boolean check(Contexts contexts, StringInputStream string, AstNode returnedTree);
}
