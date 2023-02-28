package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.StringInputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class InstructionHandlerBase {
    private final HashMap<InstructionToken[], Consumer<InstructionToken[]>> pathways = new HashMap<>();

    protected void addPathway(Consumer<InstructionToken[]> handler, InstructionToken... route) {
        pathways.put(route, handler);
    }

    /**
     * See {@link #handle(InstructionToken...)}
     */
    public int handle(StringInputStream instruction) {
        InstructionToken[] instructionTokens = InstructionTokenizer.tokenize(instruction);
        return handle(instructionTokens);
    }

    /**
     * Handles an instruction with one of the predefined pathways. Returns 0 if handled successfully and 1 if no route
     * was found.
     */
    public int handle(InstructionToken... instructionTokens) {
        for (InstructionToken[] route : pathways.keySet()) {
            if (route.length == instructionTokens.length) {
                ArrayList<InstructionToken> arguments = new ArrayList<>();
                boolean failed = false;

                for (int i=0; i<route.length; i++) {
                    InstructionToken part = route[i];
                    if (part.type == InstructionToken.InstructionTokenType.__ARGUMENT__) {
                        if (part.storage != instructionTokens[i].type) {
                            failed = true;
                            break;
                        }
                        arguments.add(instructionTokens[i]);
                    } else if (!part.equals(instructionTokens[i])) {
                        failed = true;
                        break;
                    }
                }

                if (!failed) {
                    pathways.get(route).accept(arguments.toArray(InstructionToken[]::new));
                    return 0;
                }
            }
        }

        return 1;
    }
}