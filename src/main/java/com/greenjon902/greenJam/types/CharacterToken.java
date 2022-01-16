package com.greenjon902.greenJam.types;

import com.greenjon902.greenJam.Logging;

public class CharacterToken extends Token {
    private final static TokenType type = TokenType.CHARACTER;

    private char value;

    public CharacterToken(String value) {
        this(value.charAt(0));
    }

    public CharacterToken(char value) {
        this.value = value;
    }

    @Override
    TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CharacterToken{" +
                "value=" + value +
                '}';
    }
}