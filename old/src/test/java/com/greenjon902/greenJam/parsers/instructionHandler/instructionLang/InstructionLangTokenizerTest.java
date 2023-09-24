package org.greenJam.parsers.instructionHandler.instructionLang;

import org.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.greenJam.parsers.instructionHandler.InstructionLiteral;
import org.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.greenJam.parsers.instructionHandler.InstructionOperator.*;
import static org.greenJam.parsers.instructionHandler.instructionLang.InstructionLangKeyword.FUNCTION;
import static org.greenJam.testUtils.ResultUtils.checkOkAndListCorrect;

class InstructionLangTokenizerTest {
	@Test
	public void Should_ReturnListOfTokens_When_GivenValidInput_AndSingleSpaces() {
		checkOkAndListCorrect(
				List.of(START_CODE_BLOCK, new InstructionLiteral.String("Testing"), new InstructionIdentifier("remembers_asking"),
						END_LINE, END_CODE_BLOCK), // TODO: Add other types
				InstructionLangTokenizer.tokenize(new StringInputStream("{ \"Testing\" remembers_asking ; }"))
				// Surround all with code blocks to ensure it is all tokenized
		);
	}

	@Test
	public void Should_ReturnListOfTokens_When_GivenValidInput_AndDifferentSpacing() {
		checkOkAndListCorrect(
				List.of(START_CODE_BLOCK, new InstructionLiteral.String("Testing"), new InstructionIdentifier("remembers_asking"),
						END_LINE, FUNCTION, END_CODE_BLOCK), // TODO: Add other types
				InstructionLangTokenizer.tokenize(new StringInputStream("{\"Testing\"\t   remembers_asking \n; def  }   "))
				// Surround all with code blocks to ensure it is all tokenized
		);
	}
}