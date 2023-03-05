package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.SyntaxContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JoiningSyntaxRuleTest {
    @Test
    void joining() {
        SyntaxContext syntaxContext = new SyntaxContext();
        syntaxContext.add("group1", new ParserTestResources.ReturningSyntaxRule(1, 2, 3));
        syntaxContext.add("group2", new ParserTestResources.ReturningSyntaxRule(4));

        JoiningSyntaxRule joiningSyntaxRule = new JoiningSyntaxRule("group1", "group2");

        assertEquals(new AstNode(1, 2, 3, 4),
                joiningSyntaxRule.match("", syntaxContext));
    }
}
