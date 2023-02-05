package com.greenjon902.greenJam.instructionHandler.handlers;

import com.greenjon902.greenJam.common.InstructionTokenInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.instructionHandler.InstructionKeyword;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstInstructionHandlerTest {
    @Test
    public void syntaxRuleInstructionHandling() {
        SyntaxContext syntaxContext = new SyntaxContext();
        FirstInstructionHandler firstInstructionHandler = new FirstInstructionHandler(syntaxContext);

        firstInstructionHandler.handle(new InstructionTokenInputStream(
                "TestInstruction", 5, new InstructionToken[]    {
                        new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.SYNTAX),
                        new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.RULE),
                        new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.ADD),
                        new InstructionToken(InstructionToken.InstructionTokenType.IDENTIFIER, "foo"),
                        new InstructionToken(InstructionToken.InstructionTokenType.SYNTAX_RULE, new SyntaxRule(0,
                                new SyntaxInstruction[]{
                                        SyntaxInstruction.MATCH_LITERAL
                                },
                                new Object[]{
                                        "bar"
                                }))
            }
        ));
        assertEquals(1, syntaxContext.groupAmount());
        assertArrayEquals(new String[]{"foo"}, syntaxContext.getGroupNames());
        assertArrayEquals(new SyntaxRule[]{
                new SyntaxRule(0,
                        new SyntaxInstruction[]{
                                SyntaxInstruction.MATCH_LITERAL
                        },
                        new Object[]{
                                "bar"
                        })
        }, syntaxContext.getRules("foo"));

        firstInstructionHandler.handle(new InstructionTokenInputStream(
                "TestInstruction", 5, new InstructionToken[]    {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.SYNTAX),
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.RULE),
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.REMOVE),
                new InstructionToken(InstructionToken.InstructionTokenType.IDENTIFIER, "foo"),
                new InstructionToken(InstructionToken.InstructionTokenType.SYNTAX_RULE, new SyntaxRule(0,
                        new SyntaxInstruction[]{
                                SyntaxInstruction.MATCH_LITERAL
                        },
                        new Object[]{
                                "bar"
                        }))
        }
        ));
        assertEquals(0, syntaxContext.groupAmount()); // No more items in group so shouldn't exist
    }
}