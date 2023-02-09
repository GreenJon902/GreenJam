package com.greenjon902.greenJam.instructionHandler.handlers;

import com.greenjon902.greenJam.common.Errors;
import com.greenjon902.greenJam.common.InstructionTokenInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.instructionHandler.InstructionKeyword;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;

public class SyntaxIgnoredInstructionHandler extends InstructionHandlerBase {
    private final SyntaxContext syntaxContext;

    public SyntaxIgnoredInstructionHandler(SyntaxContext syntaxContext) {
        this.syntaxContext = syntaxContext;
    }

    @Override
    public void handle(InstructionTokenInputStream tokens) {
        InstructionToken instruction = tokens.consume();
        if (instruction.type != InstructionToken.InstructionTokenType.KEYWORD) {
            tokens.location -= 1;
            Errors.instructionHandler_invalidTokenAtLocation(tokens);
        }

        if (!tokens.hasLeft(1)) Errors.instructionHandler_invalidInstructionAmountLeftForType(tokens, "syntax", 2);

        if (tokens.next().type != InstructionToken.InstructionTokenType.STRING) Errors.instructionHandler_invalidTokenAtLocation(tokens);
        String string = (String) tokens.consume().storage;


        switch ((InstructionKeyword) instruction.storage) {
            case ADD -> syntaxContext.ignore((String) string);
            case REMOVE -> syntaxContext.removeIgnore((String) string);
            default -> {
                tokens.location -= 1;
                Errors.instructionHandler_invalidTokenAtLocation(tokens);
            }
        }
    }
}
