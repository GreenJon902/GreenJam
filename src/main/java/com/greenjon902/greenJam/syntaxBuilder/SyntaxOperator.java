package com.greenjon902.greenJam.syntaxBuilder;

import java.util.Objects;

public class SyntaxOperator {
    public final SyntaxOperatorType type;
    public final Object storage;

    public SyntaxOperator(SyntaxOperatorType type, Object storage) {
        this.type = type;
        this.storage = storage;
    }

    public SyntaxOperator(SyntaxOperatorType type) {
        this(type, null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, storage);
    }

    enum SyntaxOperatorType {
        START_RECORD, STOP_RECORD, END, STRING_FORCER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntaxOperator that = (SyntaxOperator) o;
        return type == that.type && Objects.equals(storage, that.storage);
    }

    @Override
    public String toString() {
        return "SyntaxOperator{" +
                "type=" + type +
                ", storage=" + storage +
                '}';
    }
}
