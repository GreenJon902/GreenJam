package com.greenjon902.greenJam.instructionHandler;

import java.util.Objects;

public class InstructionToken {
    public final InstructionTokenType type;
    public final Object storage;

    public InstructionToken(InstructionTokenType type, Object storage) {
        this.type = type;
        this.storage = storage;
    }

    public static InstructionToken makeArgument(InstructionTokenType type) {
        return new InstructionToken(InstructionTokenType.__ARGUMENT__, type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, storage);
    }

    public enum InstructionTokenType {
        SYNTAX_RULE, IDENTIFIER, STRING, KEYWORD, BOOLEAN, __ARGUMENT__ // __ARGUMENT__ is used internally for parsing of commands
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
