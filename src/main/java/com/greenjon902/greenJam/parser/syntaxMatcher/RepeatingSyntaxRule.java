package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.parser.ParserContext;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Matches a syntax rule multiple times. It then returns an AstNode with multiple values, one for each of the returned
 * notes from matching.
 */
public class RepeatingSyntaxRule extends SyntaxRule {
    public final String group;
    public final int maxLength;
    public final boolean canBeEmpty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepeatingSyntaxRule that = (RepeatingSyntaxRule) o;
        return maxLength == that.maxLength && group.equals(that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, maxLength);
    }

    @Override
    public String toString() {
        return "RepeatingSyntaxRule{" +
                "group='" + group + '\'' +
                ((maxLength > -1) ? (", maxLength=" + maxLength) : "")  +
                '}';
    }

    /**
     * @param group The identifier of the group that repeats.
     * @param maxLength The max amount of repeats, set too -1 for infinite.
     * @param canBeEmpty Can this have nothing inside it?
     */
    public RepeatingSyntaxRule(String group, int maxLength, boolean canBeEmpty) {
        this.group = group;
        this.maxLength = maxLength;
        this.canBeEmpty = canBeEmpty;
    }

    /**
     * See {@link RepeatingSyntaxRule(String, int, boolean)}.
     */
    public RepeatingSyntaxRule(String group, boolean canBeEmpty) {
        this(group, -1, canBeEmpty);
    }

    @Override
    public String format() {
        return toString();
    }

    @Override
    public AstNode match(StringInputStream string, SyntaxContext syntaxContext, ParserContext parserContext) {
        while (string.consumeIfAny(syntaxContext.getIgnored()));

        ArrayList<AstNode> nodes = new ArrayList<>();
        while (maxLength == -1 || nodes.size() < maxLength) {
            AstNode node = SyntaxRule.match(string, group, syntaxContext, parserContext);
            if (node == null) {
                break;
            }
            nodes.add(node);
        }

        if (nodes.size() == 0 && !canBeEmpty) {
            return null;
        } else {
            return new AstNode(nodes.toArray(Object[]::new));
        }
    }
}
