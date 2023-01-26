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

    @Test
    void tokenizeEscapeSequence() {
        SyntaxToken[] syntaxTokens;
        //<editor-fold desc="Escape Sequence Tests" defaultstate="collapsed">
        syntaxTokens = SyntaxTokenizer.tokenize("\\'");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "'")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\\"");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\"")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\\\");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\\")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\n");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\n")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\r");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\r")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\t");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\t")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\b");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\b")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\f");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\f")
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\0");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "\0")
        }, syntaxTokens);

        syntaxTokens = SyntaxTokenizer.tokenize("\\x00");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, String.valueOf((char) 0x00))
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\x99");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, String.valueOf((char) 0x99))
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\xAA");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, String.valueOf((char) 0xAA))
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\xFF");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, String.valueOf((char) 0xFF))
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\x14");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, String.valueOf((char) 0x14))
        }, syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("\\xC7");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, String.valueOf((char) 0xC7))
        }, syntaxTokens);
        //</editor-fold>
    }

    @Test
    void tokenizeComplexExpression() {
        SyntaxToken[] syntaxTokens;

        syntaxTokens = SyntaxTokenizer.tokenize("if <{expression}> <2== <1{expression}>1>2");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "if "),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0)),
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "expression"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 0)),
                new SyntaxToken(SyntaxTokenType.LITERAL, " "),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 2)),
                new SyntaxToken(SyntaxTokenType.LITERAL, "== "),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 1)),
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "expression"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 1)),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 2)),
        }, syntaxTokens);
    }
}