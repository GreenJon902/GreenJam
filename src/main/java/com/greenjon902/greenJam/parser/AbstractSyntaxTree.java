package com.greenjon902.greenJam.parser;

public class AbstractSyntaxTree {


    @Override
    public String toString() {
        return "AbstractSyntaxTree{" +
                "rootNode=" + rootNode +
                '}';
    }

    public String prettyPrint() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AbstractSyntaxTree{\n");

        String indent = "\t";
        rootNode.prettyPrint(stringBuilder, indent);

        stringBuilder.append("}");
        return stringBuilder.toString();
    }


    public final AbstractSyntaxTreeNode rootNode;

    public AbstractSyntaxTree(AbstractSyntaxTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
