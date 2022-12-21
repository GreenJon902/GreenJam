package com.greenjon902.greenJam.parser;

public class Literal extends AbstractSyntaxTreeNode {
    public final String value;
    public Literal(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, int indent) {
        String stringIndent = "\t".repeat(indent);

        stringBuilder.append(stringIndent);
        stringBuilder.append("Literal{\"");
        stringBuilder.append(value);
        stringBuilder.append("\"}\n");
    }
}
