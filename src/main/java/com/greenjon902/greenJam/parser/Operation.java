package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.tokenizer.OperatorType;

public class Operation extends AbstractSyntaxTreeNode {
    public final OperatorType operatorType;
    public final AbstractSyntaxTreeNode a;
    public final AbstractSyntaxTreeNode b;

    @Override
    public String toString() {
        return "Operation{" +
                "operatorType=" + operatorType +
                ", a=" + a +
                ", b=" + b +
                '}';
    }

    public Operation(OperatorType operatorType, AbstractSyntaxTreeNode a, AbstractSyntaxTreeNode b) {
        this.operatorType = operatorType;
        this.a = a;
        this.b = b;
    }
}
