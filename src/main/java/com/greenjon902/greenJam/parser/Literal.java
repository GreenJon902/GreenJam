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
}
