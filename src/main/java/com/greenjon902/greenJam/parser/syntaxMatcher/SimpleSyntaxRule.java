package com.greenjon902.greenJam.parser.syntaxMatcher;


import com.greenjon902.greenJam.common.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class SimpleSyntaxRule extends SyntaxRule {
    private final int memoryLocationNumber;
    private final SyntaxInstruction[] syntaxInstructions;
    private final Object[] syntaxInstructionData;

    public SimpleSyntaxRule(int memoryLocations, SyntaxInstruction[] syntaxInstructions, Object[] syntaxInstructionData) {
        this.memoryLocationNumber = memoryLocations;
        this.syntaxInstructions = syntaxInstructions;
        this.syntaxInstructionData = syntaxInstructionData;
    }

    @Override
    public String toString() {
        return "SimpleSyntaxRule{" +
                "memoryLocations=" + memoryLocationNumber +
                ", syntaxInstructions=" + Arrays.toString(syntaxInstructions) +
                ", syntaxInstructionData=" + Arrays.toString(syntaxInstructionData) +
                '}';
    }

    public String format() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int instructionIndex = 0; instructionIndex < syntaxInstructions.length; instructionIndex++) {
            SyntaxInstruction instruction = syntaxInstructions[instructionIndex];
            Object data = syntaxInstructionData[instructionIndex];

            switch (instruction) {
                case MATCH_LITERAL -> stringBuilder.append(data);
                case START_RECORD -> stringBuilder.append("<").append(data);
                case STOP_RECORD -> stringBuilder.append(">").append(data);
                case MATCH_GROUP -> stringBuilder.append("{").append(data).append("}");
                case RECORD_GROUP -> {
                    Tuple.Two<Integer, String> groupRecordData = (Tuple.Two<Integer, String>) data;
                    stringBuilder.append("<").append(groupRecordData.A).append("{").append(groupRecordData.B).append("}>").append(groupRecordData.A);
                }
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public AstNode match(StringInputStream string, SyntaxContext syntaxContext) {
        while (string.consumeIfAny(syntaxContext.getIgnored()));

        int stringLocationSave = string.location;

        HashMap<Integer, Integer> currentRecordings = new HashMap<>();
        Object[] memoryLocations = new Object[memoryLocationNumber];

        for (int instructionIndex = 0; instructionIndex < syntaxInstructions.length; instructionIndex++) {
            SyntaxInstruction instruction = syntaxInstructions[instructionIndex];
            Object data = syntaxInstructionData[instructionIndex];

            switch (instruction) {
                case MATCH_LITERAL -> {
                    if (!string.consumeIf((String) data)) {
                        string.location = stringLocationSave;
                        return null;
                    }
                }
                case START_RECORD -> {
                    if (currentRecordings.containsKey((Integer) data)) Errors.syntaxMatcher_alreadyRecording(string, this, (Integer) data);
                    currentRecordings.put((Integer) data, string.location);
                }
                case STOP_RECORD -> {
                    if (!currentRecordings.containsKey((Integer) data)) Errors.syntaxMatcher_triedToStopRecordingWhenNotRecording(string, this, (Integer) data);
                    if (memoryLocations[(Integer) data] == null) memoryLocations[(Integer) data] = "";
                    memoryLocations[(Integer) data] += string.string.substring(currentRecordings.get(data), string.location);
                    currentRecordings.remove((Integer) data);
                }
                case MATCH_GROUP -> {
                    if (!syntaxContext.hasGroup((String) data)) Errors.syntaxMatcher_unknownGroup(string, this, (String) data);
                    AstNode astNode = match(string, (String) data, syntaxContext);
                    if (astNode == null) {
                        string.location = stringLocationSave;
                        return null;
                    }
                }
                case RECORD_GROUP -> {
                    if (!syntaxContext.hasGroup(((Tuple.Two<Integer, String>) data).B)) Errors.syntaxMatcher_unknownGroup(string, this, ((Tuple.Two<Integer, String>) data).B);
                    if (memoryLocations[((Tuple.Two<Integer, String>) data).A] != null) Errors.syntaxMatcher_triedToRerecordNode(string, this, ((Tuple.Two<Integer, String>) data).A, ((Tuple.Two<Integer, String>) data).B);
                    AstNode astNode = match(string, ((Tuple.Two<Integer, String>) data).B, syntaxContext);
                    if (astNode == null) {
                        string.location = stringLocationSave;
                        return null;
                    }
                    memoryLocations[((Tuple.Two<Integer, String>) data).A] = astNode;


                }
                default -> throw new RuntimeException();
            }
        }

        return new AstNode(memoryLocations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSyntaxRule rule = (SimpleSyntaxRule) o;
        return memoryLocationNumber == rule.memoryLocationNumber && Arrays.equals(syntaxInstructions, rule.syntaxInstructions) && Arrays.equals(syntaxInstructionData, rule.syntaxInstructionData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(memoryLocationNumber);
        result = 31 * result + Arrays.hashCode(syntaxInstructions);
        result = 31 * result + Arrays.hashCode(syntaxInstructionData);
        return result;
    }
}
