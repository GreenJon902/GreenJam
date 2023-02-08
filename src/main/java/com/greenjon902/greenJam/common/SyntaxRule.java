package com.greenjon902.greenJam.common;


import java.util.Arrays;
import java.util.Objects;

public class SyntaxRule {
    public final int memoryLocations;
    public final SyntaxInstruction[] syntaxInstructions;
    public final Object[] syntaxInstructionData;

    public SyntaxRule(int memoryLocations, SyntaxInstruction[] syntaxInstructions, Object[] syntaxInstructionData) {
        this.memoryLocations = memoryLocations;
        this.syntaxInstructions = syntaxInstructions;
        this.syntaxInstructionData = syntaxInstructionData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntaxRule that = (SyntaxRule) o;
        return memoryLocations == that.memoryLocations && Arrays.equals(syntaxInstructions, that.syntaxInstructions) && Arrays.equals(syntaxInstructionData, that.syntaxInstructionData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(memoryLocations);
        result = 31 * result + Arrays.hashCode(syntaxInstructions);
        result = 31 * result + Arrays.hashCode(syntaxInstructionData);
        return result;
    }

    @Override
    public String toString() {
        return "SyntaxRule{" +
                "memoryLocations=" + memoryLocations +
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

    public static class Link extends SyntaxRule {

        public final String otherGroup;

        public Link(String otherGroup) {
            super(-1, null, null);
            this.otherGroup = otherGroup;
        }

        public String format() {
            return "LinkTo{" + otherGroup + "}";
        }

        @Override
        public String toString() {
            return "SyntaxRule.link{" +
                    "otherGroup" + otherGroup +
                    '}';
        }
    }
}
