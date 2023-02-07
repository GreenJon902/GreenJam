package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxMatcherTest {
    @Test
    void matchLiteral() {
        assertEquals(new AstNode(),
                SyntaxMatcher.match(
                    new StringInputStream("<string>", "foo"),
                    new SyntaxRule(0,
                            new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                            new Object[]{"foo"}),
                    new SyntaxContext()));
        assertNull(SyntaxMatcher.match(
                new StringInputStream("<string>", "bar"),
                new SyntaxRule(0,
                        new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                        new Object[]{"baz"}),
                new SyntaxContext()));
    }

    @Test
    void recording() {
        assertEquals(new AstNode("foo"),
                SyntaxMatcher.match(
                    new StringInputStream("<string>", "foo"),
                    new SyntaxRule(1,
                            new SyntaxInstruction[]{
                                    SyntaxInstruction.START_RECORD,
                                    SyntaxInstruction.MATCH_LITERAL,
                                    SyntaxInstruction.STOP_RECORD,
                            },
                            new Object[]{0, "foo", 0}),
                    new SyntaxContext()));
    }

    @Test
    void matchGroup() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("bar", new SyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"foo"}));
        syntaxContext.add("baz", new SyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_GROUP},
                new Object[]{"bar"}));
        syntaxContext.add("foo", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_GROUP, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "bar", 0}));

        assertEquals(new AstNode(),
                SyntaxMatcher.match(
                    new StringInputStream("<string>", "foo"),
                "bar",
                    syntaxContext));
        assertEquals(new AstNode(),
                SyntaxMatcher.match(
                    new StringInputStream("<string>", "foo"),
                    "baz",
                    syntaxContext));
        assertNull(SyntaxMatcher.match(
                new StringInputStream("<string>", "bar"),
                "baz",
                syntaxContext));

        assertEquals(new AstNode("foo"),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "foo"),
                        "foo",
                        syntaxContext));
    }

    @Test
    void recordGroup() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("bar", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "foo", 0}));
        syntaxContext.add("baz", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.RECORD_GROUP},
                new Object[]{new Tuple.Two<>(0, "bar")}));

        assertEquals(new AstNode(new AstNode("foo")),
                SyntaxMatcher.match(
                    new StringInputStream("<string>", "foo"),
                    "baz",
                    syntaxContext));
    }
}