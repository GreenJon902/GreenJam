package com.greenjon902.greenJam.parser.instructionHandler.syntaxBuilder;

import com.greenjon902.greenJam.StringInputStream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyntaxTokenizer {
    public final static char groupSubstitutionOpen = '{';
    public final static char groupSubstitutionClose = '}';
    public static Set<Character> groupSubstitutionCharacters = new HashSet<>() {{
            //<editor-fold desc="Identifier Characters" defaultstate="collapsed">
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
    public final static char startRecord = '<';
    public final static char stopRecord = '>';
    public static Set<Character> integerNumbers = new HashSet<>() {{
        //<editor-fold desc="Identifier Characters" defaultstate="collapsed">
        add('1');
        add('2');
        add('3');
        add('4');
        add('5');
        add('6');
        add('7');
        add('8');
        add('9');
        add('0');
        //</editor-fold>
    }};

    public static SyntaxToken[] tokenize(String string) {
        return tokenize(new StringInputStream(string));
    }

    public static SyntaxToken[] tokenize(StringInputStream syntax) {
        List<SyntaxToken> tokens = new ArrayList<>();

        while (!syntax.isEnd()) {
            SyntaxTokenType tokenType;
            Object tokenStorage;
            if ((tokenStorage = attemptGetGroupSubstitution(syntax)) != null) {
                tokenType = SyntaxTokenType.GROUP_SUBSTITUTION;
            } else if ((tokenStorage = attemptGetOperator(syntax)) != null) {
                tokenType = SyntaxTokenType.OPERATOR;
            } else if ((tokenStorage = attemptGetLiteral(syntax)) != null) {
                tokenType = SyntaxTokenType.LITERAL;
            } else {
                throw new RuntimeException("Failed to recognise next token at location - " + syntax.currentLocationString());
            }
            tokens.add(new SyntaxToken(tokenType, tokenStorage));
        }

        return tokens.toArray(SyntaxToken[]::new);
    }

    private static String attemptGetGroupSubstitution(StringInputStream syntax) {
        if (syntax.consumeIf(groupSubstitutionOpen)) {
            StringBuilder name = new StringBuilder();

            while (!syntax.isEnd()) {
                if (syntax.consumeIf(groupSubstitutionClose)) {
                    break;
                }
                if (groupSubstitutionCharacters.contains(syntax.string.charAt(syntax.location))) {
                    name.append(syntax.consume());
                }
            }
            return name.toString();
        }
        return null;
    }

    private static SyntaxOperator attemptGetOperator(StringInputStream syntax) {
        if (syntax.consumeIf(startRecord)) {
            StringBuilder storageLocation = new StringBuilder();
            while (!syntax.isEnd() && integerNumbers.contains(syntax.string.charAt(syntax.location))) {
                storageLocation.append(syntax.consume());
            }
            if (storageLocation.length() == 0) {
                storageLocation.append("0");
            }

            return new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.START_RECORD, Integer.valueOf(storageLocation.toString()));


        } else if (syntax.consumeIf(stopRecord)) {
            StringBuilder storageLocation = new StringBuilder();
            while (!syntax.isEnd() && integerNumbers.contains(syntax.string.charAt(syntax.location))) {
                storageLocation.append(syntax.consume());
            }
            if (storageLocation.length() == 0) {
                storageLocation.append("0");
            }

            return new SyntaxOperator(SyntaxOperator.SyntaxOperatorType.STOP_RECORD, Integer.valueOf(storageLocation.toString()));
        }

        return null;

    }

    private static String attemptGetLiteral(StringInputStream syntax) {
        StringBuilder literal = new StringBuilder();

        while (!syntax.isEnd()) {
            literal.append(syntax.consume());
        }

        if (literal.length() == 0) {
            return null;
        }

        return literal.toString();
    }
}
