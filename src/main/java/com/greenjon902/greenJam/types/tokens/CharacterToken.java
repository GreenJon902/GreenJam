package com.greenjon902.greenJam.types.tokens;

import com.greenjon902.greenJam.types.tokenClassification.TokenType;

public class CharacterToken extends Token {
    private final char value;

    public CharacterToken(char value) {
        super();
        this.value = value;
    }

    @Override
    TokenType getType() {
        return TokenType.CHARACTER;
    }
}
