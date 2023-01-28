package com.greenjon902.greenJam.syntaxBuilder;

import java.util.Objects;

public class SyntaxOperator {
    SyntaxOperatorType type;
    Object storage;

    public SyntaxOperator(SyntaxOperatorType type, Object storage) {
        this.type = type;
        this.storage = storage;
    }

    enum SyntaxOperatorType {
        START_RECORD, STOP_RECORD;
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
