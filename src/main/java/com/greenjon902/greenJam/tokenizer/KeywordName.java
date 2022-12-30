package com.greenjon902.greenJam.tokenizer;

public enum KeywordName {
    RETURN("return", KeywordType.STATEMENT),
    INT("int", KeywordType.PRIMITIVE), VOID("void", KeywordType.PRIMITIVE);

    public final String name;
    public final KeywordType type;

    KeywordName(String name, KeywordType type) {
        this.name = name;
        this.type = type;
    }

    public enum KeywordType {
        STATEMENT, PRIMITIVE

    }
}
