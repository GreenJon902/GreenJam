package org.greenJam.parsers.instructionHandler.instructionLang;

import org.greenJam.parsers.instructionHandler.InstructionIdentifier;

import java.util.Collections;
import java.util.List;

/**
 * The storage for a function declaration in instructionLang.
 * The args are set as local variables for the code block to use. The return value is whatever the code block evaluates
 * to.
 *
 * @param name The name of the function being declared
 * @param args The arguments that can be supplied to code block
 * @param codeBlock The code block to run
 */
public record FunctionDeclaration(InstructionIdentifier name, List<InstructionIdentifier> args, CodeBlock codeBlock)
		implements InstructionLangTreeLeaf {

	public FunctionDeclaration(InstructionIdentifier name, List<InstructionIdentifier> args, CodeBlock codeBlock) {
		this.name = name;
		this.args = Collections.unmodifiableList(args);
		this.codeBlock = codeBlock;
	}
}
