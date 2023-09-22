package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

/**
 * Parser information for any operators in Instructions or InstructionLang.
 */
public enum InstructionOperator implements StatementTokenizerHelper<InstructionOperator>, InstructionLangTreeLeaf {
	START_CODE_BLOCK("{"), END_CODE_BLOCK("}"), END_LINE(";"), OPEN_BRACKET("("), CLOSE_BRACKET(")"),
	ASSIGNMENT("=", (InstructionLangTreeLeaf a, InstructionLangTreeLeaf b) -> new Assignment((InstructionIdentifier) a, b), 0, true),
	ADD("+", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.ADD), 2),
	SUBTRACT("-", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.SUBTRACT), 1),
	MULTIPLY("*", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.MULTIPLY), 3),
	DIVIDE("/", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.DIVIDE), 4),
	BIT_SHIFT_LEFT("<<", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.BIT_SHIFT_LEFT), 5),
	BIT_SHIFT_RIGHT(">>", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.BIT_SHIFT_RIGHT), 6);

	public final @NotNull String chars;

	/**
	 * The function used to make an {@link Operator} of this token type.
	 */
	public final @Nullable BiFunction<InstructionLangTreeLeaf, InstructionLangTreeLeaf, ? extends Operator> treeValueGenerator;
	public final int precedence;  // -1 is N/A
	public boolean rightAssociative;

	InstructionOperator(@NotNull String chars, @Nullable BiFunction<InstructionLangTreeLeaf, InstructionLangTreeLeaf, ? extends Operator> treeValueGenerator, int precedence, boolean rightAssociative) {
		this.chars = chars;
		this.treeValueGenerator = treeValueGenerator;
		this.precedence = precedence;
		this.rightAssociative = rightAssociative;
	}

	InstructionOperator(@NotNull String chars, @Nullable BiFunction<InstructionLangTreeLeaf, InstructionLangTreeLeaf, ? extends Operator> treeValueGenerator, int precedence) {
		this(chars, treeValueGenerator, precedence, false);
	}


		InstructionOperator(@NotNull String chars) {
		this(chars, null, -1, false);
	}

	@Override
	public Result<InstructionOperator> apply(InputStream inputStream) {
		return inputStream.consumeIf(chars) ? Result.ok(this) : Result.fail();
	}

	/**
	 * The base class of an operator. Subclasses are like {@link InstructionOperator}s, however they link one or
	 * more {@link InstructionLangTreeLeaf}s together, e.g. adding.
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

	/**
	 * An operation that is done in an expression, for example addition, or a bit-shift.
	 * @param a The first part
	 * @param b The second part
	 */
	public record ExpressionOperator(InstructionLangTreeLeaf a, InstructionLangTreeLeaf b, ExpressionOperation operation) implements Operator {
	}

	/**
	 * See {@link ExpressionOperator}.
	 */
	public enum ExpressionOperation {
		ADD, SUBTRACT, MULTIPLY, DIVIDE, BIT_SHIFT_LEFT, BIT_SHIFT_RIGHT;
	}
}
