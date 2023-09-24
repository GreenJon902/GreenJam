package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import java.util.Collections;
import java.util.List;

/**
 * The storage for an if statement. The number of conditions should be the number of blocks, or the number of
 * blocks - 1. As you can have `if a do b, elif c do e`, or you can have `if a do b, else do e`
 *
 * @param conditions The condition to enter each branch
 * @param blocks The corresponding code blocks.
 */
public record If(List<InstructionLangTreeLeaf> conditions, List<CodeBlock> blocks) implements InstructionLangTreeLeaf {
	public If(List<InstructionLangTreeLeaf> conditions, List<CodeBlock> blocks) {
		if (!(conditions.size() == blocks.size() || conditions.size() == blocks.size() - 1) || conditions.isEmpty()) {
			throw new RuntimeException("Invalid number of conditions or blocks");
		}
		this.conditions = Collections.unmodifiableList(conditions);
		this.blocks = Collections.unmodifiableList(blocks);
	}
}
