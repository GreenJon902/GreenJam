package com.greenjon902.greenJam.tokenizer;

import java.util.ArrayList;

public class Tokenizer {
    public static String command_delclarer = "::";
    public static String string_opener = "\"";
    public static String string_closer = "\"";
    public static String line_ender = ";";
    public static ArrayList<Character> whitespace_characters = new ArrayList<>() {{
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
            tokens.add(getNextToken());
        }

        return tokens.toArray(Token[]::new);
    }


    private Token getNextToken() {
        skipWhitespace();

        Token token;
        token = attemptGetCommand();
        if (token != null) {
            return token;
        }

        token = attemptGetStringLiteral();
        if (token != null) {
            return token;
        }

        token = attemptGetLineEnd();
        if (token != null) {
            return token;
        }

        throw new RuntimeException("Failed to recognise next token at location - " + location);
    }


    private Token attemptGetCommand() {
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
            return Token.COMMAND(command_name.toString());
        }

        return null;
    }

    private Token attemptGetStringLiteral() {
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
            return Token.STRING_LITERAL(string_contents.toString());
        }

        return null;
    }

    private Token attemptGetLineEnd() {
        int offset = 0;

        if (matchToString(line_ender, offset)) {
            offset += line_ender.length();

            location += offset;
            return Token.LINE_END();
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
