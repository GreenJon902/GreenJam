package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxMatcherTest { // TODO: Add tests that check the actual structure of AstNodes
    @Test
    void matchLiteral() {
        assertNotNull(SyntaxMatcher.match(
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
        assertNotNull(SyntaxMatcher.match(
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

        assertNotNull(SyntaxMatcher.match(
                new StringInputStream("<string>", "foo"),
                "bar",
                syntaxContext));
        assertNotNull(SyntaxMatcher.match(
                new StringInputStream("<string>", "foo"),
                "baz",
                syntaxContext));
        assertNull(SyntaxMatcher.match(
                new StringInputStream("<string>", "bar"),
                "baz",
                syntaxContext));
    }

    @Test
    void recordGroup() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("bar", new SyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"foo"}));
        syntaxContext.add("baz", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.RECORD_GROUP},
                new Object[]{new Tuple.Two<>(0, "bar")}));

        assertNotNull(SyntaxMatcher.match(
                new StringInputStream("<string>", "foo"),
                "baz",
                syntaxContext));
    }
}