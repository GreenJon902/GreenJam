package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.*;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.common.SyntaxInstruction.*;
import static com.greenjon902.greenJam.parser.syntaxMatcher.ParserTestResources.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionSyntaxMatcherTest {
    @Test
    void matchExpression() {
        SyntaxContext syntaxContext = new SyntaxContext();


        // Identifiers ---------
        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("operand", new SimpleSyntaxRule(1,
                    new SyntaxInstruction[]{
                            SyntaxInstruction.START_RECORD,
                            MATCH_LITERAL,
                            SyntaxInstruction.STOP_RECORD},
                    new Object[]{0, String.valueOf(character), 0})); // The actual program doesn't know difference between string and character so make string
        }


        // Operators ---------
        syntaxContext.add("operator", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{START_RECORD, MATCH_LITERAL, STOP_RECORD},
                new Object[]{0, "^", 0}));
        syntaxContext.add("operator", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{START_RECORD, MATCH_LITERAL, STOP_RECORD},
                new Object[]{0, "/", 0}));
        syntaxContext.add("operator", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{START_RECORD, MATCH_LITERAL, STOP_RECORD},
                new Object[]{0, "*", 0}));
        syntaxContext.add("operator", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{START_RECORD, MATCH_LITERAL, STOP_RECORD},
                new Object[]{0, "+", 0}));
        syntaxContext.add("operator", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{START_RECORD, MATCH_LITERAL, STOP_RECORD},
                new Object[]{0, "-", 0}));

        // Expression ---------
        syntaxContext.add("operand", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{
                        MATCH_LITERAL,
                        SyntaxInstruction.RECORD_GROUP,
                        MATCH_LITERAL},
                new Object[]{
                        "(",
                        new Tuple.Two<>(0, "expression"),
                        ")"
                }));
        syntaxContext.add("expression", new ExpressionSyntaxRule("operand", "operator", false, true));

        // Testing ---------
        assertEquals(
                quadraticFormulaNodeVer2,
                SyntaxRule.match(
                        new StringInputStream("<string>", quadraticFormulaString),
                        "expression",
                        syntaxContext));
    }
}
