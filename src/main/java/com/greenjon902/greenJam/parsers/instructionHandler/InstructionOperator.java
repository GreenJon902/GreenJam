package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.Item;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

/**
 * Parser information for any operators in Instructions or InstructionLang.
 * Note: these should be safe against being mistaken for other operators (e.g. > and >>).
 */
public enum InstructionOperator implements StatementTokenizerHelper<InstructionOperator>, InstructionLangTreeLeaf {
	START_CODE_BLOCK("{"), END_CODE_BLOCK("}"), END_LINE(";"), OPEN_BRACKET("("), CLOSE_BRACKET(")"),
	LIST_DELIMITER(","), START_GET_ITEM("["), END_GET_ITEM("]"),

	ATTRIBUTE(".", (InstructionLangTreeLeaf a, InstructionLangTreeLeaf b) -> new Attribute(a, (InstructionIdentifier) b), -1),  // This is handled elsewhere
	ASSIGNMENT("=", (InstructionLangTreeLeaf a, InstructionLangTreeLeaf b) -> new Assignment(a, b), 0, true),
	ADD("+", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.ADD), 200),
	SUBTRACT("-", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.SUBTRACT), 100),
	MULTIPLY("*", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.MULTIPLY), 300),
	DIVIDE("/", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.DIVIDE), 400),
	BIT_SHIFT_LEFT("<<", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.BIT_SHIFT_LEFT), 500),
	BIT_SHIFT_RIGHT(">>", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.BIT_SHIFT_RIGHT), 600),
	GREATER_THAN(">", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.GREATER_THAN), 600) {
		@Override
		public Result<InstructionOperator> apply(InputStream inputStream) {  // Protect against being mistaken
			inputStream.push();
			Result<InstructionOperator> res = super.apply(inputStream);
			if (inputStream.consumeIf(">") || inputStream.consumeIf("=")) {
				inputStream.pop();
				return Result.fail();
			} else {
				inputStream.popKeep();
				return res;
			}
		}
	},
	LESS_THAN("<", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.LESS_THAN), 620) {
		@Override
		public Result<InstructionOperator> apply(InputStream inputStream) {  // Protect against being mistaken
			inputStream.push();
			Result<InstructionOperator> res = super.apply(inputStream);
			if (inputStream.consumeIf("<") || inputStream.consumeIf("=")) {
				inputStream.pop();
				return Result.fail();
			} else {
				inputStream.popKeep();
				return res;
			}
		}
	},
	GREATER_EQUALS_THAN(">=", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.GREATER_EQUALS_THAN), 640),
	LESS_EQUALS_THAN("<=", (a, b) -> new ExpressionOperator(a, b, ExpressionOperation.LESS_EQUALS_THAN), 660);

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
	 * An assignment operation, this stores the result from B, into the value A.
	 * @param a The identifier, {@link Item}, {@link Attribute}
	 * @param b The expression
	 */
	public record Assignment(InstructionLangTreeLeaf a, InstructionLangTreeLeaf b) implements Operator {
		public Assignment {
			if (!(a instanceof InstructionIdentifier || a instanceof Item || a instanceof Attribute)) {
				throw new IllegalArgumentException("A must be of type identifier, item or attribute");
			}
		}
	}

	/**
	 * An attribute operation, this gets the attribute B from the result of A.
	 * @param a The identifier
	 * @param b The expression
	 */
	public record Attribute(InstructionLangTreeLeaf a, InstructionIdentifier b) implements Operator {
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
		ADD, SUBTRACT, MULTIPLY, DIVIDE, BIT_SHIFT_LEFT, BIT_SHIFT_RIGHT, GREATER_THAN, LESS_THAN, GREATER_EQUALS_THAN, LESS_EQUALS_THAN;
	}
}
