package com.greenjon902.greenJam;

public class Errors {
    /**
     * Formats and throws syntax errors.
     */
    private static void throwSyntaxError(String message, StringInputStream stringInputStream) {
        System.err.println("File \"" + stringInputStream.fileName + "\", line " + stringInputStream.getCurrentLineNumber());
        System.err.println(stringInputStream.getCurrentLine());
        System.err.println(" ".repeat(stringInputStream.getCurrentLinePosition() - 1) + "^");

        RuntimeException exception = new RuntimeException(message);
        throw exception;
    }

    public static void syntaxTokenizer_invalidGroupCharacter(StringInputStream stringInputStream) {
        throwSyntaxError("Syntax Error: Invalid character \"" + stringInputStream.next() + "\" for a group name" + stringInputStream.location, stringInputStream);
    }

    public static void syntaxTokenizer_unterminatedGroupSubstitution(StringInputStream stringInputStream) {
        throwSyntaxError("Syntax Error: Unterminated group substitution", stringInputStream);
    }
}
