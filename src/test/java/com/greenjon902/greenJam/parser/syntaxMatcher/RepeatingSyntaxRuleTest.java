package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.parser.syntaxMatcher.ParserTestResources.alphaNumericCharacterList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepeatingSyntaxRuleTest {
    @Test
    void matchRepeatingCharacter() {
        SyntaxContext syntaxContext = new SyntaxContext();

        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("character", new SimpleSyntaxRule(0,
                    new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                    new Object[]{String.valueOf(character)})); // The actual program doesn't know difference between string and character so make string
        }
        syntaxContext.add("characters", new RepeatingSyntaxRule("character", false));
        SyntaxRule recorder = new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_GROUP, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "characters", 0});

        assertEquals(new AstNode("HelloWorld"), recorder.match("HelloWorld", syntaxContext));
    }

    @Test
    void matchRepeatingComplexGroup() {
        SyntaxContext syntaxContext = new SyntaxContext();

        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("item", new SimpleSyntaxRule(0,
                    new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                    new Object[]{String.valueOf(character)})); // The actual program doesn't know difference between string and character so make string
        }
        syntaxContext.add("list_item", new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{
                        SyntaxInstruction.START_RECORD,
                        SyntaxInstruction.MATCH_GROUP,
                        SyntaxInstruction.STOP_RECORD,
                        SyntaxInstruction.MATCH_LITERAL},
                new Object[]{0, "item", 0, ", "}));
        SyntaxRule rule = new RepeatingSyntaxRule("list_item", true);
        syntaxContext.add("list", rule);

        assertEquals(new AstNode(), rule.match("", syntaxContext));
        assertEquals(new AstNode(new AstNode("a")), rule.match("a, ", syntaxContext));
        assertEquals(new AstNode(new AstNode("h"), new AstNode("e"),
                new AstNode("l"), new AstNode("l"), new AstNode("o")),
                rule.match("h, e, l, l, o, ", syntaxContext));

    }

    @Test
    void matchWithLimit() {
        SyntaxContext syntaxContext = new SyntaxContext();

        for (char character : alphaNumericCharacterList) {
            syntaxContext.add("character", new SimpleSyntaxRule(0,
                    new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                    new Object[]{String.valueOf(character)})); // The actual program doesn't know difference between string and character so make string
        }
        syntaxContext.add("characters", new RepeatingSyntaxRule("character", 5, false));
        SyntaxRule recorder = new SimpleSyntaxRule(1,
                new SyntaxInstruction[]{SyntaxInstruction.START_RECORD, SyntaxInstruction.MATCH_GROUP, SyntaxInstruction.STOP_RECORD},
                new Object[]{0, "characters", 0});

        assertEquals(new AstNode("Hi"), recorder.match("Hi", syntaxContext));
        assertEquals(new AstNode("Hello"), recorder.match("HelloWorld", syntaxContext));
    }
}
