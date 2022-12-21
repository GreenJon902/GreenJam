package com.greenjon902.greenJam.tokenizer;

import java.util.Objects;

public class Token {
    public final TokenType type;
    public final Object primaryStorage;

    public Token(TokenType type, Object primaryStorage) {
        this.type = type;
        this.primaryStorage = primaryStorage;
    }


    public boolean isOperator(OperatorType operatorType) {
        return type == TokenType.OPERATOR && primaryStorage == operatorType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", primaryStorage=\"" + primaryStorage + '\"' +
                '}';
    }

    public boolean isBracket(BracketType bracketType) {
        return type == TokenType.BRACKET && primaryStorage == bracketType;
    }
}
