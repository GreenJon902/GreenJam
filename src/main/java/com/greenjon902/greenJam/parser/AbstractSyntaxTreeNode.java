package com.greenjon902.greenJam.parser;

public abstract class AbstractSyntaxTreeNode {
    public abstract void prettyPrint(StringBuilder stringBuilder, int indent);
    public abstract String toString(); // Force all subclasses to have to implement this
}
