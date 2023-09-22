package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
// TODO: Fix test names

class InstructionLangParserTest_codeBlocks {
	@Test
	public void multipleLinesTest() {
		Assertions.assertEquals(
				CodeBlock.of(
						new Assignment(
							new InstructionIdentifier("foo"),
							new InstructionIdentifier("bar")
						),
						new Assignment(
								new InstructionIdentifier("bar"),
								new InstructionIdentifier("baz")
						)),
				InstructionLangParser.parse(List.of(
						START_CODE_BLOCK,
							new InstructionIdentifier("foo"), ASSIGNMENT,
								new InstructionIdentifier("bar"), END_LINE,
							new InstructionIdentifier("bar"), ASSIGNMENT,
								new InstructionIdentifier("baz"), END_LINE,
						END_CODE_BLOCK
						)
				)
		);
	}
}