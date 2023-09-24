package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

/**
 * The storage for a loop.
 *
 * @param statement The statement to check if we are still looping
 * @param codeBlock The actual code block that is looping
 * @param checkBefore Should the statement be checked at the start, or should it be checked at the end.
 */
public record Loop(InstructionLangTreeLeaf statement, CodeBlock codeBlock, boolean checkBefore) implements InstructionLangTreeLeaf {
}
