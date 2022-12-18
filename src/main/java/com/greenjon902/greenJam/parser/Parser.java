package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.tokenizer.Token;

import java.util.ArrayList;

public class Parser {
    private int location;
    private Token[] tokens;

    public Parser(Token[] tokens) {
        this.location = 0;
        this.tokens = tokens;
    }

    public AbstractSyntaxTree getParsed() {
        AbstractSyntaxTreeBuilder astBuilder = new AbstractSyntaxTreeBuilder();

        while (location < tokens.length) {

        }

        return astBuilder.toAst();
    }
}
