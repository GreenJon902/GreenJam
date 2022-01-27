package com.greenjon902.greenJam.types.tokens;

import com.greenjon902.greenJam.types.tokenClassification.TokenType;

public class OperatorToken extends Token {
    private final String operatorType;

    public OperatorToken(String operatorType) {
        super();
        this.operatorType = operatorType;
    }

    @Override
    TokenType getType() {
        return TokenType.OPERATOR;
    }
}
