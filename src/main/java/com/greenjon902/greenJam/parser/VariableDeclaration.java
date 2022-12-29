package com.greenjon902.greenJam.parser;

import java.util.Arrays;

public class VariableDeclaration extends AbstractSyntaxTreeNode {
    public final Identifier type;
    public final Identifier[] identifiers;

    public VariableDeclaration(Identifier type, Identifier[] identifiers) {
        this.type = type;
        this.identifiers = identifiers;
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "type=" + type +
                ", identifiers=" + Arrays.toString(identifiers) +
                '}';
    }
}
