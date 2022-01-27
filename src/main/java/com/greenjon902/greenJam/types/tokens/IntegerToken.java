package com.greenjon902.greenJam.types.tokens;

import com.greenjon902.greenJam.types.tokenClassification.TokenType;

public class IntegerToken extends Token {
    private final int value;

    public IntegerToken(int value) {
        super();
        this.value = value;
    }

    @Override
    public TokenType getType() {
        return TokenType.INTEGER;
    }

    @Override
    public String toString() {
        return "IntegerToken{" +
                "value=" + value +
                '}';
    }
}
