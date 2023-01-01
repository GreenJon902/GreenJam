package com.greenjon902.greenJam.parser;

import java.util.Arrays;

public class FunctionArguments extends AbstractSyntaxTreeNode {
    public final AbstractSyntaxTreeNode[] arguments;

    public FunctionArguments(AbstractSyntaxTreeNode[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "FunctionArguments{" +
                "arguments=" + Arrays.toString(arguments) +
                '}';
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("FunctionArguments{\n");
        boolean firstLoop = true;
        for (AbstractSyntaxTreeNode argument : arguments) {
            if (firstLoop) {
                firstLoop = false;
            } else {
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
                stringBuilder.append(",\n");
            }
            argument.prettyPrint(stringBuilder, indent + "\t");
        }
        stringBuilder.append(indent).append("}\n");
    }
}
