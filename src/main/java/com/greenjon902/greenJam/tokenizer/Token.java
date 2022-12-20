package com.greenjon902.greenJam.tokenizer;

public class Token {
    public final TokenType type;
    public final Object primaryStorage;

    public Token(TokenType type, Object primaryStorage) {
        this.type = type;
        this.primaryStorage = primaryStorage;
    }





    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", primaryStorage=\"" + primaryStorage + '\"' +
                '}';
    }
}
