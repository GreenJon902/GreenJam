package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.*;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.parser.syntaxMatcher.ParserTestResources.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MixedSyntaxMatcherTests {
    @Test
    void matchMixedExpressions1() {
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
    void matchMixedExpressions2() { // Matching of quadratic formula before we have ExpressionSyntaxParser
        SyntaxContext syntaxContext = new SyntaxContext();

        // Operands ---------
        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("operand", new SimpleSyntaxRule(1,
                    new SyntaxInstruction[]{
                            SyntaxInstruction.START_RECORD,
                            SyntaxInstruction.MATCH_LITERAL,
                            SyntaxInstruction.STOP_RECORD},
                    new Object[]{0, String.valueOf(character), 0})); // The actual program doesn't know difference between string and character so make string
        }


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
        syntaxContext.addLink("expression_level_six", "operand");


        // Testing ---------
        assertEquals(
                quadraticFormulaNodeVer1,
                SyntaxRule.match(
                        new StringInputStream("<string>", quadraticFormulaString),
                        "expression_level_one",
                        syntaxContext));
    }
}
