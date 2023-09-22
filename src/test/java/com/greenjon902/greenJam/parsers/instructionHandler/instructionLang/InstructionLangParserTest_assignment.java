package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
// TODO: Fix test names

class InstructionLangParserTest_assignment {
	@Test
	public void simple() {
		Assertions.assertEquals(
				CodeBlock.of(
				new InstructionOperator.Assignment(
						new InstructionIdentifier("foo"),
						new InstructionIdentifier("bar")
				)),
				InstructionLangParser.parse(List.of(
						START_CODE_BLOCK,
							new InstructionIdentifier("foo"), ASSIGNMENT,
								new InstructionIdentifier("bar"), END_LINE,
						END_CODE_BLOCK
						)
				)
		);
	}

	@Test
	public void withExpression() {
		Assertions.assertEquals(
				CodeBlock.of(
						new InstructionOperator.Assignment(
								new InstructionIdentifier("foo"),
								new ExpressionOperator(
										new InstructionIdentifier("bar"),
										new InstructionIdentifier("baz"),
										ExpressionOperation.ADD)
						)),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
									new InstructionIdentifier("foo"), ASSIGNMENT,
										new InstructionIdentifier("bar"), ADD, new InstructionIdentifier("baz"), END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}

	@Test
	public void stackedAssignment() {
		Assertions.assertEquals(
				CodeBlock.of(
						new InstructionOperator.Assignment(
								new InstructionIdentifier("foo"),
								new InstructionOperator.Assignment(
										new InstructionIdentifier("bar"),
										new InstructionIdentifier("baz")
								))),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
								new InstructionIdentifier("foo"), ASSIGNMENT,
								new InstructionIdentifier("bar"), ASSIGNMENT,
								new InstructionIdentifier("baz"), END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}
}