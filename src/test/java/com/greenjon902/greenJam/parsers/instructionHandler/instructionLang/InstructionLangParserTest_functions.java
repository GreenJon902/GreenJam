package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator.*;
// TODO: Fix test names
// TODO: function declaration

class InstructionLangParserTest_functions {
	@Test
	public void noArgCalling() {
		Assertions.assertEquals(
				CodeBlock.of(
						new InstructionLangFunctionCall(
								new InstructionIdentifier("foo")
						)),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
								new InstructionIdentifier("foo"), OPEN_BRACKET, CLOSE_BRACKET, END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}

	@Test
	public void oneArgCalling() {
		Assertions.assertEquals(
				CodeBlock.of(
						new InstructionLangFunctionCall(
								new InstructionIdentifier("foo"),
								new InstructionIdentifier("bar")
						)),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
								new InstructionIdentifier("foo"), OPEN_BRACKET,
									new InstructionIdentifier("bar"),
								CLOSE_BRACKET, END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}

	@Test
	public void multiArgCalling() {
		Assertions.assertEquals(
				CodeBlock.of(
						new InstructionLangFunctionCall(
								new InstructionIdentifier("foo"),
								new InstructionIdentifier("bar"),
								new InstructionIdentifier("baz"),
								new InstructionIdentifier("bro")
						)),
				InstructionLangParser.parse(List.of(
								START_CODE_BLOCK,
								new InstructionIdentifier("foo"), OPEN_BRACKET,
									new InstructionIdentifier("bar"), COMMA,
									new InstructionIdentifier("baz"), COMMA,
									new InstructionIdentifier("bro"),
								CLOSE_BRACKET, END_LINE,
								END_CODE_BLOCK
						)
				)
		);
	}
}