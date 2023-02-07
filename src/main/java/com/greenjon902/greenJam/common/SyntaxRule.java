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
        return "SyntaxRuleImpl{" +
                "memoryLocations=" + memoryLocations +
                ", syntaxInstructions=" + Arrays.toString(syntaxInstructions) +
                ", syntaxInstructionData=" + Arrays.toString(syntaxInstructionData) +
                '}';
    }
}
