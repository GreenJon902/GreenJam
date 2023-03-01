package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.Tuple;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SyntaxTokenizerTest {

    @Test
    void tokenizeLiterals() {
        SyntaxToken[] syntaxTokens;

        syntaxTokens = SyntaxTokenizer.tokenize("foo");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo")
        }, syntaxTokens);

        syntaxTokens = SyntaxTokenizer.tokenize("foo bar");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo bar")
        }, syntaxTokens);
    }

    @Test
    void tokenizeGroupSubstitution() {
        SyntaxToken[] syntaxTokens = SyntaxTokenizer.tokenize("[bar]");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "bar")
        }, syntaxTokens);
    }

    @Test
    void tokenizeRecordedGroupSubstitution() {
        SyntaxToken[] syntaxTokens = SyntaxTokenizer.tokenize("{foo}");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.RECORDED_GROUP_SUBSTITUTION, new Tuple.Two<>(0, "foo"))
        }, syntaxTokens);

        syntaxTokens = SyntaxTokenizer.tokenize("{6bar}");
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.RECORDED_GROUP_SUBSTITUTION, new Tuple.Two<>(6, "bar"))
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
        syntaxTokens = SyntaxTokenizer.tokenize("`");
        assertArrayEquals(new SyntaxToken[0], syntaxTokens);
        syntaxTokens = SyntaxTokenizer.tokenize("`<");
        assertArrayEquals(new SyntaxToken[0], syntaxTokens);
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

        syntaxTokens = SyntaxTokenizer.tokenize("[if_keyword] {expression} <2== {1expression}>2`not recorded");
        System.out.println(Arrays.toString(syntaxTokens));
        assertArrayEquals(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "if_keyword"),
                new SyntaxToken(SyntaxTokenType.LITERAL, " "),
                new SyntaxToken(SyntaxTokenType.RECORDED_GROUP_SUBSTITUTION, new Tuple.Two<>(0, "expression")),
                new SyntaxToken(SyntaxTokenType.LITERAL, " "),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 2)),
                new SyntaxToken(SyntaxTokenType.LITERAL, "== "),
                new SyntaxToken(SyntaxTokenType.RECORDED_GROUP_SUBSTITUTION, new Tuple.Two<>(1, "expression")),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 2)),
        }, syntaxTokens);
    }
}