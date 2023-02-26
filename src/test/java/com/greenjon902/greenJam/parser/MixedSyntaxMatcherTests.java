package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MixedSyntaxMatcherTests {
    public static char[] alphaNumericCharacterList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();

    @Test
    void matchComplexExpressions1() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("identifier", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_LITERAL,
                        SyntaxInstruction.STOP_RECORD},
                new Object[]{0,
                        "test",
                        0}));


        syntaxContext.add("expression_level_one", new SimpleSyntaxRule(3,
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


        syntaxContext.add("expression_level_two", new SimpleSyntaxRule(3,
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
                SyntaxRule.match(
                        new StringInputStream("<string>", "test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("*", new AstNode("test"), new AstNode("test")),
                SyntaxRule.match(
                        new StringInputStream("<string>", "test*test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+", new AstNode("test"), new AstNode("test")),
                SyntaxRule.match(
                        new StringInputStream("<string>", "test+test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+",
                        new AstNode("*",
                                new AstNode("test"),
                                new AstNode("test")),
                        new AstNode("test")),
                SyntaxRule.match(
                        new StringInputStream("<string>", "test*test+test"),
                        "expression_level_one",
                        syntaxContext));
        assertEquals(new AstNode("+",
                        new AstNode("test"),
                        new AstNode("*",
                                new AstNode("test"),
                                new AstNode("test"))),
                SyntaxRule.match(
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
                SyntaxRule.match(
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
                SyntaxRule.match(
                        new StringInputStream("<string>", "test+test*test*test"),
                        "expression_level_one",
                        syntaxContext));
    }

    @Test
    void matchComplexExpressions2() {
        SyntaxContext syntaxContext = new SyntaxContext();

        // Setup ---------
        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("identifier_character", new SimpleSyntaxRule(0,
                    new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                    new Object[]{String.valueOf(character)})); // The actual program doesn't know difference between string and character so make string
        }
        syntaxContext.ignore(" ");


        // Identifiers ---------
        syntaxContext.add("identifier_characters", new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{
                        SyntaxInstruction.MATCH_GROUP,
                        SyntaxInstruction.MATCH_GROUP},
                new Object[]{
                        "identifier_character", "identifier_characters"}));
        syntaxContext.add("identifier_characters", new SimpleSyntaxRule(0,
                new SyntaxInstruction[]{SyntaxInstruction.MATCH_GROUP},
                new Object[]{"identifier_character"}));

        syntaxContext.add("identifier", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_GROUP, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "identifier_characters", 0}));


        // BIDMAS ---------
        // Subtraction
        syntaxContext.add("expression_level_one", new SimpleSyntaxRule(3,
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
        syntaxContext.add("expression_level_two", new SimpleSyntaxRule(3,
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
        syntaxContext.add("expression_level_three", new SimpleSyntaxRule(3,
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
        syntaxContext.add("expression_level_four", new SimpleSyntaxRule(3,
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
        syntaxContext.add("expression_level_five", new SimpleSyntaxRule(3,
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
        syntaxContext.add("expression_level_six", new SimpleSyntaxRule(1,
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
                SyntaxRule.match(
                        new StringInputStream("<string>", "hello"),
                        "expression_level_one",
                        syntaxContext));

        assertEquals(new AstNode("+",
                        new AstNode("hello"),
                        new AstNode("world")),
                SyntaxRule.match(
                        new StringInputStream("<string>", "hello+world"),
                        "expression_level_one",
                        syntaxContext));

        assertEquals(new AstNode("+",
                        new AstNode("how"),
                        new AstNode("*",
                                new AstNode("are"),
                                new AstNode("you"))),
                SyntaxRule.match(
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
                SyntaxRule.match(
                        new StringInputStream("<string>", "((zero-b)+(b^two-four*a*c)^(one/two))/(two*a)"),
                        "expression_level_one",
                        syntaxContext));
    }
}