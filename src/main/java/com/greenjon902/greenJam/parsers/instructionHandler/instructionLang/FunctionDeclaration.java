package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;

/**
 * The storage for a function declaration in instructionLang.
 * The args are set as local variables for the code block to use. The return value is whatever the code block evaluates
 * to.
 *
 * @param name The name of the function being declared
 * @param args The arguments that can be supplied to code block
 * @param codeBlock The code block to run
 */
public record FunctionDeclaration(InstructionIdentifier name, InstructionIdentifier[] args, CodeBlock codeBlock)
		implements InstructionLangTreeLeaf {

}
