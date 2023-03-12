package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.parser.ParserContext;

public abstract class SyntaxRule implements Printable { // Exists for a few utils and to force certain things upon anything extending
    public static AstNode match(StringInputStream string, String group, Contexts contexts) {
        AstNode ret = null;

        for (SyntaxRule syntaxRule : contexts.syntax.getRules(group)) {
            if (!contexts.parser.alreadyMatching(string.location, syntaxRule)) {
                contexts.parser.matchPush(string.location, syntaxRule);

                AstNode astNode = syntaxRule.match(string, contexts);
                contexts.parser.matchPop();
                if (astNode != null) {
                    ret = astNode;
                    break;
                }
            }
        }
        if (contexts.error.hasErrorAssignationFor(group)) {
            for (String error_group : contexts.error.getErrorAssignations(group)) {
                if (contexts.error.getError(error_group).check(contexts, string, ret)) {
                    Errors.errors_conditionMet(string, contexts.error.getMessage(group, error_group));
                }
            }
        }

        return ret;
    }

    /**
     * Match the next characters in the given string to this rule. This may use other rules inside the parsing.
     * @return The created AstNode or null if it couldn't match it.
     */
    public abstract AstNode match(StringInputStream string, Contexts contexts);

    public AstNode match(String string, Contexts contexts) {
        return match(new StringInputStream("<string>", string), contexts);
    }

    public abstract boolean equals(Object o);

    public static AstNode match(String string, String group, SyntaxContext syntaxContext, ErrorContext errorContext) {
        return match(new StringInputStream("<string>", string), group, new Contexts(syntaxContext, new ParserContext(), errorContext));
    }

    public AstNode match(String string, SyntaxContext syntaxContext, ErrorContext errorContext) {
        return match(string, new Contexts(syntaxContext, new ParserContext(), errorContext));
    }
}