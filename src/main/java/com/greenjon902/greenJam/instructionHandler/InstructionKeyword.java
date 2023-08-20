package com.greenjon902.greenJam.instructionHandler;

import java.util.Arrays;
import java.util.Comparator;

public enum InstructionKeyword {
    SYNTAX("SYNTAX"), RULE("RULE"), ADD("ADD"), REMOVE("REMOVE"), ALL("ALL"),
    IGNORED("IGNORED"), LINK("LINK"), REPEATING("REPEATING"), ROOT_NODE("ROOT_NODE"),
    SET("SET"), JOIN("JOIN"), EXPRESSIONS("EXPRESSION"), ERROR("ERROR"),
    ASSIGN("ASSIGN"), ERROR_CONDITION("ERROR_CONDITION"), RETURNING_NULL("RETURNING_NULL"),
    NEXT_IS("NEXT_IS"), NOT("NOT"), AND("AND"), CREATE("CREATE"),
    INCLUDE("INCLUDE"), END_OF_FILE("END_OF_FILE");

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

    public final InstructionToken instructionToken;

    InstructionKeyword(String string) {
        this.string = string;
        instructionToken = new InstructionToken(InstructionToken.InstructionTokenType.KEYWORD, this);
    }
}
