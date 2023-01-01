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

    public boolean isKeyword(KeywordName keywordName) {
        return type == TokenType.KEYWORD && primaryStorage == keywordName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(primaryStorage, token.primaryStorage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, primaryStorage);
    }
}
