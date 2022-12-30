package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.tokenizer.KeywordName;

public class Primitive extends AbstractSyntaxTreeNode {
    public final KeywordName name;

    public Primitive(KeywordName name) {
        this.name = name;
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("Primitive{").append(name).append("}\n");
    }

    @Override
    public String toString() {
        return "Primitive{" +
                "name=" + name +
                '}';
    }
}
