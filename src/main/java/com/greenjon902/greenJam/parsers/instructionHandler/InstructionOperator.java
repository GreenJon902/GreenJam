package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parser information for any operators in Instructions or InstructionLang.
 */
public enum InstructionOperator implements StatementTokenizerHelper<InstructionOperator>, InstructionLangTreeLeaf {
	START_CODE_BLOCK("{"), END_CODE_BLOCK("}"), END_LINE(";"),
	ASSIGNMENT("=", Assignment.class);

	protected final @NotNull String chars;
	protected final @Nullable Class<? extends Operator> treeValueClass;

	InstructionOperator(@NotNull String chars, @Nullable Class<? extends Operator> treeValueClass) {
		this.chars = chars;
		this.treeValueClass = treeValueClass;
	}

	InstructionOperator(@NotNull String chars) {
		this(chars, null);
	}

	@Override
	public Result<InstructionOperator> apply(InputStream inputStream) {
		return inputStream.consumeIf(chars) ? Result.ok(this) : Result.fail();
	}

	/**
	 * The base class of an operator.
	 */
	public interface Operator extends InstructionLangTreeLeaf {

	}

	/**
	 * An assignment operation, this stores the result from B, into the identifier A.
	 * @param a The identifier
	 * @param b The expression
	 */
	public record Assignment(InstructionIdentifier a, InstructionLangTreeLeaf b) implements Operator {
	}
}
