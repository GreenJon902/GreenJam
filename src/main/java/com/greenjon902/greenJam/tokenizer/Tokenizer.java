package com.greenjon902.greenJam.tokenizer;

import java.util.ArrayList;
import java.util.HashSet;

public class Tokenizer {
    public static String command_delclarer = "::";
    public static String string_opener = "\"";
    public static String string_closer = "\"";
    public static String comment_declarer = "//";
    public static String block_comment_opener = "/**";
    public static String block_comment_closer = "*/";
    public static String line_ender = ";";
    public static String comma = ",";
    public static char positive_sign = '+';
    public static char negative_sign = '-';
    public static char decimal_point_character = '.';
    public static HashSet<Character> identifier_characters = new HashSet<>() {{
        //<editor-fold desc="Identifier Characters" defaultstate="collapsed">
        add('a'); add('A');
        add('b'); add('B');
        add('c'); add('C');
        add('d'); add('D');
        add('e'); add('E');
        add('f'); add('F');
        add('g'); add('G');
        add('h'); add('H');
        add('i'); add('I');
        add('j'); add('J');
        add('k'); add('K');
        add('l'); add('L');
        add('m'); add('M');
        add('n'); add('N');
        add('o'); add('O');
        add('p'); add('P');
        add('q'); add('Q');
        add('r'); add('R');
        add('s'); add('S');
        add('t'); add('T');
        add('u'); add('U');
        add('v'); add('V');
        add('w'); add('W');
        add('x'); add('X');
        add('y'); add('Y');
        add('z'); add('Z');
        add('0'); add('1'); add('2'); add('3'); add('4'); add('5'); add('6'); add('7'); add('8'); add('9');
        add('_');
        //</editor-fold>
    }};
    public static HashSet<Character> whitespace_characters = new HashSet<>() {{
        add(' ');
        add('\t');
        add('\n');
    }};

    private int location;
    private String string;

    public Tokenizer(String string) {
        this.location = 0;
        this.string = string;
    }

    public Token[] getTokens() {
        ArrayList<Token> tokens = new ArrayList<>();

        while (location < string.length()) {
            Token newToken = getNextToken();
            if (newToken != null) {
                tokens.add(newToken);
            }
        }

        return tokens.toArray(Token[]::new);
    }


    private Token getNextToken() {
        skipWhitespace();

        if (matchToString(comment_declarer, 0)) {
            String newline = System.getProperty("line.separator");;
            while (location < string.length() && !matchToString(newline, 0)) {
                location += 1;
            }
            location += newline.length();
            return null;

        } else if (matchToString(block_comment_opener, 0)) {
            location += block_comment_opener.length();
            while (location < string.length() && !matchToString(block_comment_closer, 0)) {
                location += 1;
            }
            location += block_comment_closer.length();
            return null;
        }

        TokenType tokenType;
        Object token_info;

        if ((token_info = attemptGetCommand()) != null) {
            tokenType = TokenType.COMMAND;
        } else if ((token_info = attemptGetStringLiteral()) != null) {
            tokenType = TokenType.LITERAL;
        } else if ((token_info = attemptGetNumericLiteral()) != null) {
            tokenType = TokenType.LITERAL;
        } else if (attemptGetComma() != null) {
            tokenType = TokenType.COMMA;
        } else if ((token_info = attemptGetBracket()) != null) {
            tokenType = TokenType.BRACKET;
        } else if ((token_info = attemptGetOperator()) != null) {
            tokenType = TokenType.OPERATOR;
        } else if ((token_info = attemptGetIdentifier()) != null) {
            tokenType = TokenType.IDENTIFIER;
        } else if (attemptGetLineEnd() != null) { // We don't need to keep the line end character
            tokenType = TokenType.LINE_END;

        } else {
            throw new RuntimeException("Failed to recognise next token at location - " + location + " \"" + string.charAt(location) + "\"");
        }

        return new Token(tokenType, token_info);
    }


    /**
     * Checks of the next token is an operator. If the next value is not an operator then null is returned.
     * @return The operator or null.
     */
    private OperatorType attemptGetOperator() {
        for (OperatorType operatorType : OperatorType.values()) {
             if (string.regionMatches(location, operatorType.symbol, 0, operatorType.symbol.length())) {
                 location += operatorType.symbol.length();
                 return operatorType;
             }
        }

        return null;
    }

    /**
     * Checks of the next token is a comma. If the next value is not a comma then null is returned.
     * @return The comma or null.
     */
    private String attemptGetComma() {
        int offset = 0;

        if (matchToString(comma, offset)) {
            offset += comma.length();

            location += offset;
            return comma;
        }

        return null;
    }


