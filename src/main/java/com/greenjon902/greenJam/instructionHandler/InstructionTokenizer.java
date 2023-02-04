package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxOperator;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxToken;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxTokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InstructionTokenizer {
    public static InstructionToken[] tokenize(String string) {
        return tokenize(new StringInputStream("<string>", string));
    }

    public static InstructionToken[] tokenize(StringInputStream instruction) {
        List<InstructionToken> tokens = new ArrayList<>();

        InstructionToken.InstructionTokenType tokenType;
        Object tokenStorage;
        if ((tokenStorage = attemptGetKeyword(instruction)) != null) {
            tokenType = InstructionToken.InstructionTokenType.KEYWORD;
        } else {
            throw new RuntimeException("Failed to recognise next token at location - " + instruction.currentLocationString());
        }
        tokens.add(new InstructionToken(tokenType, tokenStorage));

        return tokens.toArray(InstructionToken[]::new);
    }

    private static InstructionKeyword attemptGetKeyword(StringInputStream instruction) {
        for (InstructionKeyword keyword : InstructionKeyword.lengthOrderedKeywords) {
            if (instruction.consumeIf(keyword.string)) {
                return keyword;
            }
        }
        return null;
    }
}