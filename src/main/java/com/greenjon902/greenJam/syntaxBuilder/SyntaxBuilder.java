package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxMatcher;

public class SyntaxBuilder {
    public static SyntaxMatcher build(String string) {
        return SyntaxParser.parse(SyntaxTokenizer.tokenize(string));
    }
}
