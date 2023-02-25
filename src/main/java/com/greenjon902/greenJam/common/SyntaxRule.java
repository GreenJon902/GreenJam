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

    public abstract AstNode match(StringInputStream string, SyntaxContext syntaxContext);

    public abstract boolean equals(Object o);
}