package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
// TODO: Fix test names

class InstructionLangParserTest_expression {
	@Test
	public void precedenceTest() {
		Assertions.assertEquals(
				CodeBlock.of(
					new ExpressionOperator(
							new ExpressionOperator(
									new InstructionIdentifier("foo"),
									new ExpressionOperator(
											new InstructionIdentifier("bar"),
											new InstructionIdentifier("baz"),
												ExpressionOperation.MULTIPLY
									),
							ExpressionOperation.ADD
							),
					new InstructionIdentifier("bru"),
					ExpressionOperation.SUBTRACT
					)
				),
				InstructionLangParser.parse(List.of(
						START_CODE_BLOCK,
							new InstructionIdentifier("foo"), ADD,
							new InstructionIdentifier("bar"), MULTIPLY,
							new InstructionIdentifier("baz"), SUBTRACT,
							new InstructionIdentifier("bru"), END_LINE,
						END_CODE_BLOCK
						)
				)
		);
	}
}