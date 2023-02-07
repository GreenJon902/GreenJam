package com.greenjon902.greenJam.common;

import java.util.ArrayList;
import java.util.HashMap;

public class SyntaxContext {
    private final HashMap<String, ArrayList<SyntaxRule>> syntaxRules = new HashMap<>();

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
}
