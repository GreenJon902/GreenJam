package com.greenjon902.greenJam.types;

public class FloatToken extends Token {
    private final static TokenType type = TokenType.FLOAT;

    private float value;

    public FloatToken(String value) {
        this(Float.parseFloat(value));
    }

    public FloatToken(float value) {
        this.value = value;
    }

    @Override
    TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "FloatToken{" +
                "value=" + value +
                '}';
    }
}
