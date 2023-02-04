package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxBuilder;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxOperator;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxToken;
import com.greenjon902.greenJam.syntaxBuilder.SyntaxTokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class InstructionTokenizer {
    public static Set<Character> identifier_characters = new HashSet<>() {{
        //<editor-fold desc="Group Name Characters" defaultstate="collapsed">
        add('a');
        add('b');
        add('c');
        add('d');
        add('e');
        add('f');
        add('g');
        add('h');
        add('i');
        add('j');
        add('k');
        add('l');
        add('m');
        add('n');
        add('o');
        add('p');
        add('q');
        add('r');
        add('s');
        add('t');
        add('u');
        add('v');
        add('w');
        add('x');
        add('y');
        add('z');
        add('_');
        //</editor-fold>
    }};

    public static InstructionToken[] tokenize(String string) {
        return tokenize(new StringInputStream("<string>", string));
    }

    public static InstructionToken[] tokenize(StringInputStream instruction) {
        List<InstructionToken> tokens = new ArrayList<>();

        InstructionToken.InstructionTokenType tokenType;
        Object tokenStorage;
        if ((tokenStorage = attemptGetKeyword(instruction)) != null) {
            tokenType = InstructionToken.InstructionTokenType.KEYWORD;
        } else if ((tokenStorage = attemptGetSyntaxRule(instruction)) != null) {
            tokenType = InstructionToken.InstructionTokenType.SYNTAX_RULE;
        } else if ((tokenStorage = attemptGetIdentifier(instruction)) != null) {
            tokenType = InstructionToken.InstructionTokenType.IDENTIFIER;
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

    private static SyntaxRule attemptGetSyntaxRule(StringInputStream instruction) {
        if (instruction.consumeIf('`')) {
            return SyntaxBuilder.build(instruction);
        }
        return null;
    }

    private static String attemptGetIdentifier(StringInputStream instruction) {
        StringBuilder stringBuilder = new StringBuilder();
        while (!instruction.isEnd() && identifier_characters.contains(instruction.next())) {
            stringBuilder.append(instruction.consume());
        }
        if (stringBuilder.length() == 0) {
            return null;
        } else {
            return stringBuilder.toString();
        }
    }
}