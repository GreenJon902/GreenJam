package com.greenjon902.greenJam.syntaxBuilder;

import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.common.Tuple;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxBuilderTest {
    @Test
    void parse() {
        SyntaxRule syntaxMatcher = SyntaxBuilder.build("if <1{identifier}>1 <==> <2{identifier}:>2");
        assertEquals(
                new SimpleSyntaxRule(3,
                        new SyntaxInstruction[]{
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.RECORD_GROUP,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.START_RECORD,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.STOP_RECORD,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.START_RECORD,
                                SyntaxInstruction.MATCH_GROUP,
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.STOP_RECORD,
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

    @Test
    void parseWithEnd() {
        SyntaxRule syntaxMatcher = SyntaxBuilder.build("while <{identifier}>`i do not matter");
        assertEquals(
                new SimpleSyntaxRule(1,
                        new SyntaxInstruction[]{
                                SyntaxInstruction.MATCH_LITERAL,
                                SyntaxInstruction.RECORD_GROUP,
                        },
                        new Object[]{
                                "while ",
                                new Tuple.Two<>(0, "identifier"),
                        }
                ), syntaxMatcher);
    }
}