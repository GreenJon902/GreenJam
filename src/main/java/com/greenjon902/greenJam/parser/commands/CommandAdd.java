package com.greenjon902.greenJam.parser.commands;

import com.greenjon902.greenJam.parser.AbstractSyntaxTreeNode;
import com.greenjon902.greenJam.parser.Command;
import com.greenjon902.greenJam.parser.Identifier;

public class CommandAdd extends Command {


    private final AbstractSyntaxTreeNode input_1;
    private final AbstractSyntaxTreeNode input_2;
    private final Identifier output;

    public CommandAdd(AbstractSyntaxTreeNode input_1, AbstractSyntaxTreeNode input_2, Identifier output) {
        this.input_1 = input_1;
        this.input_2 = input_2;
        this.output = output;
    }

    public CommandAdd(AbstractSyntaxTreeNode input_1, AbstractSyntaxTreeNode input_2) {
        this(input_1, input_2, null);
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, int indent) {
        String stringIndent = "\t".repeat(indent);

        stringBuilder.append(stringIndent);
        stringBuilder.append("Command.Add{");
        stringBuilder.append("\n");

        indent += 1;
        stringIndent = "\t".repeat(indent);
        indent += 1;

        stringBuilder.append(stringIndent);
        stringBuilder.append("input_1={\n");
        input_1.prettyPrint(stringBuilder, indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("},\n");
        stringBuilder.append(stringIndent);
        stringBuilder.append("input_2={\n");
        input_2.prettyPrint(stringBuilder, indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");

        if (output != null) {
            stringBuilder.append("output={\n");
            output.prettyPrint(stringBuilder, indent);
            stringBuilder.append(stringIndent);
            stringBuilder.append("}\n");
        }

        indent -= 2;
        stringIndent = "\t".repeat(indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");
    }

    @Override
    public String toString() {
        return "CommandAdd{" +
                "input_1=" + input_1 +
                ", input_2=" + input_2 +
                ", output=" + output +
                '}';
    }
}
