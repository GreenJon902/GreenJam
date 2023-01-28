package com.greenjon902.greenJam.common;

public class Errors {
    /**
     * Formats and throws syntax errors.
     */
    private static void throwSyntaxError(String message, StringInputStream stringInputStream) throws RuntimeException {
        System.err.println("File \"" + stringInputStream.fileName + "\", line " + stringInputStream.getCurrentLineNumber());
        System.err.println(stringInputStream.getCurrentLine());
        System.err.println(" ".repeat(stringInputStream.getCurrentLinePosition() - 1) + "^");

        throw new RuntimeException(message);
    }

    public static void syntaxTokenizer_invalidGroupCharacter(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Syntax Error: Invalid character \"" + stringInputStream.next() + "\" for a group name" + stringInputStream.location, stringInputStream);
    }

    public static void syntaxTokenizer_unterminatedGroupSubstitution(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Syntax Error: Unterminated group substitution", stringInputStream);
    }

    public static void syntaxTokenizer_invalidEscapeSequence(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Syntax Error: Invalid escape sequence", stringInputStream);
    }
}
