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

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("Operation{").append(operatorType).append("\n");
        stringBuilder.append(indent).append("\ta={\n");
        a.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\tb={\n");
        b.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("}\n");
    }
}
