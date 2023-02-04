package com.greenjon902.greenJam.instructionHandler;

import java.util.Objects;

public class InstructionToken {
    public final InstructionTokenType type;
    public final Object storage;

    public InstructionToken(InstructionTokenType type, Object storage) {
        this.type = type;
        this.storage = storage;
    }

    enum InstructionTokenType {
        KEYWORD
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructionToken that = (InstructionToken) o;
        return type == that.type && Objects.equals(storage, that.storage);
    }

    @Override
    public String toString() {
        return "InstructionToken{" +
                "type=" + type +
                ", storage=" + storage +
                '}';
    }
}
