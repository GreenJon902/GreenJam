package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.api.exceptions.InstructionLangParserSyntaxError;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionLiteral;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator;
import com.greenjon902.greenJam.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;

// TODO: Method Doc

/**
 * The main parser class for InstructionLang.
 * // TODO: More doc on the internals of this
 */
public class InstructionLangParser {
	public static CodeBlock parse(List<InstructionLangTreeLeaf> tokens) {
		if (tokens.get(0) != START_CODE_BLOCK) {
			throw new InstructionLangParserSyntaxError("InstructionLang must start with a code block");
		}

		Ret ret = parseCodeBlockInner(tokens, 1);  // 1 as we skipped the START_CODE_BLOCK

		if (tokens.get(ret.i()) != END_CODE_BLOCK) {
			throw new InstructionLangParserSyntaxError("InstructionLang must end with a code block");
		}

		return (CodeBlock) ret.tree();
	}

	// TOOD: Code block normal (checks for curly brackets)
	public static Ret parseCodeBlockInner(List<InstructionLangTreeLeaf> tokens, int i) {
		List<InstructionLangTreeLeaf> lines = new ArrayList<>();
		InstructionLangTreeLeaf return_value;

		while (true) {
			Ret ret = parseStatement(tokens, i);
			i = ret.i();

			if (tokens.get(i) == END_CODE_BLOCK) {  // No semi-colon, so return value
				return_value = ret.tree();
				break;

			} else if (tokens.get(i) == END_LINE) {
				lines.add(ret.tree());
				i += 1;
				if (tokens.get(i) == END_CODE_BLOCK) { // No return value of code block
					return_value = new NullLine();
					break;
				}

			} else {
				throw new InstructionLangParserSyntaxError("Expected " + END_LINE);
			}
		}
		// Don't add one to I, as END_CODE_BLOCK is processes elsewhere!

		return new Ret(new CodeBlock(lines, return_value), i);
	}

	private static Ret parseStatement(List<InstructionLangTreeLeaf> tokens, int i) {
		Result<Ret> res = tryParseStatement(tokens, i);
		if (!res.isOk) {
			// Something's not been parsed right
			throw new InstructionLangParserSyntaxError("I literrally dont even know whats going on anymore");
		}
		return res.unwrap();
	}

	private static Result<Ret> tryParseStatement(List<InstructionLangTreeLeaf> tokens, int i) {

		// Cast now so easier to read
		BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>> tryParseStatement = InstructionLangParser::tryParseStatement;

		// Stuff that is declared during the if expression
		Result<Ret2> result;
		Result<Ret> result2;
		Ret2 ret2;
		InstructionOperator instructionOperator;


		if (tryMatch(tokens, i, END_LINE).isOk) {  // Check for empty line -------------------------------
			i += 1;
			return Result.ok(new Ret(new NullLine(), i));

		} else if ((result2 = tryParseExpression(tokens, i)).isOk) {  // Check for expression ------------------------

			Ret ret = result2.unwrap();
			i = ret.i();
			InstructionLangTreeLeaf tree = ret.tree();
			return Result.ok(new Ret(tree, i));

		} else {  // Fail ------------------------
			return Result.fail();
		}
	}

	/**
	 * Attempts to parse a {@link InstructionIdentifier},
	 * {@link com.greenjon902.greenJam.parsers.instructionHandler.InstructionLiteral}, bracketed expression or a
	 * "-" literal.
	 * Then attempt to parse it as a function.
	 */ // TODO: parse "-" literal
	private static Result<Ret> tryParsePrimary(List<InstructionLangTreeLeaf> tokens, int i) {
		InstructionLangTreeLeaf next = tokens.get(i);
		i += 1;

		// Do any extra logic, or exit if not a primary.
		if (next == OPEN_BRACKET) {
			Result<Ret> res = tryParseExpression(tokens, i);
			if (!res.isOk) return Result.fail();
			Ret ret = res.unwrap();
			i = ret.i();
			if (tokens.get(i) != CLOSE_BRACKET) return Result.fail();
			i += 1;
			next = ret.tree();

		} else if (!(next instanceof InstructionLiteral || next instanceof InstructionIdentifier)) {  // These assume next to be the primary
			return Result.fail();
		}

		if (tokens.get(i) == OPEN_BRACKET) {  // Now try and check for a function call
			i += 1;  // Consume bracket
			List<InstructionLangTreeLeaf> args = new ArrayList<>();

			if (tokens.get(i) != CLOSE_BRACKET) {
				while (true) { // Parse args
					Result<Ret> res = tryParseExpression(tokens, i);
					if (!res.isOk) return Result.fail();
					Ret ret = res.unwrap();
					i = ret.i();
					args.add(ret.tree());

					if (tokens.get(i) == CLOSE_BRACKET) break;
					if (tokens.get(i) != COMMA) return Result.fail();  // Tf is this
					i += 1;  // Consume comma
				}
			}

			if (tokens.get(i) != CLOSE_BRACKET) return Result.fail();
			i += 1;  // Consume bracket

			next = new InstructionLangFunctionCall(next, args.toArray(InstructionLangTreeLeaf[]::new));
		}

		return Result.ok(new Ret(next, i));
	}

