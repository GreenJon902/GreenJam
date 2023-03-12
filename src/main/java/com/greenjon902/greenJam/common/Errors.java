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

    /**
     * Formats and throws syntax errors for instructions.
     */
    private static void throwInstructionSyntaxError(String message, InstructionTokenInputStream instructionTokenInputStream) throws RuntimeException {
        System.err.println("File \"" + instructionTokenInputStream.fileName + "\", line " + instructionTokenInputStream.lineNumber);
        System.err.println(instructionTokenInputStream.format());
        System.err.println(" ".repeat(instructionTokenInputStream.getStringPosition()) +
                "^".repeat(instructionTokenInputStream.getStringLength()));

        throw new RuntimeException("Syntax Error: " + message);
    }

    /**
     * Formats and throws syntax matcher errors.
     */
    private static void throwSyntaxMatcherError(String message, StringInputStream stringInputStream, SyntaxRule syntaxRule) throws RuntimeException {
        System.err.println("File \"" + stringInputStream.fileName + "\", line " + stringInputStream.getCurrentLineNumber());
        System.err.println(stringInputStream.getCurrentLine());
        System.err.println(" ".repeat(stringInputStream.getCurrentLinePosition() - 1) + "^");

        throw new RuntimeException("Syntax Matcher Error: " + message);
    }

    /**
     * Formats and throws syntax matcher errors.
     */
    private static void throwParserError(String message, StringInputStream stringInputStream) throws RuntimeException {
        System.err.println("File \"" + stringInputStream.fileName + "\", line " + stringInputStream.getCurrentLineNumber());
        System.err.println(stringInputStream.getCurrentLine());
        System.err.println(" ".repeat(stringInputStream.getCurrentLinePosition() - 1) + "^");

        throw new RuntimeException("Parser Error: " + message);
    }

    public static void syntaxTokenizer_invalidGroupCharacter(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Invalid character \"" + stringInputStream.next() + "\" for a group name" + stringInputStream.location, stringInputStream);
    }

    public static void syntaxTokenizer_unterminatedGroupSubstitution(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Unterminated group substitution", stringInputStream);
    }

    public static void invalidEscapeSequence(StringInputStream stringInputStream) throws RuntimeException {
        throwSyntaxError("Invalid escape sequence", stringInputStream);
    }

    public static void syntaxTokenizer_unrecognisedToken(StringInputStream stringInputStream) {
        throwSyntaxError("Invalid syntax for syntax rule", stringInputStream);
    }

    public static void instructionTokenizer_unrecognisedToken(StringInputStream stringInputStream) {
        throwSyntaxError("Invalid syntax for instruction", stringInputStream);
    }

    public static void instructionTokenizer_missingEndOfInstructionCharacter(StringInputStream stringInputStream) {
        throwSyntaxError("Missing end of instruction character", stringInputStream);
    }

    public static void syntaxMatcher_alreadyRecording(StringInputStream stringInputStream, SyntaxRule syntaxRule, int memoryLocation) {
        throwSyntaxMatcherError("Tried to start recording when already recording to " + memoryLocation, stringInputStream, syntaxRule);
    }

    public static void syntaxMatcher_triedToStopRecordingWhenNotRecording(StringInputStream stringInputStream, SyntaxRule syntaxRule, int memoryLocation) {
        throwSyntaxMatcherError("Tried to stop recording to a location that wasn't being recorded to - " + memoryLocation, stringInputStream, syntaxRule);
    }

    public static void syntaxMatcher_unknownGroup(StringInputStream stringInputStream, SyntaxRule syntaxRule, String group) {
        throwSyntaxMatcherError("Tried to match unknown group - \"" + group + "\"", stringInputStream, syntaxRule);
    }

    public static void syntaxMatcher_triedToRerecordNode(StringInputStream stringInputStream, SyntaxRule syntaxRule, int memoryLocation, String group) {
        throwSyntaxMatcherError("Tried to rerecord a node at the location " + memoryLocation + " from group \"" + group + "\"", stringInputStream, syntaxRule);
    }

    public static void errors_conditionMet(StringInputStream stringInputStream, String message) {
        throwSyntaxError(message, stringInputStream);
    }

    public static void parser_noRootGroup(StringInputStream stringInputStream) {
        throwParserError("No root group has been set", stringInputStream);
    }

    public static void parser_invalidCommand(StringInputStream stringInputStream) {
        throwParserError("Could not recognise command", stringInputStream);
    }

    public static void errorConditions_conditionAlreadyExists(String group) {
        throw new RuntimeException("Tried to add an error condition that already exists - \"" + group + "\"");
    }
}
