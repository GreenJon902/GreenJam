package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.api.exceptions.InstructionLangParserSyntaxError;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionLiteral;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator;
import com.greenjon902.greenJam.utils.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
import static com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangKeyword.*;

/**
 * The main parser class for InstructionLang.
 * // TODO: More doc on the internals of this
 */
public class InstructionLangParser {
	/**
	 * Parses a list of tokens into an AST for the instruction lang.
	 * @param tokens The token list
	 * @return The root code block.
	 */
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

	/**
	 * Parses a list of tokens into a code block, this checks for curly braces then runs {@link #parseCodeBlockInner(List, int)}.
	 * */
	private static Ret parseCodeBlock(List<InstructionLangTreeLeaf> tokens, int i) {
		if (tokens.get(i) != START_CODE_BLOCK) {
			throw new InstructionLangParserSyntaxError("Code block must start with with a \"" + START_CODE_BLOCK.chars + "\"");
		}

		Ret ret = parseCodeBlockInner(tokens, i + 1);  // 1 as we skipped the START_CODE_BLOCK
		i = ret.i();

		if (tokens.get(i) != END_CODE_BLOCK) {
			throw new InstructionLangParserSyntaxError("Code block must end with with a \"" + END_CODE_BLOCK.chars + "\"");
		}

		return new Ret(ret.tree(), i + 1);
	}



	/**
	 * Parses the inside of a code block, this consists of a group of lines.
	 */
	private static Ret parseCodeBlockInner(List<InstructionLangTreeLeaf> tokens, int i) {
		List<InstructionLangTreeLeaf> lines = new ArrayList<>();
		InstructionLangTreeLeaf return_value;

		if (tokens.get(i) == END_CODE_BLOCK) {  // empty so just set the return to null line
			return_value = new NullLine();

		} else {
			while (true) {
				Ret ret = parseStatement(tokens, i);
				i = ret.i();

				if (tokens.get(i) == END_CODE_BLOCK) {  // No semi-colon, so return value
					return_value = ret.tree();
					break;

				} else if (tokens.get(i) == END_LINE || tokens.get(i - 1) == END_CODE_BLOCK) {  // We don't need a END_LINE after an e.g. function declaration
					lines.add(ret.tree());
					i += 1;
					if (tokens.get(i) == END_CODE_BLOCK) { // No return value of code block
						return_value = new NullLine();
						break;
					}

				} else {
					throw new InstructionLangParserSyntaxError("Expected " + END_LINE + " or " + END_CODE_BLOCK + ", found " + tokens.get(i));
				}
			}
			// Don't add one to I, as END_CODE_BLOCK is processes elsewhere!
		}

		return new Ret(new CodeBlock(lines, return_value), i);
	}

	/**
	 * Parses a statement, see {@link #tryParseStatement(List, int)}.
	 */
	private static Ret parseStatement(List<InstructionLangTreeLeaf> tokens, int i) {
		Result<Ret> res = tryParseStatement(tokens, i);
		if (!res.isOk) {
			// Something's not been parsed right
			throw new InstructionLangParserSyntaxError("I literrally dont even know whats going on anymore");
		}
		return res.unwrap();
	}

