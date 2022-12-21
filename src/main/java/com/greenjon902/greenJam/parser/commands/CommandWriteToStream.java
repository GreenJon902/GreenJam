package com.greenjon902.greenJam.parser.commands;

import com.greenjon902.greenJam.parser.AbstractSyntaxTreeNode;
import com.greenjon902.greenJam.parser.Command;
import com.greenjon902.greenJam.parser.Identifier;

public class CommandWriteToStream extends Command {
    private final AbstractSyntaxTreeNode stream;
    private final AbstractSyntaxTreeNode data;

    public CommandWriteToStream(AbstractSyntaxTreeNode stream, AbstractSyntaxTreeNode data) {
        this.stream = stream;
        this.data = data;
    }

    @Override
    public void prettyPrint(StringBuilder stringBuilder, int indent) {
        String stringIndent = "\t".repeat(indent);

        stringBuilder.append(stringIndent);
        stringBuilder.append("Command.WriteToStream{");
        stringBuilder.append("\n");

        indent += 1;
        stringIndent = "\t".repeat(indent);
        indent += 1;

        stringBuilder.append(stringIndent);
        stringBuilder.append("stream={\n");
        stream.prettyPrint(stringBuilder, indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("},\n");
        stringBuilder.append(stringIndent);
        stringBuilder.append("data={\n");
        data.prettyPrint(stringBuilder, indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");

        indent -= 2;
        stringIndent = "\t".repeat(indent);
        stringBuilder.append(stringIndent);
        stringBuilder.append("}\n");
    }
}
