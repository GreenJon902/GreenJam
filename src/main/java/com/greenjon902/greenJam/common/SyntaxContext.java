package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.parser.syntaxMatcher.JoiningSyntaxRule;
import com.greenjon902.greenJam.parser.syntaxMatcher.LinkSyntaxRule;
import com.greenjon902.greenJam.parser.syntaxMatcher.RepeatingSyntaxRule;

import java.util.ArrayList;
import java.util.HashMap;

public class SyntaxContext {
    private final HashMap<String, ArrayList<SyntaxRule>> syntaxRules = new HashMap<>();
    private final ArrayList<String> ignored = new ArrayList<>();
    private String commandStartStartString = ";;";
    private String rootGroup = null;

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

    public void addRepeating(String group, String other_group) {
        add(group, new RepeatingSyntaxRule(other_group, false));
    }

    public void removeRepeating(String group, String other_group) {
        remove(group, new RepeatingSyntaxRule(other_group, false));
    }

    public void addJoin(String group, String other_group1, String other_group2) {
        add(group, new JoiningSyntaxRule(other_group1, other_group2));
    }

    public void removeJoin(String group, String other_group1, String other_group2) {
        remove(group, new JoiningSyntaxRule(other_group1, other_group2));
    }


    public int groupAmount() {
        return syntaxRules.size();
    }

    public String[] getGroupNames() {
        return syntaxRules.keySet().toArray(String[]::new);
    }

    public SyntaxRule[] getRules(String group) {
        ArrayList<SyntaxRule> rules = syntaxRules.get(group);
        if (rules == null) {
            return new SyntaxRule[0];
        }
        return rules.toArray(SyntaxRule[]::new);
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

    public String getCommandStartStartString() {
        return commandStartStartString;
    }

    public void setCommandStartStartString(String commandStartStartString) {
        this.commandStartStartString = commandStartStartString;
    }

    public boolean hasRootGroup() {
        return rootGroup != null;
    }

    /**
     * The root group is the group that it tries to parse when it first comes across a none-instruction.
     * This expects you to have already ran {@link #hasRootGroup()}. Once it has started parsing the root group,
     * changing this value will have no effect.
     */
    public String getRootGroup() {
        if (rootGroup == null) {
            throw new RuntimeException("No root group has been given.");
        }
        return rootGroup;
    }

    public void setRootGroup(String rootGroup) {
        this.rootGroup = rootGroup;
    }
}
