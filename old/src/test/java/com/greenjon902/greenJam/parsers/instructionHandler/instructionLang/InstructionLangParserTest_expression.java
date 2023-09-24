package org.greenJam.parsers.instructionHandler.instructionLang;

import org.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.greenJam.parsers.instructionHandler.InstructionOperator.*;
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

	@Test
	public void bracketTest() {
		Assertions.assertEquals(
				CodeBlock.of(
						new ExpressionOperator(
								new ExpressionOperator(
										new ExpressionOperator(
											new InstructionIdentifier("foo"),
											new InstructionIdentifier("bar"),
												ExpressionOperation.ADD
										),
										new InstructionIdentifier("baz"),
										ExpressionOperation.MULTIPLY
								),
								new InstructionIdentifier("bru"),
								ExpressionOperation.SUBTRACT
						)
				),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
								OPEN_BRACKET,
									new InstructionIdentifier("foo"), ADD,
									new InstructionIdentifier("bar"),
								CLOSE_BRACKET, MULTIPLY,
								new InstructionIdentifier("baz"), SUBTRACT,
								new InstructionIdentifier("bru"), END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}

	/**
	 * Tests using an assignment token inside of expression.<br>
	 * e.g. `a + b = c + d`<br>
	 * which is the same as `a + (b = (c + d))`.
	 */
	@Test
	public void storeInsideExpressionTest() {
		Assertions.assertEquals(
				CodeBlock.of(
						new ExpressionOperator(
								new InstructionIdentifier("foo"),
								new Assignment(
										new InstructionIdentifier("bar"),
										new ExpressionOperator(
												new InstructionIdentifier("baz"),
												new InstructionIdentifier("bru"),
												ExpressionOperation.ADD
										)
								),
								ExpressionOperation.ADD
						)
				),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
								new InstructionIdentifier("foo"), ADD,
								new InstructionIdentifier("bar"), ASSIGNMENT,
								new InstructionIdentifier("baz"), ADD,
								new InstructionIdentifier("bru"), END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}
}