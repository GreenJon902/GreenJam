package com.greenjon902.greenJam.instructionHandler.handlers;

import com.greenjon902.greenJam.common.Errors;
import com.greenjon902.greenJam.common.InstructionTokenInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.instructionHandler.InstructionKeyword;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;

public class SyntaxInstructionHandler extends InstructionHandlerBase {
    private final SyntaxRuleInstructionHandler syntaxRuleInstructionHandler;
    private final SyntaxIgnoredInstructionHandler syntaxIgnoredInstructionHandler;

    public SyntaxInstructionHandler(SyntaxContext syntaxContext) {
        this.syntaxRuleInstructionHandler = new SyntaxRuleInstructionHandler(syntaxContext);
        this.syntaxIgnoredInstructionHandler = new SyntaxIgnoredInstructionHandler(syntaxContext);
    }

    @Override
    public void handle(InstructionTokenInputStream tokens) {
        if (tokens.next().type != InstructionToken.InstructionTokenType.KEYWORD) {
            Errors.instructionHandler_invalidTokenAtLocation(tokens);
        }

        switch ((InstructionKeyword) tokens.next().storage) {
            case RULE -> {
                tokens.consume();
                syntaxRuleInstructionHandler.handle(tokens);
            }
            case IGNORED -> {
                tokens.consume();
                syntaxIgnoredInstructionHandler.handle(tokens);
            }
            default -> Errors.instructionHandler_invalidTokenAtLocation(tokens);
        }
    }
}
