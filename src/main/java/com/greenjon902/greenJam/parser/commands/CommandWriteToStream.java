package com.greenjon902.greenJam.parser.commands;

import com.greenjon902.greenJam.parser.AbstractSyntaxTreeNode;
import com.greenjon902.greenJam.parser.Command;
import com.greenjon902.greenJam.parser.Identifier;

public class CommandWriteToStream extends Command {
    private final AbstractSyntaxTreeNode stream;
    private final AbstractSyntaxTreeNode data;

    @Override
    public String toString() {
        return "CommandWriteToStream{" +
                "stream=" + stream +
                ", data=" + data +
                '}';
    }

    public CommandWriteToStream(AbstractSyntaxTreeNode stream, AbstractSyntaxTreeNode data) {
        this.stream = stream;
        this.data = data;
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append("Command.WriteToStream{\n");
        stringBuilder.append(indent).append("\tstream={\n");
        stream.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t},\n");
        stringBuilder.append(indent).append("\tdata={\n");
        data.prettyPrint(stringBuilder, indent + "\t\t");
        stringBuilder.append(indent).append("\t}\n");
        stringBuilder.append(indent).append("}\n");
    }
}