	/**
	 * Attempts to parse an expression, or a singular primary.
	 */
	private static Result<Ret> tryParseExpression(List<InstructionLangTreeLeaf> tokens, int i) {
		Result<Ret> res = tryParsePrimary(tokens, i);
		Ret ret = res.unwrap();
		return tryParseExpressionSecondHalf(tokens, ret.i(), ret.tree(), 0);
	}

	/**
	 * Attempts to parse the second half of an expression. See {@link #tryParseExpression(List, int)}.
	 */
	private static Result<Ret> tryParseExpressionSecondHalf(List<InstructionLangTreeLeaf> tokens, int i, InstructionLangTreeLeaf a, int minPrecedence) {
		InstructionLangTreeLeaf lookahead = tokens.get(i);


		while (lookahead instanceof InstructionOperator op && (op.precedence >= minPrecedence || op.rightAssociative)) {
			i += 1;
			Result<Ret> resB = tryParsePrimary(tokens, i);
			Ret retB = resB.unwrap();
			i = retB.i();
			InstructionLangTreeLeaf b = retB.tree();

			lookahead = tokens.get(i);
			while (lookahead instanceof InstructionOperator nextOp &&
					(nextOp.precedence > op.precedence || nextOp.rightAssociative)) {

				Result<Ret> resB2 = tryParseExpressionSecondHalf(tokens, i, b, op.precedence + (nextOp.precedence > op.precedence ? 1 : 0)); // This expression is for the above todo
				Ret retB2 = resB2.unwrap();
				i = retB2.i();
				b = retB2.tree();

				lookahead = tokens.get(i);
			}

			a = op.treeValueGenerator.apply(a, b);
		}

		return Result.ok(new Ret(a, i));
	}

	/**
	 * Tries to match the next tokens, while also checking that the list is big enough for that.
	 * It starts at token i, and then matches the next tokens to the items in toMatch.
	 * <br><br>
	 * Items in toMatch can be these <br>
	 * &nbsp;InstructionLangTreeLeaf - It is matched using `!=`.<br>
	 * &nbsp;BiFunction< List< InstructionLangTreeLeaf>, Integer, Result< Ret>> - The function is run.
	 * &nbsp;Class< ? extends InstructionLangTreeLeaf> - It checks if the token is an instance of that class.
	 *
	 * @param tokens The token list
	 * @param i The current location
	 * @param toMatch The list of things to match
	 * @return An array of {@link Ret}s
	 */
	private static Result<Ret2> tryMatch(List<InstructionLangTreeLeaf> tokens, int i, Object... toMatch) {
		InstructionLangTreeLeaf[] trees = new InstructionLangTreeLeaf[toMatch.length];

		for (int i2=0; i2<toMatch.length; i2++) {
			Object object = toMatch[i2];

			if (object instanceof InstructionLangTreeLeaf instructionLangTreeLeaf) {
				if (tokens.get(i) != instructionLangTreeLeaf) {
					return Result.fail();
				}
				trees[i2] = instructionLangTreeLeaf;
				i += 1;

 			} else if (object instanceof BiFunction<?,?,?> biFunction_) {
				BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>> biFunction =
						(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) biFunction_;
				Result<Ret> res = biFunction.apply(tokens, i);

				if (!res.isOk) {
					return Result.fail();
				}

				Ret ret = res.unwrap();
				trees[i2] = ret.tree();
				i = ret.i();


			} else if (object instanceof Class<?> clazz_) {
				Class<? extends InstructionLangTreeLeaf> clazz = (Class<? extends InstructionLangTreeLeaf>) clazz_;

				if (!clazz.isInstance(tokens.get(i))) {
					return Result.fail();
				}
				trees[i2] = tokens.get(i);
				i += 1;

			} else {
				throw new IllegalArgumentException(object + " is wrong type");
			}
		}
		return Result.ok(new Ret2(trees, i));
	}
}

record Ret(InstructionLangTreeLeaf tree, int i) {}
record Ret2(InstructionLangTreeLeaf[] trees, int i) {}
