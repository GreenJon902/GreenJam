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
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("Command.Add{\n");
        stringBuilder.append(indent).append("\tinput_1={\n");
        input_1.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\tinput_2={\n");
        input_2.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        if (output != null) {
            stringBuilder.append(indent).append("\toutput={\n");
                output.prettyPrint(stringBuilder, indent + "\t\t");
            stringBuilder.append(indent).append("\t}\n");
        }
        stringBuilder.append(indent).append("}\n");
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
