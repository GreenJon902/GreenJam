package com.greenjon902.greenJam.parser;

public class Identifier extends AbstractSyntaxTreeNode {
    public final String name;
    public Identifier(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, int indent) {
        String stringIndent = "\t".repeat(indent);

        stringBuilder.append(stringIndent);
        stringBuilder.append("Identifier{\"");
        stringBuilder.append(name);
        stringBuilder.append("\"}\n");
    }
}
