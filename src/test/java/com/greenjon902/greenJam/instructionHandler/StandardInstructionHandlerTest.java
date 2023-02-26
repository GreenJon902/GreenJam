package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.parser.syntaxMatcher.LinkSyntaxRule;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.instructionHandler.InstructionHandlerUtils.*;
import static com.greenjon902.greenJam.instructionHandler.InstructionToken.InstructionTokenType.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class StandardInstructionHandlerTest {
    @Test
    void modifySimpleRules() {
        SimpleSyntaxRule rule = new SimpleSyntaxRule(0, new SyntaxInstruction[0], new Object[0]);
        String group = "group";
        InstructionToken ruleInstruction = new InstructionToken(SYNTAX_RULE, rule);
        InstructionToken groupInstruction = new InstructionToken(IDENTIFIER, group);

        SyntaxContext syntaxContext = new SyntaxContext();
        StandardInstructionHandler standardInstructionHandler = new StandardInstructionHandler(syntaxContext);

        assertEquals(0, standardInstructionHandler.handle(SYNTAX, RULE, ADD, groupInstruction, ruleInstruction));
        assertArrayEquals(new SyntaxRule[] {rule}, syntaxContext.getRules(group));
        assertEquals(0, standardInstructionHandler.handle(SYNTAX, RULE, REMOVE, groupInstruction, ruleInstruction));
        assertArrayEquals(new SyntaxRule[] {}, syntaxContext.getRules(group));
    }

    @Test
    void modifyLinkRules() {
        String group1 = "group1";
        String group2 = "group2";
        InstructionToken group1Instruction = new InstructionToken(IDENTIFIER, group1);
        InstructionToken group2Instruction = new InstructionToken(IDENTIFIER, group2);
        LinkSyntaxRule group2Rule = new LinkSyntaxRule(group2);

        SyntaxContext syntaxContext = new SyntaxContext();
        StandardInstructionHandler standardInstructionHandler = new StandardInstructionHandler(syntaxContext);

        assertEquals(0, standardInstructionHandler.handle(SYNTAX, RULE, ADD, LINK, group1Instruction, group2Instruction));
        assertArrayEquals(new SyntaxRule[] {group2Rule}, syntaxContext.getRules(group1));
        assertEquals(0, standardInstructionHandler.handle(SYNTAX, RULE, REMOVE, LINK, group1Instruction, group2Instruction));
        assertArrayEquals(new SyntaxRule[] {}, syntaxContext.getRules(group1));
    }

    @Test
    void modifyIgnored() {
        String string = "test";
        InstructionToken stringInstruction = new InstructionToken(STRING, string);

        SyntaxContext syntaxContext = new SyntaxContext();
        StandardInstructionHandler standardInstructionHandler = new StandardInstructionHandler(syntaxContext);

        assertEquals(0, standardInstructionHandler.handle(SYNTAX, IGNORED, ADD, stringInstruction));
        assertArrayEquals(new String[] {"test"}, syntaxContext.getIgnored());
        assertEquals(0, standardInstructionHandler.handle(SYNTAX, IGNORED, REMOVE, stringInstruction));
        assertArrayEquals(new String[] {}, syntaxContext.getIgnored());
    }
}
