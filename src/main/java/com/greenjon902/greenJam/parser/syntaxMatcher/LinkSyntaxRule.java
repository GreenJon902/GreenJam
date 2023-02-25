package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxRule;

import java.util.Objects;

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
        while (string.consumeIfAny(syntaxContext.getIgnored()));

        return match(string, otherGroup, syntaxContext);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkSyntaxRule that = (LinkSyntaxRule) o;
        return otherGroup.equals(that.otherGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(otherGroup);
    }
}
