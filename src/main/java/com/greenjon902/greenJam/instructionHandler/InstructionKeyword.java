package com.greenjon902.greenJam.instructionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public enum InstructionKeyword {
    SYNTAX("SYNTAX"), RULE("RULE"), ADD("ADD"), REMOVE("REMOVE"), ALL("ALL");

    public final String string;

    public static final InstructionKeyword[] lengthOrderedKeywords;
    static {
        lengthOrderedKeywords = new InstructionKeyword[InstructionKeyword.values().length];
        int i = 0;
        for (InstructionKeyword keyword : InstructionKeyword.values()) {
            lengthOrderedKeywords[i] = keyword;
            i += 1;
        }
        Arrays.sort(lengthOrderedKeywords, Comparator.comparingInt(o -> o.string.length()));
        Arrays.sort(lengthOrderedKeywords, Comparator.reverseOrder());
    }

    InstructionKeyword(String string) {
        this.string = string;
    }
}
