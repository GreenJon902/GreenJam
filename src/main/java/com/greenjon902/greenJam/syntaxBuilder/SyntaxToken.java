package com.greenjon902.greenJam.syntaxBuilder;

import java.util.Objects;

public class SyntaxToken {
    SyntaxTokenType type;
    Object storage;

    public SyntaxToken(SyntaxTokenType type, Object storage) {
        this.type = type;
        this.storage = storage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntaxToken that = (SyntaxToken) o;
        return type == that.type && Objects.equals(storage, that.storage);
    }

    @Override
    public String toString() {
        return "SyntaxToken{" +
                "type=" + type +
                ", storage=" + storage +
                '}';
    }
}
