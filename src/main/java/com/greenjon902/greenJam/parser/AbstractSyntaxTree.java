package com.greenjon902.greenJam.parser;

public class AbstractSyntaxTree {


    @Override
    public String toString() {
        return "AbstractSyntaxTree{" +
                "rootNode=" + rootNode +
                '}';
    }

    public final AbstractSyntaxTreeNode rootNode;

    public AbstractSyntaxTree(AbstractSyntaxTreeNode rootNode) {
        this.rootNode = rootNode;
    }
}
