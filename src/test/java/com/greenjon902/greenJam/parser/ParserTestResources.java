package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

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

    public static class ReturningSyntaxRule extends SyntaxRule {
        private final Object[] returnValue;

        public ReturningSyntaxRule(Object... returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public AstNode match(StringInputStream string, Contexts contexts) {
            return new AstNode(returnValue);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReturningSyntaxRule that = (ReturningSyntaxRule) o;
            return Arrays.equals(returnValue, that.returnValue);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(returnValue);
        }

        @Override
        public String toString() {
            return "ReturningSyntaxRule{" +
                    "returnValue=" + Arrays.toString(returnValue) +
                    '}';
        }

        @Override
        public String format() {
            return toString();
        }

    }

    public static File getPack(String packName) {
        URL url = ParserTestResources.class.getResource("/com/greenjon902/greenJam/syntaxPacks/" + packName);
        try {
            return Paths.get(url.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
