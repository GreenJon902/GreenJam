package com.greenjon902.greenJam.types;

public class IntegerToken extends Token {
    private final static TokenType type = TokenType.INTEGER;

    private int value;

    public IntegerToken(String value) {
        this(Integer.parseInt(value));
    }

    public IntegerToken(int value) {
        this.value = value;
    }

    @Override
    TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "IntegerToken{" +
                "value=" + value +
                '}';
    }
}
