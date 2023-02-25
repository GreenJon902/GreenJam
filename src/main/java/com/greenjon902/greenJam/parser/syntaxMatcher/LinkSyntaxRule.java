package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxRule;

public class LinkSyntaxRule extends SyntaxRule {
    public final String otherGroup;

    public LinkSyntaxRule(String otherGroup) {
        this.otherGroup = otherGroup;
    }

    public String format() {
        return "LinkTo{" + otherGroup + "}";
    }

    @Override
    public String toString() {
        return "LinkSyntaxRule{" +
                "otherGroup" + otherGroup +
                '}';
    }

    @Override
    public AstNode match(StringInputStream string, SyntaxContext syntaxContext) {
        return match(string, otherGroup, syntaxContext);
    }
}
