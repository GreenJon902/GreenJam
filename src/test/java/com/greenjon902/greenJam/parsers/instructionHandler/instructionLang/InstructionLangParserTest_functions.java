package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
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
			if (tokens.get(tokens.size() - 1) == COMMA) tokens.remove(tokens.size() - 1);

			tokens.add(CLOSE_BRACKET);
			tokens.add(END_LINE);
			tokens.add(END_CODE_BLOCK);

			values.add(Arguments.of( // Package args for junit
					n + " args",
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

}