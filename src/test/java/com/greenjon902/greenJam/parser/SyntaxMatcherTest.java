package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxMatcherTest {
    public static char[] alphaNumericCharacterList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();

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

    @Test
    void joiningStrings() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("test_one", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "hello ", "world", 0}));
        syntaxContext.add("test_two", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD, SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_LITERAL, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "hello ", 0, 0, "world", 0}));

        assertEquals(new AstNode("hello world"),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "hello world"),
                        "test_one",
                        syntaxContext));
        assertEquals(new AstNode("hello world"),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "hello world"),
                        "test_two",
                        syntaxContext));
    }


    @Test
    void ignoringParts() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("test_one", new SyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                new Object[]{"hello"}));

        assertNull(SyntaxMatcher.match(
                        new StringInputStream("<string>", " hello"),
                        "test_one",
                        syntaxContext));

        syntaxContext.ignore(" ");

        assertEquals(new AstNode(),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", " hello"),
                        "test_one",
                        syntaxContext));

        syntaxContext.ignore("not");

        assertEquals(new AstNode(),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "nothello"),
                        "test_one",
                        syntaxContext));
    }

    @Test
    void matchComplexExpressions1() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("identifier", new SyntaxRule(1,
                new SyntaxInstruction[]{
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD},
                new Object[]{0,
                        "test",
                        0}));


        syntaxContext.add("expression_level_one", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "expression_level_two"),
                        0,
                        "+",
                        0,
                        new Tuple.Two<>(2, "expression_level_one")}));
        syntaxContext.addLink("expression_level_one", "expression_level_two");


        syntaxContext.add("expression_level_two", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "identifier"),
                        0,
                        "*",
                        0,
                        new Tuple.Two<>(2, "expression_level_two")}));
        syntaxContext.addLink("expression_level_two", "identifier");



        assertEquals(new AstNode("test"),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("*", new AstNode("test"), new AstNode("test")),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test*test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+", new AstNode("test"), new AstNode("test")),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test+test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+",
                                new AstNode("*",
                                        new AstNode("test"),
                                        new AstNode("test")),
                                new AstNode("test")),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test*test+test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+",
                        new AstNode("test"),
                        new AstNode("*",
                                new AstNode("test"),
                                new AstNode("test"))),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test+test*test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+",
                        new AstNode("test"),
                        new AstNode("+",
                                new AstNode("*",
                                        new AstNode("test"),
                                        new AstNode("test")),
                                new AstNode("test"))),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test+test*test+test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+",
                        new AstNode("test"),
                        new AstNode("*",
                                new AstNode("test"),
                                new AstNode("*",
                                        new AstNode("test"),
                                        new AstNode("test")))),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "test+test*test*test"),
                        "expression_level_one",
                        syntaxContext));
    }

    @Test
    void matchComplexExpressions2() {
        SyntaxContext syntaxContext = new SyntaxContext();

        // Setup ---------
        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("identifier_character", new SyntaxRule(0,
                    new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                    new Object[]{String.valueOf(character)})); // The actual program doesn't know difference between string and character so make string
        }
        syntaxContext.ignore(" ");


        // Identifiers ---------
        syntaxContext.add("identifier_characters", new SyntaxRule(0,
                new SyntaxInstruction[]{
                        SyntaxInstruction.MATCH_GROUP,
                        SyntaxInstruction.MATCH_GROUP},
                new Object[]{
                        "identifier_character", "identifier_characters"}));
        syntaxContext.add("identifier_characters", new SyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_GROUP},
                new Object[]{"identifier_character"}));

        syntaxContext.add("identifier", new SyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_GROUP, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "identifier_characters", 0}));


        // BIDMAS ---------
        // Subtraction
        syntaxContext.add("expression_level_one", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "expression_level_two"),
                        0,
                        "-",
                        0,
                        new Tuple.Two<>(2, "expression_level_one")}));
        syntaxContext.addLink("expression_level_one", "expression_level_two");
        // Addition
        syntaxContext.add("expression_level_two", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "expression_level_three"),
                        0,
                        "+",
                        0,
                        new Tuple.Two<>(2, "expression_level_two")}));
        syntaxContext.addLink("expression_level_two", "expression_level_three");
        // Multiplication
        syntaxContext.add("expression_level_three", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "expression_level_four"),
                        0,
                        "*",
                        0,
                        new Tuple.Two<>(2, "expression_level_three")}));
        syntaxContext.addLink("expression_level_three", "expression_level_four");
        // Division
        syntaxContext.add("expression_level_four", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "expression_level_five"),
                        0,
                        "/",
                        0,
                        new Tuple.Two<>(2, "expression_level_four")}));
        syntaxContext.addLink("expression_level_four", "expression_level_five");
        // Division
        syntaxContext.add("expression_level_five", new SyntaxRule(3,
                new SyntaxInstruction[]{
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.RECORD_GROUP},
                new Object[]{
                        new Tuple.Two<>(1, "expression_level_six"),
                        0,
                        "^",
                        0,
                        new Tuple.Two<>(2, "expression_level_five")}));
        syntaxContext.addLink("expression_level_five", "expression_level_six");
        // Brackets
        syntaxContext.add("expression_level_six", new SyntaxRule(1,
                new SyntaxInstruction[]{
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.RECORD_GROUP,
                        SyntaxInstruction.MATCH_LITERAL},
                new Object[]{
                        "(",
                        new Tuple.Two<>(0, "expression_level_one"),
                        ")"
                }));
        syntaxContext.addLink("expression_level_six", "identifier");


        // Testing
        assertEquals(new AstNode("hello"),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "hello"),
                        "expression_level_one",
                        syntaxContext));

        assertEquals(new AstNode("+",
                        new AstNode("hello"),
                        new AstNode("world")),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "hello+world"),
                        "expression_level_one",
                        syntaxContext));

        assertEquals(new AstNode("+",
                        new AstNode("how"),
                        new AstNode("*",
                                new AstNode("are"),
                                new AstNode("you"))),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "how+are*you"),
                        "expression_level_one",
                        syntaxContext));

        // Quadratic formula!!
        assertEquals(
                new AstNode("/",
                        new AstNode(
                            new AstNode("+",
                                    new AstNode(
                                        new AstNode("-",
                                                new AstNode("zero"),
                                                new AstNode("b")
                                        )
                                    ),
                                        new AstNode("^",
                                                new AstNode(
                                                    new AstNode("-",
                                                            new AstNode("^",
                                                                    new AstNode("b"),
                                                                    new AstNode("two")
                                                            ),
                                                            new AstNode("*",
                                                                    new AstNode("four"),
                                                                    new AstNode("*",
                                                                            new AstNode("a"),
                                                                            new AstNode("c")
                                                                    )
                                                            )
                                                    )
                                                ),
                                                new AstNode(
                                                    new AstNode("/",
                                                            new AstNode("one"),
                                                            new AstNode("two")
                                                    )
                                                )
                                        )
                            )
                        ),
                        new AstNode(
                                new AstNode("*",
                                        new AstNode("two"),
                                        new AstNode("a"))
                        )
                ),
                SyntaxMatcher.match(
                        new StringInputStream("<string>", "((zero-b)+(b^two-four*a*c)^(one/two))/(two*a)"),
                        "expression_level_one",
                        syntaxContext));
    }
}