	/**
	 * Tries to parse a statement, this could be an empty line, expression, or a function declaration, etc.
	 */
	private static Result<Ret> tryParseStatement(List<InstructionLangTreeLeaf> tokens, int i) {  // TODO: Fix this mess

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

		} else if ((result2 = tryParseFunctionDeclaration(tokens, i)).isOk) {  // Check for function declaration -------
			Ret ret = result2.unwrap();
			i = ret.i();
			InstructionLangTreeLeaf tree = ret.tree();
			return Result.ok(new Ret(tree, i));

		} else if ((result2 = tryParseDoWhile(tokens, i)).isOk) {  // Check for do while loop -------
			Ret ret = result2.unwrap();
			i = ret.i();
			InstructionLangTreeLeaf tree = ret.tree();
			return Result.ok(new Ret(tree, i));

		} else if ((result2 = tryParseForLoop(tokens, i)).isOk) {  // Check for for loop -------
			Ret ret = result2.unwrap();
			i = ret.i();
			InstructionLangTreeLeaf tree = ret.tree();
			return Result.ok(new Ret(tree, i));

		} else if ((result2 = tryParseVariableDeclaration(tokens, i)).isOk) {  // Check for variable declaration -------
			Ret ret = result2.unwrap();
			i = ret.i();
			InstructionLangTreeLeaf tree = ret.tree();
			return Result.ok(new Ret(tree, i));

		} else if ((result2 = tryParseIf(tokens, i)).isOk) {  // Check for if statement -------
			Ret ret = result2.unwrap();
			i = ret.i();
			InstructionLangTreeLeaf tree = ret.tree();
			return Result.ok(new Ret(tree, i));

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
	 * Attempts to parse a function declaration.
	 */
	private static Result<Ret> tryParseFunctionDeclaration(List<InstructionLangTreeLeaf> tokens, int i) {
		// Parse keyword and name
		Result<Ret2> result;
		if (!(result = tryMatch(tokens, i, FUNCTION, InstructionIdentifier.class, OPEN_BRACKET)).isOk) return Result.fail();
		Ret2 ret = result.unwrap();
		InstructionIdentifier name = (InstructionIdentifier) ret.trees()[1];
		i = ret.i();

		// Parse args
		List<InstructionIdentifier> args = new ArrayList<>();
		while (tokens.get(i) instanceof InstructionIdentifier identifier) {  // Might be no args, or last op may be COMMA
			args.add(identifier);
			i += 1;
			if (tokens.get(i) != LIST_DELIMITER) break;  // We need a comma between args
			i += 1;
		}
		if (tokens.get(i) != CLOSE_BRACKET) return Result.fail();
		i += 1;

		// Parse actual code
		Ret ret2 = parseCodeBlock(tokens, i);
		i = ret2.i();
		CodeBlock codeBlock = (CodeBlock) ret2.tree();

		return Result.ok(new Ret(new FunctionDeclaration(name, args, codeBlock), i));
	}

	/**
	 * Attempts to parse a do while loop.
	 */
	private static Result<Ret> tryParseDoWhile(List<InstructionLangTreeLeaf> tokens, int i) {
		// Parse keyword and name
		Result<Ret2> result;
		if (!(result = tryMatch(tokens, i, DO,
				(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) (tokens1, i1) -> Result.ok(parseCodeBlock(tokens1, i1)),
				WHILE, OPEN_BRACKET,
				(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) InstructionLangParser::tryParseStatement,
				CLOSE_BRACKET
		)).isOk) return Result.fail();
		Ret2 ret = result.unwrap();

		CodeBlock codeBlock = (CodeBlock) ret.trees()[1];
		InstructionLangTreeLeaf statement = ret.trees()[4];
		i = ret.i();

		return Result.ok(new Ret(new Loop(statement, codeBlock, false), i));
	}

	/**
	 * Attempts to parse a for loop.
	 */
	private static Result<Ret> tryParseForLoop(List<InstructionLangTreeLeaf> tokens, int i) {
		// Parse keyword and name
		Result<Ret2> result;
		if (!(result = tryMatch(tokens, i, FOR,
				InstructionIdentifier.class,
				IN, (BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) InstructionLangParser::tryParseStatement,
				(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) (tokens1, i1) -> Result.ok(parseCodeBlock(tokens1, i1))
		)).isOk) return Result.fail();
		Ret2 ret = result.unwrap();

		InstructionIdentifier name = (InstructionIdentifier) ret.trees()[1];
		InstructionLangTreeLeaf valueGetter = ret.trees()[3];
		CodeBlock codeBlock = (CodeBlock) ret.trees()[4];
		i = ret.i();

		return Result.ok(new Ret(new ForLoop(name, valueGetter, codeBlock), i));
	}

	/**
	 * Attempts to parse a for loop.
	 */
	private static Result<Ret> tryParseIf(List<InstructionLangTreeLeaf> tokens, int i) {
		// Parse keyword and name
		Result<Ret2> result;
		if (!(result = tryMatch(tokens, i, IF,
				OPEN_BRACKET,
				(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) InstructionLangParser::tryParseStatement,
				CLOSE_BRACKET,
				(BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) (tokens1, i1) -> Result.ok(parseCodeBlock(tokens1, i1))
		)).isOk) return Result.fail();
		Ret2 ret = result.unwrap();

		CodeBlock codeBlock = (CodeBlock) ret.trees()[4];
		InstructionLangTreeLeaf condition = ret.trees()[2];
		i = ret.i();

		return Result.ok(new Ret(new If(List.of(condition), List.of(codeBlock)), i));  // TODO: Other branches
	}


	/**
	 * Attempts to parse one or multiple variable declarations. If only one is declared then it can also be assigned.
	 */
	private static Result<Ret> tryParseVariableDeclaration(List<InstructionLangTreeLeaf> tokens, int i) {
		if (tokens.get(i) != VARIABLE) {
			return Result.fail();
		}
		i += 1;


		Result<Ret2> res;
		if (tryMatch(tokens, i, InstructionIdentifier.class, LIST_DELIMITER).isOk) {  // E.g. let a, b, c;
			List<Declaration> declarations = new ArrayList<>();
			do {
				declarations.add(new Declaration((InstructionIdentifier) tokens.get(i)));
				i += 2;
			} while (tokens.get(i - 1) == LIST_DELIMITER);
			i -= 1;
			if (tokens.get(i) != CLOSE_BRACKET) return Result.fail();
			i += 1;

			return Result.ok(new Ret(new CodeBlock(declarations, null), i));  // Use a code block as mutliple things we have to do


		} else if ((res = tryMatch(tokens, i, InstructionIdentifier.class, ASSIGNMENT)).isOk) {
			Ret2 ret2 = res.unwrap();
			i = ret2.i();

			Ret ret = parseStatement(tokens, i);
			i = ret.i();

			return Result.ok(new Ret(new CodeBlock(  // Use a code block as mutliple things we have to do
					Collections.singletonList(new Declaration((InstructionIdentifier) ret2.trees()[0])),
					new Assignment(ret2.trees()[0], ret.tree())), i));
		}

		return Result.fail();
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

		// Do any extra logic, or exit if not a primary -----------------
		if (next == OPEN_BRACKET) {
			Result<Ret> res = tryParseExpression(tokens, i);
			if (!res.isOk) return Result.fail();
			Ret ret = res.unwrap();
			i = ret.i();
			if (tokens.get(i) != CLOSE_BRACKET) return Result.fail();
			i += 1;
			next = ret.tree();

		} else if (!(next instanceof InstructionLiteral.Literal || next instanceof InstructionIdentifier)) {  // These assume next to be the primary
			return Result.fail();
		}


		while (true) {
			// See if we can get any attributes -----------------
			Result<Ret2> ret2;
			if ((ret2 = tryMatch(tokens, i, ATTRIBUTE, InstructionIdentifier.class)).isOk) {
				i = ret2.unwrap().i();
				next = ATTRIBUTE.treeValueGenerator.apply(next, ret2.unwrap().trees()[1]);
			}

			// Now try and check for a function call -----------------
			else if (tokens.get(i) == OPEN_BRACKET) {
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
						if (tokens.get(i) != LIST_DELIMITER) return Result.fail();  // Tf is this
						i += 1;  // Consume comma
					}
				}

				if (tokens.get(i) != CLOSE_BRACKET) return Result.fail();
				i += 1;  // Consume bracket

				next = new InstructionLangFunctionCall(next, args.toArray(InstructionLangTreeLeaf[]::new));
			}


			// Now try and check for a get item ---------------------
			else if ((ret2 = tryMatch(tokens, i, START_GET_ITEM, (BiFunction<List<InstructionLangTreeLeaf>, Integer, Result<Ret>>) InstructionLangParser::tryParseStatement, END_GET_ITEM)).isOk) {
				i = ret2.unwrap().i();
				next = new Item(next, ret2.unwrap().trees()[1]);
			}

			// Nothing happened so we can leave now
			else {
				break;
			}
		}

		return Result.ok(new Ret(next, i));
	}

	/**
	 * Attempts to parse an expression, or a singular primary.
	 */
	private static Result<Ret> tryParseExpression(List<InstructionLangTreeLeaf> tokens, int i) {
		Result<Ret> res = tryParsePrimary(tokens, i);
		Ret ret = res.unwrap();
		return tryParseExpressionSecondHalf(tokens, ret.i(), ret.tree(), 0);  // 0 will stop any N/A operators
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
	 * &nbsp;BiFunction< List< InstructionLangTreeLeaf>, Integer, Result< Ret>> - The function is run.<br>
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

/**
 * A record to help return multiple values.
 * @param tree The tree that was got
 * @param i The new index
 */
record Ret(InstructionLangTreeLeaf tree, int i) {}

/**
 * A record to help return multiple values.
 * @param trees The trees that were got
 * @param i The new index
 */
record Ret2(InstructionLangTreeLeaf[] trees, int i) {}
