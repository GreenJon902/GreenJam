package com.greenjon902.greenJam.parser;

public abstract class AbstractSyntaxTreeNode {
    /**
     * Print out the entire tree using a string builder. Each node should end with a newline but not start with one.
     * Indents are provided with tabs, and overall are incremented by two per node ( usually, exceptions include codeblocks):
     * <pre>
     *     Node{
     *         arg1={
     *
     *         }
     *     }
     * </pre>
     */
    public abstract void prettyPrint(StringBuilder stringBuilder, String indent);
    public abstract String toString(); // Force all subclasses to have to implement this
}
