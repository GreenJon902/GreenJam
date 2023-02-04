package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.common.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxParserTest {

    @Test
    void parseLiteral() {
        SyntaxRule syntaxMatcher;

        syntaxMatcher = SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo")
        });
        assertEquals(new SyntaxRule(
                0, new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL}, new Object[]{"foo"}
        ), syntaxMatcher);

        syntaxMatcher = SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.LITERAL, "foo bar")
        });
        assertEquals(new SyntaxRule(
                0, new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL}, new Object[]{"foo bar"}
        ), syntaxMatcher);
    }

    @Test
    void parseRecording() {
        SyntaxRule syntaxMatcher;

        syntaxMatcher = SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0)),
                new SyntaxToken(SyntaxTokenType.LITERAL, "bar"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 0))
        });
        assertEquals(new SyntaxRule(
                1, new SyntaxInstruction[]{
                        SyntaxInstruction.START_RECORD_CHARACTER,
                        SyntaxInstruction.MATCH_LITERAL,
                SyntaxInstruction.STOP_RECORD_CHARACTER
                }, new Object[]{0, "bar", 0}
        ), syntaxMatcher);

        syntaxMatcher = SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 7)),
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "baz"),
                new SyntaxToken(SyntaxTokenType.LITERAL, "bar"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 7))
        });
        assertEquals(new SyntaxRule(
                8, new SyntaxInstruction[]{
                SyntaxInstruction.START_RECORD_CHARACTER,
                SyntaxInstruction.MATCH_GROUP,
                SyntaxInstruction.MATCH_LITERAL,
                SyntaxInstruction.STOP_RECORD_CHARACTER
        }, new Object[]{7, "baz", "bar", 7}
        ), syntaxMatcher);

        syntaxMatcher = SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, 0)),
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "baz"),
                new SyntaxToken(SyntaxTokenType.OPERATOR, new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, 0))
        });
        assertEquals(new SyntaxRule(
                1, new SyntaxInstruction[]{
                SyntaxInstruction.RECORD_GROUP
        }, new Object[]{new Tuple.Two<>(0, "baz")}
        ), syntaxMatcher);
    }

    @Test
    void parseGroupSubstitution() {
        SyntaxRule syntaxMatcher = SyntaxParser.parse(new SyntaxToken[] {
                new SyntaxToken(SyntaxTokenType.GROUP_SUBSTITUTION, "foo")
        });
        assertEquals(new SyntaxRule(
                0, new SyntaxInstruction[]{SyntaxInstruction.MATCH_GROUP}, new Object[]{"foo"}
        ), syntaxMatcher);
    }
}