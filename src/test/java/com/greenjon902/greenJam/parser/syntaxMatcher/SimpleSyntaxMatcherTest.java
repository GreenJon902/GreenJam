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
                        new StringInputStream("<string>", "foo"),
                        new SyntaxContext()));
        assertNull(new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"baz"}).match(
                new StringInputStream("<string>", "bar"),
                new SyntaxContext()));
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
                        new StringInputStream("<string>", "foo"),

                        new SyntaxContext()));
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
                        new StringInputStream("<string>", "foo"),
                        "bar",
                        syntaxContext));
        assertEquals(new AstNode(),
                SyntaxRule.match(
                        new StringInputStream("<string>", "foo"),
                        "baz",
                        syntaxContext));
        assertNull(SyntaxRule.match(
                new StringInputStream("<string>", "bar"),
                "baz",
                syntaxContext));

        assertEquals(new AstNode("foo"),
                SyntaxRule.match(
                        new StringInputStream("<string>", "foo"),
                        "foo",
                        syntaxContext));
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
                        new StringInputStream("<string>", "foo"),
                        "baz",
                        syntaxContext));
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
                        new StringInputStream("<string>", "hello world"),
                        "test_one",
                        syntaxContext));
        assertEquals(new AstNode("hello world"),
                SyntaxRule.match(
                        new StringInputStream("<string>", "hello world"),
                        "test_two",
                        syntaxContext));
    }


    @Test
    void ignoringParts() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("test_one", new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"hello"}));

        assertNull(SyntaxRule.match(
                new StringInputStream("<string>", " hello"),
                "test_one",
                syntaxContext));

        syntaxContext.ignore(" ");

        assertEquals(new AstNode(),
                SyntaxRule.match(
                        new StringInputStream("<string>", " hello"),
                        "test_one",
                        syntaxContext));

        syntaxContext.ignore("not");

        assertEquals(new AstNode(),
                SyntaxRule.match(
                        new StringInputStream("<string>", "nothello"),
                        "test_one",
                        syntaxContext));
    }

}