package com.greenjon902.greenJam.instructionHandler.handlers;

import com.greenjon902.greenJam.common.InstructionTokenInputStream;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;

public abstract class InstructionHandlerBase {
    public abstract void handle(InstructionTokenInputStream tokens);
}
