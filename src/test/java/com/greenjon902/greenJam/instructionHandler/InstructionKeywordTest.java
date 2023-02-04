package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.syntaxBuilder.SyntaxOperator;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxToken;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxTokenType;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxTokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionKeywordTest {

    @Test
    void tokenizeKeyword() {
        InstructionToken[] instructionTokens;

        instructionTokens = InstructionTokenizer.tokenize("SYNTAX");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.SYNTAX)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("RULE");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.RULE)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("ADD");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.ADD)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("REMOVE");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.REMOVE)
        }, instructionTokens);
        instructionTokens = InstructionTokenizer.tokenize("ALL");
        assertArrayEquals(new InstructionToken[] {
                new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, InstructionKeyword.ALL)
        }, instructionTokens);
    }
}