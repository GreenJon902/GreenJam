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
}
