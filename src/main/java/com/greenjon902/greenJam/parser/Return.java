package com.greenjon902.greenJam.parser;

public class Return extends AbstractSyntaxTreeNode {
    public final AbstractSyntaxTreeNode object;

    public Return(AbstractSyntaxTreeNode object) {
        this.object = object;
    }

    public Return() {
        this(null);
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        if (object == null) {
            stringBuilder.append(indent).append("Return{}").append("\n");
        } else {
            stringBuilder.append(indent).append("Return{").append("\n");
            object.prettyPrint(stringBuilder, indent + "\t");
            stringBuilder.append(indent).append("}\n");
        }
    }

    @Override
    public String toString() {
        return "Return{" +
                "object=" + object +
                '}';
    }
}
