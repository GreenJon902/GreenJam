package com.greenjon902.greenJam.parser;

import java.util.Collections;
import java.util.List;

public class CodeBlock extends AbstractSyntaxTreeNode {
    public final List<AbstractSyntaxTreeNode> contents;

    public CodeBlock(List<AbstractSyntaxTreeNode> contents) {
        this.contents = Collections.unmodifiableList(contents);
    }

    @Override
    public String toString() {
        return "CodeBlock{" +
                "contents=" + contents +
                '}';
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("CodeBlock{\n");
        boolean firstLoop = true;
        for (AbstractSyntaxTreeNode node : contents) {
            if (firstLoop) {
                firstLoop = false;
            } else {
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
                stringBuilder.append(",\n");
            }
            node.prettyPrint(stringBuilder, indent + "\t");
        }
        stringBuilder.append(indent).append("}\n");
    }
}
