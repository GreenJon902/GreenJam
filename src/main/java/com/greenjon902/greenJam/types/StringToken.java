package com.greenjon902.greenJam.types;

public class StringToken extends Token {
    private final static TokenType type = TokenType.STRING;

    private String value;


    public StringToken(String value) {
        this.value = value;
    }

    @Override
    TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "StringToken{" +
                "value=" + value +
                '}';
    }
}