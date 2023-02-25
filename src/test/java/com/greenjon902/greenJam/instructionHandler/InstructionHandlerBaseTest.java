package com.greenjon902.greenJam.instructionHandler;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.greenjon902.greenJam.instructionHandler.InstructionKeyword.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InstructionHandlerBaseTest {
    @Test
    void handleKeywords() {
        AtomicInteger resultCode = new AtomicInteger(0);

        InstructionHandlerBase instructionHandler = new InstructionHandlerBase() {};
        instructionHandler.addPathway((InstructionToken[] instructions) -> resultCode.set(1),
                SYNTAX.instructionToken, ADD.instructionToken);
        instructionHandler.addPathway((InstructionToken[] instructions) -> resultCode.set(2),
                SYNTAX.instructionToken, REMOVE.instructionToken);

        assertEquals(0, resultCode.get());
        assertEquals(0, instructionHandler.handle(SYNTAX.instructionToken, REMOVE.instructionToken));
        assertEquals(2, resultCode.get());
        assertEquals(0, instructionHandler.handle(SYNTAX.instructionToken, ADD.instructionToken));
        assertEquals(1, resultCode.get());
        assertEquals(1, instructionHandler.handle(SYNTAX.instructionToken));
        assertEquals(1, resultCode.get());
    }
}