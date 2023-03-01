package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.common.Tuple;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;

import java.util.ArrayList;

public class SyntaxParser {
    public static SyntaxRule parse(SyntaxToken[] tokens) {
        ArrayList<SyntaxInstruction> syntaxInstructions = new ArrayList<>();
        ArrayList<Object> syntaxInstructionData = new ArrayList<>();
        int highestMemoryLocation = -1;

        for (int i=0; i < tokens.length; i++) {
            SyntaxToken currentToken = tokens[i];
            switch (currentToken.type) {
                case LITERAL -> {
                    syntaxInstructions.add(SyntaxInstruction.MATCH_LITERAL);
                    syntaxInstructionData.add(currentToken.storage); // Literal text
                }
                case OPERATOR -> {
                    if (((SyntaxOperator) currentToken.storage).type == SyntaxOperator.SyntaxOperatorType.START_RECORD) {
                        syntaxInstructions.add(SyntaxInstruction.START_RECORD);
                        syntaxInstructionData.add(((SyntaxOperator) currentToken.storage).storage);
                        highestMemoryLocation = Math.max(highestMemoryLocation, (Integer) ((SyntaxOperator) currentToken.storage).storage);

                    } else if ((((SyntaxOperator) currentToken.storage).type == SyntaxOperator.SyntaxOperatorType.STOP_RECORD)) {
                        syntaxInstructions.add(SyntaxInstruction.STOP_RECORD);
                        syntaxInstructionData.add(((SyntaxOperator) currentToken.storage).storage);
                        highestMemoryLocation = Math.max(highestMemoryLocation, (Integer) ((SyntaxOperator) currentToken.storage).storage);

                    } else {
                        throw new RuntimeException();
                    }
                }
                case GROUP_SUBSTITUTION -> {
                    syntaxInstructions.add(SyntaxInstruction.MATCH_GROUP);
                    syntaxInstructionData.add(currentToken.storage); // Group name
                }
                case RECORDED_GROUP_SUBSTITUTION -> {
                    syntaxInstructions.add(SyntaxInstruction.RECORD_GROUP);
                    syntaxInstructionData.add(currentToken.storage); // Group name
                    highestMemoryLocation = Math.max(highestMemoryLocation, ((Tuple.Two<Integer, String>) currentToken.storage).A);
                }
                default -> throw new RuntimeException();
            }
        }

        return new SimpleSyntaxRule(highestMemoryLocation + 1, syntaxInstructions.toArray(SyntaxInstruction[]::new),
                syntaxInstructionData.toArray(Object[]::new));
    }
}
