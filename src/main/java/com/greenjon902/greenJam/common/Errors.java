package com.greenjon902.greenJam.common;

public class Errors {
    /**
     * Formats and throws syntax errors.
     */
    private static void throwSyntaxError(String message, StringInputStream stringInputStream) throws RuntimeException {
        System.err.println("File \"" + stringInputStream.fileName + "\", line " + stringInputStream.getCurrentLineNumber());
        System.err.println(stringInputStream.getCurrentLine());
        System.err.println(" ".repeat(stringInputStream.getCurrentLinePosition() - 1) + "^");

        throw new RuntimeException("Syntax Error: " + message);
    }

    public static void syntaxTokenizer_invalidGroupCharacter(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Invalid character \"" + stringInputStream.next() + "\" for a group name" + stringInputStream.location, stringInputStream);
    }

    public static void syntaxTokenizer_unterminatedGroupSubstitution(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Unterminated group substitution", stringInputStream);
    }

    public static void syntaxTokenizer_invalidEscapeSequence(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Invalid escape sequence", stringInputStream);
    }

    public static void syntaxTokenizer_unrecognisedToken(StringInputStream stringInputStream) {
        throwSyntaxError("Invalid syntax for syntax rule", stringInputStream);
    }

    public static void instructionTokenizer_unrecognisedToken(StringInputStream stringInputStream) {
        throwSyntaxError("Invalid syntax for instruction", stringInputStream);
    }
}
