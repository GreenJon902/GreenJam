package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;

/**
 * The storage for a for loop.
 *
 * @param name The name to store the values in
 * @param valueGetter Where we get the values to loop through
 * @param codeBlock The actual code block that is looping
 */
public record ForLoop(InstructionIdentifier name, InstructionLangTreeLeaf valueGetter, CodeBlock codeBlock) implements InstructionLangTreeLeaf {
}
