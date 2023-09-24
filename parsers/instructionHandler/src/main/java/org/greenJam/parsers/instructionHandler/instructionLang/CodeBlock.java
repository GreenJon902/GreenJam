package org.greenJam.parsers.instructionHandler.instructionLang;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * The storage for a code block in InstructionLang, this is effectively a list of lines, though the last line may be the
 * return expression which computes the value of the code block.
 * @param lines The lines
 * @param returnExpression the return expression
 */
public record CodeBlock(@NotNull List<? extends InstructionLangTreeLeaf> lines, @NotNull InstructionLangTreeLeaf returnExpression) implements InstructionLangTreeLeaf {
	public CodeBlock(List<? extends InstructionLangTreeLeaf> lines, InstructionLangTreeLeaf returnExpression) {
		this.lines = Collections.unmodifiableList(lines);
		this.returnExpression = returnExpression;
	}

	public CodeBlock(InstructionLangTreeLeaf line, InstructionLangTreeLeaf returnExpression) {
		this(List.of(line), returnExpression);
	}

	/**
	 * Creates a code-block from lines, {@link #returnExpression)} is set to {@link NullLine}
	 * @param lines
	 * @return
	 */
	public static CodeBlock of(InstructionLangTreeLeaf... lines) {
		return new CodeBlock(List.of(lines), new NullLine());
	}
}
