package org.greenJam.parsers.instructionHandler.instructionLang;

import org.greenJam.parsers.instructionHandler.InstructionIdentifier;

/**
 * The storage for a for loop.
 *
 * @param name The name to store the values in
 * @param valueGetter Where we get the values to loop through
 * @param codeBlock The actual code block that is looping
 */
public record ForLoop(InstructionIdentifier name, InstructionLangTreeLeaf valueGetter, CodeBlock codeBlock) implements InstructionLangTreeLeaf {
}
