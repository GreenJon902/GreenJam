package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.parser.ParserContext;

public abstract class SyntaxRule implements Printable { // Exists for a few utils and to force certain things upon anything extending
    public static AstNode match(StringInputStream string, String group, SyntaxContext syntaxContext, ParserContext parserContext) {
        AstNode ret = null;

        for (SyntaxRule syntaxRule : syntaxContext.getRules(group)) {
            if (!parserContext.alreadyMatching(string.location, syntaxRule)) {
                parserContext.matchPush(string.location, syntaxRule);

                AstNode astNode = syntaxRule.match(string, syntaxContext, parserContext);
                parserContext.matchPop();
                if (astNode != null) {
                    ret = astNode;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * Match the next characters in the given string to this rule. This may use other rules inside the parsing.
     * @return The created AstNode or null if it couldn't match it.
     */
    public abstract AstNode match(StringInputStream string, SyntaxContext syntaxContext, ParserContext parserContext);

    public AstNode match(String string, SyntaxContext syntaxContext, ParserContext parserContext) {
        return match(new StringInputStream("<string>", string), syntaxContext, parserContext);
    }

    public abstract boolean equals(Object o);

    public static AstNode match(String string, String group, SyntaxContext syntaxContext) {
        return match(new StringInputStream("<string>", string), group, syntaxContext, new ParserContext());
    }

    public AstNode match(String string, SyntaxContext syntaxContext) {
        return match(string, syntaxContext, new ParserContext());
    }
}