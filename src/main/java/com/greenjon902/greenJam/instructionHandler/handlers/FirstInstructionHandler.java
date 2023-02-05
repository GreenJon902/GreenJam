package com.greenjon902.greenJam.instructionHandler.handlers;

import com.greenjon902.greenJam.common.Errors;
import com.greenjon902.greenJam.common.InstructionTokenInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.instructionHandler.InstructionKeyword;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;

public class FirstInstructionHandler extends InstructionHandlerBase {
    private final SyntaxInstructionHandler syntaxInstructionHandler;

    public FirstInstructionHandler(SyntaxContext syntaxContext) {
        this.syntaxInstructionHandler = new SyntaxInstructionHandler(syntaxContext);
    }

    @Override
    public void handle(InstructionTokenInputStream tokens) {
        if (tokens.next().type != InstructionToken.InstructionTokenType.KEYWORD) {
            Errors.instructionHandler_invalidTokenAtLocation(tokens);
        }

        switch ((InstructionKeyword) tokens.next().storage) {
            case SYNTAX -> {
                tokens.consume();
                syntaxInstructionHandler.handle(tokens);
            }
            default -> Errors.instructionHandler_invalidTokenAtLocation(tokens);
        }
    }
}
