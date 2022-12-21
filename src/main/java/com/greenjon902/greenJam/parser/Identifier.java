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
}
