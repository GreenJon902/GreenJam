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
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("Identifier{").append("\"").append(name).append("\"").append("}\n");
    }
}
