package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.parser.ParserContext;

public class Contexts {
	public final SyntaxContext syntax;
	public final ParserContext parser;
	public final ErrorContext error;

	public Contexts(SyntaxContext syntax, ParserContext parser, ErrorContext error) {
		this.syntax = syntax;
		this.parser = parser;
		this.error = error;
	}

	public Contexts(SyntaxContext syntax) {
		this(syntax, new ErrorContext());
	}

	public Contexts(SyntaxContext syntax, ErrorContext error) {
		this(syntax, new ParserContext(), error);
	}
}
