package com.greenjon902.greenJam;

public class Errors {
    public static void syntaxTokenizer_invalidGroupCharacter(StringInputStream stringInputStream) throws RuntimeException {
        throw new RuntimeException("Syntax Error: Invalid character \"" + stringInputStream.next() + "\" for a group name at location: " + stringInputStream.location);
    }

    public static void syntaxTokenizer_unterminatedGroupSubstitution(StringInputStream stringInputStream) throws RuntimeException {
        throw new RuntimeException("Syntax Error: Unterminated group substitution");
    }
}