    /**
     * Checks of the next token is an operator. If the next value is not an operator then null is returned.
     * @return The operator or null.
     */
    private BracketType attemptGetBracket() {
        for (BracketType bracketType : BracketType.values()) {
            if (string.regionMatches(location, bracketType.symbol, 0, bracketType.symbol.length())) {
                location += bracketType.symbol.length();
                return bracketType;
            }
        }

        return null;
    }


    /**
     * Checks of the next token is a numeric literal . If the next value is not a numeric literal then null is returned.
     * @return The numeric literal as string or null.
     */
    private String attemptGetNumericLiteral() {
        int offset = 0;

        StringBuilder number = new StringBuilder();

        if (string.charAt(location) == positive_sign) {
            number.append(positive_sign);
            offset += 1;
        } else if (string.charAt(location) == negative_sign) {
            number.append(negative_sign);
            offset += 1;
        }

        boolean had_decimal_point = false;
        while (true) {
            char character = string.charAt(location + offset);
            if (Character.isDigit(character)) {
                number.append(character);

            } else if ((!had_decimal_point) && (character == decimal_point_character)) {
                had_decimal_point = true;
                number.append(character);

            } else {
                break;
            }

            offset += 1;
        }

        String fullNumber = number.toString();
        if (fullNumber.isBlank() ||  // Empty or only character is . or +-
                (fullNumber.length() == 1 && !Character.isDigit(fullNumber.charAt(0)))) {
            return null;
        }

        location += offset;
        return fullNumber;
    }


    /**
     * Checks of the next token is a string literal. If the next value is not a string literal then null is returned.
     * @return The string literal or null.
     */
    private String  attemptGetStringLiteral() {
        int offset = 0;

        if (matchToString(string_opener, offset)) {
            offset += string_opener.length();

            StringBuilder string_contents = new StringBuilder();
            while (!matchToString(string_closer, offset)) { // get characters until string closer
                string_contents.append(string.charAt(location + offset));
                offset += 1;
            }
            offset += string_closer.length(); // It was the end condition for loop but wasn't added.

            location += offset;
            return string_contents.toString();
        }

        return null;
    }

    /**
     * Checks of the next token is an identifier. If the next value is not an identifier then null is returned.
     * @return The identifier or null.
     */
    private String attemptGetIdentifier() {
        int offset = 0;

        StringBuilder identifier = new StringBuilder();
        while (identifier_characters.contains(string.charAt(location + offset))) {
            identifier.append(string.charAt(location + offset));
            offset += 1;
        }

        if (offset > 0) {
            location += offset;
            return identifier.toString();
        }
        return null;
    }

    /**
     * Checks of the next token is a command. If the next value is not a command then null is returned.
     * @return The command name or null.
     */
    private CommandType attemptGetCommand() {
        int offset = 0;

        if (matchToString(command_delclarer, offset)) {
            offset += command_delclarer.length();

            offset = skipWhitespace(offset);

            StringBuilder command_name = new StringBuilder();
            while (!whitespace_characters.contains(string.charAt(location + offset))) { // get entire name
                command_name.append(string.charAt(location + offset));
                offset += 1;
            }
            location += offset;

            String command_name_ = command_name.toString();

            for (CommandType command : CommandType.values()) {
                if (command_name_.equals(command.name)) {
                    return command;
                }
            }
        }

        return null;
    }

    /**
     * Checks of the next token is a line end. If the next value is not a line end then null is returned.
     * @return The line end character or null.
     */
    private String attemptGetLineEnd() {
        int offset = 0;

        if (matchToString(line_ender, offset)) {
            offset += line_ender.length();

            location += offset;
            return line_ender;
        }

        return null;
    }

    /**
     * Move the offset that was given past any whitespace.
     *
     * @return The new offset
     */
    private int skipWhitespace(int current_offset) {
        while (whitespace_characters.contains(string.charAt(location + current_offset))) {
            current_offset += 1;
        }
        return current_offset;
    }

    /**
     * Skip any whitespace and set the location to that new location
     */
    private void skipWhitespace() {
        location += skipWhitespace(0);
    }

    /**
     * Checks whether the characters at the location + the offset are equal to a string.
     *
     * @param to_check The string that was want to test against
     * @param offset The current offset from location
     * @return Whether it would work or not
     */
    private boolean matchToString(String to_check, int offset) {
        for (int i = 0; i < to_check.length(); i++) {
            if (string.charAt(location + offset + i) != to_check.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
