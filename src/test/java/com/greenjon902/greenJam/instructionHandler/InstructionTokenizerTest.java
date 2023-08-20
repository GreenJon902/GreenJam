package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class InstructionTokenizerTest {

    @Test
    void tokenizeKeyword() {
        InstructionToken[] instructionTokens;

        instructionTokens = InstructionTokenizer.tokenize("SYNTAX;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.SYNTAX)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("RULE;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.RULE)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("ADD;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.ADD)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("REMOVE;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.REMOVE)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("ALL;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.ALL)
        }, instructionTokens);
    }

    @Test
    void tokenizeSyntaxRule() {
        InstructionToken[] instructionTokens;

        instructionTokens = InstructionTokenizer.tokenize("`test`;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.SYNTAX_RULE,
                        new SimpleSyntaxRule(0,
                                new SyntaxInstruction[]{SyntaxInstruction.MATCH_LITERAL},
                                new Object[]{"test"}))
        }, instructionTokens);
    }

    @Test
    void tokenizeString() {
        InstructionToken[] instructionTokens;

        instructionTokens = InstructionTokenizer.tokenize("\"test\";");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.STRING, "test"
                )}, instructionTokens);

        instructionTokens = InstructionTokenizer.tokenize("\"test.two\";");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.STRING, "test.two"
                )}, instructionTokens);

        instructionTokens = InstructionTokenizer.tokenize("\" \";");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.STRING, " "
                )}, instructionTokens);
    }

    @Test
    void tokenizeIdentifier() {
        InstructionToken[] instructionTokens;

        instructionTokens = InstructionTokenizer.tokenize("foo;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.IDENTIFIER, "foo")
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("bar_baz;");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.IDENTIFIER, "bar_baz")
        }, instructionTokens);
    }

    @Test
    void tokenizeWholeCommand() {
        InstructionToken[] instructionTokens;

        instructionTokens = InstructionTokenizer.tokenize("SYNTAX RULE ADD identifier `<[identifier_characters][identifier]>`;this is ignored");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.SYNTAX),
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.RULE),
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.ADD),
                new InstructionToken(InstructionToken.InstructionTokenType.IDENTIFIER, "identifier"),
                new InstructionToken(InstructionToken.InstructionTokenType.SYNTAX_RULE,
                        new SimpleSyntaxRule(1,
                                new SyntaxInstruction[]{
                                        SyntaxInstruction.START_RECORD,
                                        SyntaxInstruction.MATCH_GROUP,
                                        SyntaxInstruction.MATCH_GROUP,
                                        SyntaxInstruction.STOP_RECORD
                                },
                                new Object[]{
                                        0,
                                        "identifier_characters",
                                        "identifier",
                                        0
                                })),

        }, instructionTokens);
    }
}