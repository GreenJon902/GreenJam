package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;
import com.greenjon902.greenJam.instructionHandler.InstructionHandlerBase;

public class Parser {
    private InstructionHandlerBase instructionHandler;
    private SyntaxContext syntaxContext;

    public Parser(InstructionHandlerBase instructionHandler, SyntaxContext syntaxContext) {
        this.instructionHandler = instructionHandler;
        this.syntaxContext = syntaxContext;
    }

    public AstNode parse(StringInputStream string) {
        while (tryParseInstruction(string)); // Repeat till no more instructions

        if (string.isEnd()) return null;

        if (!syntaxContext.hasRootGroup()) {
            Errors.parser_noRootGroup(string);
        }
        AstNode node = SyntaxRule.match(string, syntaxContext.getRootGroup(), syntaxContext);

        while (tryParseInstruction(string)); // Repeat till no more instructions

        return node;
    }

    public boolean tryParseInstruction(StringInputStream string) {
        while (string.consumeIfAny(syntaxContext.getIgnored()));

        if (string.consumeIf(syntaxContext.getCommandStartStartString())) {
            int ret = instructionHandler.handle(string);
            if (ret != 0) {
                Errors.parser_invalidCommand(string);
            }
            return true;
        }
        return false;
    }
}
