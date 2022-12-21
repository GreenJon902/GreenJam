package com.greenjon902.greenJam.parser;

public class AbstractSyntaxTree {


    @Override
    public String toString() {
        return "AbstractSyntaxTree{" +
                "rootNode=" + rootNode +
                '}';
    }

    public void prettyPrint() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AbstractSyntaxTree{\n");

        int indent = 1;
        rootNode.prettyPrint(stringBuilder, indent);

        stringBuilder.append("}");
        System.out.println(stringBuilder);
    }


    public final AbstractSyntaxTreeNode rootNode;

    public AbstractSyntaxTree(AbstractSyntaxTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}