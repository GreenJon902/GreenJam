package com.greenjon902.greenJam.parser.instructionHandler.syntaxBuilder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxTokenizerTest {

    @Test
    void tokenizeLiterals() {
        SyntaxToken[] syntaxTokens = SyntaxTokenizer.tokenize("foo");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo")
        }, syntaxTokens);
    }

    @Test
    void tokenizeGroupSubstitution() {
        SyntaxToken[] syntaxTokens = SyntaxTokenizer.tokenize("{bar}");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "bar")
        }, syntaxTokens);
    }

    @Test
    void tokenizeGroupOperators() {
        SyntaxToken[] syntaxTokens;

        syntaxTokens = SyntaxTokenizer.tokenize("<");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0))
        }, syntaxTokens);

        syntaxTokens = SyntaxTokenizer.tokenize(">");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 0))
        }, syntaxTokens);

        syntaxTokens = SyntaxTokenizer.tokenize("<0");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0))
        }, syntaxTokens);

        syntaxTokens = SyntaxTokenizer.tokenize(">47");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 47))
        }, syntaxTokens);
    }
}