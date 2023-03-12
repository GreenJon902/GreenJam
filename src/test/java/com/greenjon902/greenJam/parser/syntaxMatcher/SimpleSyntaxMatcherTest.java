package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SimpleSyntaxMatcherTest {
    @Test
    void matchLiteral() {
        assertEquals(new AstNode(),
                new SimpleSyntaxRule(0,
                        new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                        new Object[]{"foo"}
                ).match(
                        "foo",
                        new Contexts(new SyntaxContext())));
        assertNull(new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"baz"}).match(
                "bar",
                new Contexts(new SyntaxContext())));
    }

    @Test
    void recording() {
        assertEquals(new AstNode("foo"),
                new SimpleSyntaxRule(1,
                        new SyntaxInstruction[]{
                                SyntaxInstruction.START_RECORD,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.STOP_RECORD,
                        },
                        new Object[]{0, "foo", 0}).match(
                        "foo",

                        new Contexts(new SyntaxContext())));
    }

    @Test
    void matchGroup() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("bar", new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"foo"}));
        syntaxContext.add("baz", new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_GROUP},
                new Object[]{"bar"}));
        syntaxContext.add("foo", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_GROUP, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "bar", 0}));

        assertEquals(new AstNode(),
                SyntaxRule.match(
                        "foo",
                        "bar",
                        syntaxContext, new ErrorContext()));
        assertEquals(new AstNode(),
                SyntaxRule.match(
                        "foo",
                        "baz",
                        syntaxContext, new ErrorContext()));
        assertNull(SyntaxRule.match(
                "bar",
                "baz",
                syntaxContext, new ErrorContext()));

        assertEquals(new AstNode("foo"),
                SyntaxRule.match(
                        "foo",
                        "foo",
                        syntaxContext, new ErrorContext()));
    }

    @Test
    void recordGroup() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("bar", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "foo", 0}));
        syntaxContext.add("baz", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.RECORD_GROUP},
                new Object[]{new Tuple.Two<>(0, "bar")}));

        assertEquals(new AstNode(new AstNode("foo")),
                SyntaxRule.match(
                        "foo",
                        "baz",
                        syntaxContext, new ErrorContext()));
    }

    @Test
    void joiningStrings() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("test_one", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "hello ", "world", 0}));
        syntaxContext.add("test_two", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD, SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "hello ", 0, 0, "world", 0}));

        assertEquals(new AstNode("hello world"),
                SyntaxRule.match(
                        "hello world",
                        "test_one",
                        syntaxContext, new ErrorContext()));
        assertEquals(new AstNode("hello world"),
                SyntaxRule.match(
                        "hello world",
                        "test_two",
                        syntaxContext, new ErrorContext()));
    }


    @Test
    void ignoringParts() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("test_one", new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"hello"}));

        assertNull(SyntaxRule.match(
                " hello",
                "test_one",
                syntaxContext, new ErrorContext()));

        syntaxContext.ignore(" ");

        assertEquals(new AstNode(),
                SyntaxRule.match(
                        " hello",
                        "test_one",
                        syntaxContext, new ErrorContext()));

        syntaxContext.ignore("not");

        assertEquals(new AstNode(),
                SyntaxRule.match(
                        "nothello",
                        "test_one",
                        syntaxContext, new ErrorContext()));
    }

}