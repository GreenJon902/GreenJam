package com.greenjon902.greenJam.types.tokens;

import com.greenjon902.greenJam.types.tokenClassification.TokenType;

public class OperatorToken extends Token {
    private final String operatorType;
    private final String operator;

    public OperatorToken(String operatorType, String operator) {
        super();
        this.operatorType = operatorType;
        this.operator = operator;
    }

    @Override
    TokenType getType() {
        return TokenType.OPERATOR;
    }
}
