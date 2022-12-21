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
    public void prettyPrint(StringBuilder stringBuilder, int indent) {
        String stringIndent = "\t".repeat(indent);

        stringBuilder.append(stringIndent);
        stringBuilder.append("Operation{");
        stringBuilder.append(operatorType);
        stringBuilder.append("\n");

        indent += 1;
        stringIndent = "\t".repeat(indent);
        indent += 1;

        stringBuilder.append(stringIndent);
        stringBuilder.append("a={\n");
        a.prettyPrint(stringBuilder, indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("},\n");
        stringBuilder.append(stringIndent);
        stringBuilder.append("b={\n");
        b.prettyPrint(stringBuilder, indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");

        indent -= 2;
        stringIndent = "\t".repeat(indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");
    }
}
