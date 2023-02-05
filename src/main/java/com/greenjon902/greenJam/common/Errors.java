package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.instructionHandler.InstructionToken;

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

    public static void instructionHandler_invalidTokenAtLocation(InstructionTokenInputStream instructionTokenInputStream) {
        throwInstructionSyntaxError("Invalid syntax of instruction", instructionTokenInputStream);
    }

    public static void instructionHandler_invalidInstructionAmountLeftForType(InstructionTokenInputStream instructionTokenInputStream, String syntax, int expected_left) {
        System.err.println("File \"" + instructionTokenInputStream.fileName + "\", line " + instructionTokenInputStream.lineNumber);
        System.err.println(instructionTokenInputStream.format());
        if (instructionTokenInputStream.instructionTokens.length - instructionTokenInputStream.location > expected_left) { // Too many
            // We want to arrow every item past how far it should be
            instructionTokenInputStream.location += expected_left; // This should never return so can modify any items
            System.err.println(" ".repeat(instructionTokenInputStream.getStringPosition()) +
                    "^".repeat(instructionTokenInputStream.getString().length() - instructionTokenInputStream.getStringPosition()));
        } else { // Too few
            // Arrow after last item
            System.err.println(" ".repeat(instructionTokenInputStream.getString().length()) +
                    "^");
        }


        throw new RuntimeException("Syntax Error: " + "Invalid length of instruction when using a " + syntax + "instruction, expected " + expected_left + " items left");
    }
}
