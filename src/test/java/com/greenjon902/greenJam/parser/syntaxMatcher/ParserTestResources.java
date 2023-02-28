package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.ObjectCloner;

public class ParserTestResources {
    public static char[] alphaNumericCharacterList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_".toCharArray();

    public final static String quadraticFormulaString = "((0-b)+(b^2-4*a*c)^(1/2))/(2*a)";
    public final static AstNode quadraticFormulaNodeVer1 = new AstNode("/", // Use versions 1 and 2 depending on how parsing algorithm works
            new AstNode(
                    new AstNode("+",
                            new AstNode(
                                    new AstNode("-",
                                            new AstNode("0"),
                                            new AstNode("b")
                                    )
                            ),
                            new AstNode("^",
                                    new AstNode(
                                            new AstNode("-",
                                                    new AstNode("^",
                                                            new AstNode("b"),
                                                            new AstNode("2")
                                                    ),
                                                    new AstNode("*",
                                                            new AstNode("4"),
                                                            new AstNode("*",
                                                                    new AstNode("a"),
                                                                    new AstNode("c")
                                                            )
                                                    )
                                            )
                                    ),
                                    new AstNode(
                                            new AstNode("/",
                                                    new AstNode("1"),
                                                    new AstNode("2")
                                            )
                                    )
                            )
                    )
            ),
            new AstNode(
                    new AstNode("*",
                            new AstNode("2"),
                            new AstNode("a"))
            )
    );
    public final static AstNode quadraticFormulaNodeVer2 = ObjectCloner.clone(quadraticFormulaNodeVer1);
    static {
        ((AstNode) ((AstNode) ((AstNode) ((AstNode) ((AstNode)
                quadraticFormulaNodeVer2.storage[1]).storage[0]).storage[2]).storage[1]).storage[0]).storage[2] =
                new AstNode("*",
                        new AstNode("*",
                                new AstNode("4"),
                                new AstNode("a")
                        ),
                        new AstNode("c")
                );

    }
}
