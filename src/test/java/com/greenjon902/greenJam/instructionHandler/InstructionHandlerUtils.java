package com.greenjon902.greenJam.instructionHandler;

import static com.greenjon902.greenJam.instructionHandler.InstructionToken.InstructionTokenType.IDENTIFIER;
import static com.greenjon902.greenJam.instructionHandler.InstructionToken.InstructionTokenType.SYNTAX_RULE;

public class InstructionHandlerUtils {
    public static final InstructionToken SYNTAX = InstructionKeyword.SYNTAX.instructionToken;
    public static final InstructionToken RULE = InstructionKeyword.RULE.instructionToken;
    public static final InstructionToken ADD = InstructionKeyword.ADD.instructionToken;
    public static final InstructionToken REMOVE = InstructionKeyword.REMOVE.instructionToken;
    public static final InstructionToken IGNORED = InstructionKeyword.IGNORED.instructionToken;
    public static final InstructionToken LINK = InstructionKeyword.LINK.instructionToken;
}
