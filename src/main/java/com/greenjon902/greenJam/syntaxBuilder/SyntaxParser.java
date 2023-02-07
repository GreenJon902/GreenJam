package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.common.Tuple;

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
                        if (i + 2 < tokens.length && tokens[i + 1].type == SyntaxTokenType.GROUP_SUBSTITUTION && // Is next group?
                                tokens[i + 2].type == SyntaxTokenType.OPERATOR && // Is next next operator?
                                ((SyntaxOperator) tokens[i + 2].storage).type == SyntaxOperator.SyntaxOperatorType.STOP_RECORD && // Is next next stop?
                                ((SyntaxOperator) currentToken.storage).storage == ((SyntaxOperator) tokens[i + 2].storage).storage) { // Is next next same location?

                            syntaxInstructions.add(SyntaxInstruction.RECORD_GROUP);
                            syntaxInstructionData.add(
                                    new Tuple.Two<>((Integer) ((SyntaxOperator) currentToken.storage).storage, // Memory location
                                            (String) tokens[i + 1].storage)); // Group name
                            i += 2; // Group, Stop Record
                            highestMemoryLocation = Math.max(highestMemoryLocation, (Integer) ((SyntaxOperator) currentToken.storage).storage);

                        } else {
                            syntaxInstructions.add(SyntaxInstruction.START_RECORD);
                            syntaxInstructionData.add(((SyntaxOperator) currentToken.storage).storage); // Memory location
                            highestMemoryLocation = Math.max(highestMemoryLocation, (Integer) ((SyntaxOperator) currentToken.storage).storage);
                        }
                    } else if ((((SyntaxOperator) currentToken.storage).type == SyntaxOperator.SyntaxOperatorType.STOP_RECORD)) {
                        syntaxInstructions.add(SyntaxInstruction.STOP_RECORD);
                        syntaxInstructionData.add(((SyntaxOperator) currentToken.storage).storage); // Memory location
                        highestMemoryLocation = Math.max(highestMemoryLocation, (Integer) ((SyntaxOperator) currentToken.storage).storage);

                    } else {
                        throw new RuntimeException();
                    }
                }
                case GROUP_SUBSTITUTION -> {
                    syntaxInstructions.add(SyntaxInstruction.MATCH_GROUP);
                    syntaxInstructionData.add(currentToken.storage); // Group name
                }
                default -> throw new RuntimeException();
            }
        }

        return new SyntaxRule(highestMemoryLocation + 1, syntaxInstructions.toArray(SyntaxInstruction[]::new),
                syntaxInstructionData.toArray(Object[]::new));
    }
}
