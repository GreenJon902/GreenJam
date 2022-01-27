package com.greenjon902.greenJam.types.tokens;

import com.greenjon902.greenJam.types.tokenClassification.TokenType;

public abstract class Token {
    public abstract TokenType getType();
    public abstract String toString();
}
