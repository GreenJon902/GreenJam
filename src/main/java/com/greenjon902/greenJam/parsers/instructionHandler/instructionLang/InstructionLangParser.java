package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.api.exceptions.InstructionLangParserSyntaxError;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import com.greenjon902.greenJam.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;

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
				throw new InstructionLangParserSyntaxError("Expected line end character(s)");
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
		Result<Ret2> result;
		if ((result = tryMatch(tokens, i,
				InstructionIdentifier.class,
				ASSIGNMENT,
				(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) (instructionLangTreeLeaves, integer) ->
						tryParseStatement(instructionLangTreeLeaves, integer))).isOk) {
			Ret2 ret2 = result.unwrap();
			i = ret2.i();
			InstructionLangTreeLeaf[] trees = ret2.trees();
			Assignment assignment = new Assignment((InstructionIdentifier) trees[0], trees[2]);
			return Result.ok(new Ret(assignment, i));

		} else if ((result = tryMatch(tokens, i,
				InstructionIdentifier.class)).isOk) {
			Ret2 ret2 = result.unwrap();
			i = ret2.i();
			InstructionLangTreeLeaf[] trees = ret2.trees();
			return Result.ok(new Ret(trees[0], i));
		}else {
				return Result.fail();
		}
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
