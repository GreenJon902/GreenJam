package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxRule;

import java.util.Objects;

public class JoiningSyntaxRule extends SyntaxRule {
    private final String group1;
    private final String group2;

    public JoiningSyntaxRule(String group1, String group2) {
        this.group1 = group1;
        this.group2 = group2;
    }

    @Override
    public String format() {
        return toString();
    }

    @Override
    public AstNode match(StringInputStream string, Contexts contexts) {
        while (string.consumeIfAny(contexts.syntax.getIgnored()));

        int stringLocationSave = string.location;

        AstNode node1 = SyntaxRule.match(string, group1, contexts);
        AstNode node2 = SyntaxRule.match(string, group2, contexts);

        if (node1 == null || node2 == null) {
            string.location = stringLocationSave;
            return null;
        }

        Object[] resultStorage = new Object[node1.storage.length + node2.storage.length];
        System.arraycopy(node1.storage, 0, resultStorage, 0, node1.storage.length);
        System.arraycopy(node2.storage, 0, resultStorage, node1.storage.length, node2.storage.length);

        return new AstNode(resultStorage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoiningSyntaxRule that = (JoiningSyntaxRule) o;
        return group1.equals(that.group1) && group2.equals(that.group2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group1, group2);
    }

    @Override
    public String toString() {
        return "JoiningSyntaxRule{" +
                "group1='" + group1 + '\'' +
                ", group2='" + group2 + '\'' +
                '}';
    }
}
