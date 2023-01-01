package com.greenjon902.greenJam.parser;

import java.util.Arrays;

public class If extends AbstractSyntaxTreeNode {
    public final Conditional[] conditionals;
    public final CodeBlock elseCodeBlock;

    public If(Conditional[] conditionals, CodeBlock elseCodeBlock) {
        this.conditionals = conditionals;
        this.elseCodeBlock = elseCodeBlock;
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("If{").append("\n");
        stringBuilder.append(indent).append("\tconditionals={\n");
        boolean firstLoop = true;
        for (Conditional conditional : conditionals) {
            if (firstLoop) {
                firstLoop = false;
            } else {
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
                stringBuilder.append(",\n");
            }
            conditional.prettyPrint(stringBuilder, indent + "\t\t");
        }
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\telseCodeBlock={\n");
        elseCodeBlock.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("}\n");
    }

    @Override
    public String toString() {
        return "If{" +
                "conditionals=" + Arrays.toString(conditionals) +
                ", elseCodeBlock=" + elseCodeBlock +
                '}';
    }
}
