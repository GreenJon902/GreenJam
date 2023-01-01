package com.greenjon902.greenJam.parser;

public class Conditional extends AbstractSyntaxTreeNode {

    public final AbstractSyntaxTreeNode expression;
    public final CodeBlock codeBlock;

    public Conditional(AbstractSyntaxTreeNode expression, CodeBlock codeBlock) {
        this.expression = expression;
        this.codeBlock = codeBlock;
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("Conditional{").append("\n");
        stringBuilder.append(indent).append("\texpression={\n");
        expression.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\tcodeBlock={\n");
        codeBlock.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("}\n");
    }

    @Override
    public String toString() {
        return "Conditional{" +
                "expression=" + expression +
                ", codeBlock=" + codeBlock +
                '}';
    }
}
