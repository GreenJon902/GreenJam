package com.greenjon902.greenJam.parser;

import java.util.Collections;
import java.util.List;

public class CodeBlock extends AbstractSyntaxTreeNode {
    public final List<AbstractSyntaxTreeNode> contents;

    public CodeBlock(List<AbstractSyntaxTreeNode> contents) {
        this.contents = Collections.unmodifiableList(contents);
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, int indent) {
        String stringIndent = "\t".repeat(indent);
        indent += 1;

        stringBuilder.append(stringIndent);
        stringBuilder.append("CodeBlock{\n");
        for (AbstractSyntaxTreeNode astNode : contents) {
            stringBuilder.append(stringIndent);
            astNode.prettyPrint(stringBuilder, indent);
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            stringBuilder.append(",\n");
        }

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append("\n"); // last comma

        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");
    }
}
