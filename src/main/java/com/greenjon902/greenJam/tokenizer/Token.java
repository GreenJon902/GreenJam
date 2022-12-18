package com.greenjon902.greenJam.tokenizer;

public class Token {
    public final TokenType type;
    public final String primaryStorage;

    private Token(TokenType type, String primaryStorage) {
        this.type = type;
        this.primaryStorage = primaryStorage;
    }

    /**
     * Create a command token with a name.
     */
    public static Token COMMAND(String commandName) {
        return new Token(TokenType.COMMAND, commandName);
    }

    /**
     * Create a string token with a name.
     */
    public static Token STRING_LITERAL(String stringContents) {
        return new Token(TokenType.STRING_LITERAL, stringContents);
    }

    public static Token LINE_END() {
        return new Token(TokenType.LINE_END, null);
    }

    public enum TokenType {
        COMMAND, STRING_LITERAL, LINE_END
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", primaryStorage=\"" + primaryStorage + '\"' +
                '}';
    }
}
