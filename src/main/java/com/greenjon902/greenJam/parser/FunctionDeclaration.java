package com.greenjon902.greenJam.parser;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionDeclaration extends AbstractSyntaxTreeNode {
    public final Identifier functionName;
    public final AbstractSyntaxTreeNode returnType;
    public final Argument[] arguments;
    public final CodeBlock codeBlock;

    public FunctionDeclaration(Identifier functionName, AbstractSyntaxTreeNode returnType, Argument[] arguments, CodeBlock codeBlock) {
        super();
        this.functionName = functionName;
        this.returnType = returnType;
        this.arguments = arguments;
        this.codeBlock = codeBlock;
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("FunctionDeclaration{").append("\n");
        stringBuilder.append(indent).append("\tname={\n");
        functionName.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\treturnType={\n");
        returnType.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("\targuments={\n");
        boolean firstLoop = true;
        for (Argument argument : arguments) {
            if (firstLoop) {
                firstLoop = false;
            } else {
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
                stringBuilder.append(",\n");
            }
            argument.prettyPrint(stringBuilder, indent + "\t\t");
        }
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("\tcodeBlock={\n");
        codeBlock.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("}\n");
    }

    @Override
    public String toString() {
        return "FunctionDeclaration{" +
                "functionName=" + functionName +
                ", returnType=" + returnType +
                ", arguments=" + Arrays.toString(arguments) +
                ", codeBlock=" + codeBlock +
                '}';
    }


    static class Argument extends AbstractSyntaxTreeNode {
        public final AbstractSyntaxTreeNode type;
        public final Identifier name;

        Argument(AbstractSyntaxTreeNode type, Identifier identifier) {
            this.type = type;
            this.name = identifier;
        }

        @Override
        public void prettyPrint(StringBuilder stringBuilder, String indent) {
            stringBuilder.append(indent).append("FunctionDeclaration.Argument{").append("\n");
            stringBuilder.append(indent).append("\ttype={\n");
            type.prettyPrint(stringBuilder, indent + "\t\t");
            stringBuilder.append(indent).append("\t},\n");
            stringBuilder.append(indent).append("\tname={\n");
            name.prettyPrint(stringBuilder, indent + "\t\t");
            stringBuilder.append(indent).append("\t}\n");
            stringBuilder.append(indent).append("}\n");
        }

        @Override
        public String toString() {
            return null;
        }
    }
}
