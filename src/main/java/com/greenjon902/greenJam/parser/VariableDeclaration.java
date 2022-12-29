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

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("VariableDeclaration{\n");
        stringBuilder.append(indent).append("\ttype={\n");
        type.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\tidentifiers={\n");
        boolean firstLoop = true;
        for (Identifier identifier : identifiers) {
            if (firstLoop) {
                firstLoop = false;
            } else {
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
                stringBuilder.append(",\n");
            }
            identifier.prettyPrint(stringBuilder, indent + "\t\t");
        }
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("}\n");
    }
}
