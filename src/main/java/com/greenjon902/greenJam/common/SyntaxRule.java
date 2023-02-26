package com.greenjon902.greenJam.common;

public abstract class SyntaxRule implements Printable { // Exists for a few utils and to force certain things upon anything extending
    public static AstNode match(StringInputStream string, String group, SyntaxContext syntaxContext) {
        for (SyntaxRule syntaxRule : syntaxContext.getRules(group)) {
            AstNode astNode = syntaxRule.match(string, syntaxContext);
            if (astNode != null) {
                return astNode;
            }
        }
        return null;
    }

    /**
     * Match the next characters in the given string to this rule. This may use other rules inside the parsing.
     * @return The created AstNode or null if it couldn't match it.
     */
    public abstract AstNode match(StringInputStream string, SyntaxContext syntaxContext);

    public AstNode match(String string, SyntaxContext syntaxContext) {
        return match(new StringInputStream("<string>", string), syntaxContext);
    }

    public abstract boolean equals(Object o);
}