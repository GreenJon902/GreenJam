package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
import static com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangKeyword.FUNCTION;
// TODO: Fix test names
// TODO: function declaration

class InstructionLangParserTest_functions {
	@ParameterizedTest(name="[{index}] {0}")
	@MethodSource
	public void calling(String name, List<InstructionLangTreeLeaf> tokens, CodeBlock expected) {
		Assertions.assertEquals(expected, InstructionLangParser.parse(tokens));
	}

	private static Stream<? extends Arguments> calling() {
		List<Arguments> values = new ArrayList<>();

		String name = "foo";
		String[] argNames = new String[] {"bar", "baz", "bro"}; // Possible args

		for (int n=0; n<argNames.length + 1; n++) { // Loop for no args, 1 arg, etc

			InstructionIdentifier[] funcCallArgs = new InstructionIdentifier[n]; // Args to be used in InstructionLangFunctionCall

			List<InstructionLangTreeLeaf> tokens = new ArrayList<>(); // Tokens to be parsed
			tokens.add(START_CODE_BLOCK);
			tokens.add(new InstructionIdentifier("foo"));
			tokens.add(OPEN_BRACKET);

			for (int i=0; i<n; i++) {  // Add actual argument information
				funcCallArgs[i] = new InstructionIdentifier(argNames[i]);

				tokens.add(new InstructionIdentifier(argNames[i]));
				tokens.add(COMMA);
			}
			if (tokens.get(tokens.size() - 1) == COMMA) tokens.remove(tokens.size() - 1);  // Remove last COMMA

			tokens.add(CLOSE_BRACKET);
			tokens.add(END_LINE);
			tokens.add(END_CODE_BLOCK);

			values.add(Arguments.of( // Package args for junit
					"Calling with " + n + " args",
					tokens,
					CodeBlock.of(
							new InstructionLangFunctionCall(
									new InstructionIdentifier(name),
									funcCallArgs
							)
					)
			));
		}

		return values.stream();
	}

	@ParameterizedTest(name="[{index}] {0}")
	@MethodSource
	public void declaration(String name, List<InstructionLangTreeLeaf> tokens, CodeBlock expected) {
		Supplier<String> message = () ->
				"Parser failed, info:" + "\n\n" +
						"tokens=" + tokens +
						"\nexpected=" + expected + "\n\n";  // Supplier so only make string when have to
		CodeBlock actual = Assertions.assertDoesNotThrow( // So we can see tokens when it crashes
				() -> InstructionLangParser.parse(tokens),
				message
		);
		Assertions.assertEquals(expected, actual, message);  // And now check the result
	}

	private static Stream<? extends Arguments> declaration() {
		List<Arguments> values = new ArrayList<>();

		InstructionIdentifier name = new InstructionIdentifier("foo");
		String[] paramNames = new String[] {"bar", "baz", "bro"};  // Possible params

		List<InstructionLangTreeLeaf> lineTokens = List.of(new InstructionIdentifier("a"), ASSIGNMENT,
				new InstructionIdentifier("b"), END_LINE);
		InstructionLangTreeLeaf lineTree = new Assignment(new InstructionIdentifier("a"), new InstructionIdentifier("b"));

		List<InstructionLangTreeLeaf> returnLineTokens = List.of(new InstructionIdentifier("a"), MULTIPLY,
				new InstructionIdentifier("b"));  // No END_LINE as return
		InstructionLangTreeLeaf returnLineTree = new ExpressionOperator(new InstructionIdentifier("a"),
				new InstructionIdentifier("b"), ExpressionOperation.MULTIPLY);

		for (int n=0; n<paramNames.length + 1; n++) { // Loop for no params, 1 arg, etc
			for (int lines_id=0; lines_id<4; lines_id++) { // First bit is for line, second if for returnLine

				List<InstructionIdentifier> funcParameters = new ArrayList<>(n); // Params to be used
				List<InstructionLangTreeLeaf> codeBlockLines;
				InstructionLangTreeLeaf codeBlockReturnLine;
				List<InstructionLangTreeLeaf> tokens = new ArrayList<>(); // Tokens to be parsed

				tokens.add(START_CODE_BLOCK);
				tokens.add(FUNCTION);
				tokens.add(name);

				// Params -------------------
				tokens.add(OPEN_BRACKET);
				for (int i=0; i<n; i++) {  // Add actual argument information
					funcParameters.add(new InstructionIdentifier(paramNames[i]));

					tokens.add(new InstructionIdentifier(paramNames[i]));
					tokens.add(COMMA);
				}
				if (tokens.get(tokens.size() - 1) == COMMA) tokens.remove(tokens.size() - 1);  // Remove last COMMA
				tokens.add(CLOSE_BRACKET);

				// Contents -------------------
				tokens.add(START_CODE_BLOCK);
				if ((lines_id & 0b10) != 0) { // Do we have the normal line
					tokens.addAll(lineTokens);
					codeBlockLines = List.of(lineTree);
				} else {
					codeBlockLines = List.of();
				}
				if ((lines_id & 0b01) != 0) { // Do we have the return line
					tokens.addAll(returnLineTokens);
					codeBlockReturnLine = returnLineTree;
				} else {
					codeBlockReturnLine = new NullLine();
				}
				tokens.add(END_CODE_BLOCK);
				tokens.add(END_LINE);  // So the function is not returned (doesn't change anything but makes expected code easier)

				tokens.add(END_CODE_BLOCK);

				values.add(Arguments.of( // Package args for junit
						"Declaring with " + n + " params, line=" + (lines_id & 0b10) + ", returnLine=" + (lines_id & 0b01),
						tokens,
						CodeBlock.of(
								new FunctionDeclaration(
										name,
										funcParameters,
										new CodeBlock(codeBlockLines, codeBlockReturnLine)
								)
						)
				));
			}
		}

		return values.stream();
	}
}