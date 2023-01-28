package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxMatcher;

import java.util.Arrays;
import java.util.Objects;

public class SyntaxMatcherImpl implements SyntaxMatcher {
    final int memoryLocations;
    final SyntaxInstruction[] syntaxInstructions;
    final Object[] syntaxInstructionData;

    public SyntaxMatcherImpl(int memoryLocations, SyntaxInstruction[] syntaxInstructions, Object[] syntaxInstructionData) {
        this.memoryLocations = memoryLocations;
        this.syntaxInstructions = syntaxInstructions;
        this.syntaxInstructionData = syntaxInstructionData;
    }

    @Override
    public AstNode parse(StringInputStream inputStream) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntaxMatcherImpl that = (SyntaxMatcherImpl) o;
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
        return "SyntaxMatcherImpl{" +
                "memoryLocations=" + memoryLocations +
                ", syntaxInstructions=" + Arrays.toString(syntaxInstructions) +
                ", syntaxInstructionData=" + Arrays.toString(syntaxInstructionData) +
                '}';
    }
}
