package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.parser.syntaxMatcher.LinkSyntaxRule;

import java.util.ArrayList;
import java.util.HashMap;

public class SyntaxContext {
    private final HashMap<String, ArrayList<SyntaxRule>> syntaxRules = new HashMap<>();
    private final ArrayList<String> ignored = new ArrayList<>();

    public void add(String group, SyntaxRule rule) {
        if (!syntaxRules.containsKey(group)) {
            syntaxRules.put(group, new ArrayList<>());
        }

        syntaxRules.get(group).add(rule);
    }

    public void remove(String group, SyntaxRule rule) {
        syntaxRules.get(group).remove(rule);

        if (syntaxRules.get(group).size() == 0) {
            syntaxRules.remove(group);
        }
    }

    public void addLink(String group, String other_group) {
        add(group, new LinkSyntaxRule(other_group));
    }

    public void removeLink(String group, String other_group) {
        remove(group, new LinkSyntaxRule(other_group));
    }

    public int groupAmount() {
        return syntaxRules.size();
    }

    public String[] getGroupNames() {
        return syntaxRules.keySet().toArray(String[]::new);
    }

    public SyntaxRule[] getRules(String group) {
        return syntaxRules.get(group).toArray(SyntaxRule[]::new);
    }

    public boolean hasGroup(String name) {
        return syntaxRules.containsKey(name);
    }

    public String[] getIgnored() {
        return ignored.toArray(String[]::new);
    }

    public void ignore(String string) {
        ignored.add(string);
    }

    public void removeIgnore(String string) {
        ignored.remove(string);
    }
}
