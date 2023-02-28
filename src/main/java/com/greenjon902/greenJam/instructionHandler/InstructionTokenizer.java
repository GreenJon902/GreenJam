package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.*;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class InstructionTokenizer {
    public final static char instruction_end_character = ';';
    public static HashSet<Character> whitespace_characters = new HashSet<>() {{
        add(' ');
        add('\t');
        add('\n');
    }};

    public static InstructionToken[] tokenize(String string) {
        return tokenize(new StringInputStream("<string>", string));
    }

    public static InstructionToken[] tokenize(StringInputStream instruction) {
        List<InstructionToken> tokens = new ArrayList<>();

        boolean firstLoop = true;
        while (!instruction.isEnd() && !instruction.consumeIf(instruction_end_character)) {
            if (!firstLoop) {
                skipButRequireWhitespace(instruction);
            } else {
                skipWhitespace(instruction); // Optional whitespace
                firstLoop = false;
            }

            InstructionToken.InstructionTokenType tokenType;
            Object tokenStorage;
            if ((tokenStorage = attemptGetKeyword(instruction)) != null) {
                tokenType = InstructionToken.InstructionTokenType.KEYWORD;
            } else if ((tokenStorage = attemptGetSyntaxRule(instruction)) != null) {
                tokenType = InstructionToken.InstructionTokenType.SYNTAX_RULE;
            } else if ((tokenStorage = attemptGetIdentifier(instruction)) != null) {
                tokenType = InstructionToken.InstructionTokenType.IDENTIFIER;
            } else if ((tokenStorage = attemptGetString(instruction)) != null) {
                tokenType = InstructionToken.InstructionTokenType.STRING;
            } else {
                Errors.instructionTokenizer_unrecognisedToken(instruction);
                tokenType = null; // It will never get here
            }
            tokens.add(new InstructionToken(tokenType, tokenStorage));
        }

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

    private static SyntaxRule attemptGetSyntaxRule(StringInputStream instruction) {
        if (instruction.consumeIf('`')) {
            return SyntaxBuilder.build(instruction);
        }
        return null;
    }

    private static String attemptGetString(StringInputStream instruction) {
        if (instruction.consumeIf('\"')) {
            StringBuilder stringBuilder = new StringBuilder();

            while (true) {
                char character = instruction.consume();
                if (character == '\"') {
                    break;
                } else if (character == '\\') {
                    character = EscapeCharacterUtil.getEscapeCharacter(instruction);
                }
                stringBuilder.append(character);
            }
            return stringBuilder.toString();
        }
        return null;
    }

    private static String attemptGetIdentifier(StringInputStream instruction) {
        StringBuilder stringBuilder = new StringBuilder();
        while (!instruction.isEnd() && CharacterLists.identifierCharacters.contains(instruction.next())) {
            stringBuilder.append(instruction.consume());
        }
        if (stringBuilder.length() == 0) {
            return null;
        } else {
            return stringBuilder.toString();
        }
    }

    public static void skipButRequireWhitespace(StringInputStream instruction) {
        if (!whitespace_characters.contains(instruction.next())) throw new RuntimeException();

        skipWhitespace(instruction);
    }

    public static void skipWhitespace(StringInputStream instruction) {
        while (whitespace_characters.contains(instruction.next())){
            instruction.consume();
        }
    }
}