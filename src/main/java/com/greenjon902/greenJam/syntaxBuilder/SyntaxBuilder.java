package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxRule;

public class SyntaxBuilder {
    public static SyntaxRule build(String string) {
        return SyntaxParser.parse(SyntaxTokenizer.tokenize(string));
    }

    public static SyntaxRule build(StringInputStream stringInputStream) {
        return SyntaxParser.parse(SyntaxTokenizer.tokenize(stringInputStream));
    }
}
