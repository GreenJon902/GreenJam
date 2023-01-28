package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxParserTest {

    @Test
    void highest() {
        assertEquals(5, SyntaxParser.highest(4, 5));
        assertEquals(10, SyntaxParser.highest(10, 5));
        assertEquals(52, SyntaxParser.highest(-1, 52));
    }

    @Test
    void parseLiteral() {
        SyntaxMatcherImpl syntaxMatcher;

        syntaxMatcher = (SyntaxMatcherImpl) SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo")
        });
        assertEquals(new SyntaxMatcherImpl(
                0, new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL}, new Object[]{"foo"}
        ), syntaxMatcher);

        syntaxMatcher = (SyntaxMatcherImpl) SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo bar")
        });
        assertEquals(new SyntaxMatcherImpl(
                0, new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL}, new Object[]{"foo bar"}
        ), syntaxMatcher);
    }

    @Test
    void parseRecording() {
        SyntaxMatcherImpl syntaxMatcher;

        syntaxMatcher = (SyntaxMatcherImpl) SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0)),
                new SyntaxToken(SyntaxTokenType.LITERAL, "bar"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 0))
        });
        assertEquals(new SyntaxMatcherImpl(
                0, new SyntaxInstruction[]{
                        SyntaxInstruction.START_RECORD_CHARACTER,
                        SyntaxInstruction.MATCH_LITERAL,
                SyntaxInstruction.STOP_RECORD_CHARACTER
                }, new Object[]{0, "bar", 0}
        ), syntaxMatcher);

        syntaxMatcher = (SyntaxMatcherImpl) SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 7)),
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "baz"),
                new SyntaxToken(SyntaxTokenType.LITERAL, "bar"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 7))
        });
        assertEquals(new SyntaxMatcherImpl(
                7, new SyntaxInstruction[]{
                SyntaxInstruction.START_RECORD_CHARACTER,
                SyntaxInstruction.MATCH_GROUP,
                SyntaxInstruction.MATCH_LITERAL,
                SyntaxInstruction.STOP_RECORD_CHARACTER
        }, new Object[]{7, "baz", "bar", 7}
        ), syntaxMatcher);

        syntaxMatcher = (SyntaxMatcherImpl) SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0)),
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "baz"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 0))
        });
        assertEquals(new SyntaxMatcherImpl(
                0, new SyntaxInstruction[]{
                SyntaxInstruction.RECORD_GROUP
        }, new Object[]{new Tuple.Two<>(0, "baz")}
        ), syntaxMatcher);
    }

    @Test
    void parseGroupSubstitution() {
        SyntaxMatcherImpl syntaxMatcher = (SyntaxMatcherImpl) SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "foo")
        });
        assertEquals(new SyntaxMatcherImpl(
                0, new SyntaxInstruction[]{SyntaxInstruction.MATCH_GROUP}, new Object[]{"foo"}
        ), syntaxMatcher);
    }
}