package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxMatcher;
import com.greenjon902.greenJam.common.Tuple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxBuilderTest {
    @Test
    void parse() {
        SyntaxMatcherImpl syntaxMatcher =
                (SyntaxMatcherImpl) SyntaxBuilder.build("if <1{identifier}>1 <==> <2{identifier}:>2");
        assertEquals(
                new SyntaxMatcherImpl(3,
                        new SyntaxInstruction[]{
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.RECORD_GROUP,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.START_RECORD_CHARACTER,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.STOP_RECORD_CHARACTER,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.START_RECORD_CHARACTER,
                                SyntaxInstruction.MATCH_GROUP,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.STOP_RECORD_CHARACTER,
                        },
                        new Object[]{
                                "if ",
                                new Tuple.Two<>(1, "identifier"),
                                " ",
                                0,
                                "==",
                                0,
                                " ",
                                2,
                                "identifier",
                                ":",
                                2
                        }
        ), syntaxMatcher);
    }
}