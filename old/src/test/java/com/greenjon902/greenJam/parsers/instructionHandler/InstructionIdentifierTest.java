package org.greenJam.parsers.instructionHandler;

import org.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.greenJam.parsers.instructionHandler.InstructionIdentifier.IDENTIFIER;
import static org.greenJam.testUtils.ResultUtils.checkOkAndCorrect;

//TODO: This could probably be compacted, also test names

class InstructionIdentifierTest {
	@Test
	public void identifiers() {
		checkOkAndCorrect(new InstructionIdentifier("test"), IDENTIFIER.apply(new StringInputStream("test")));
		checkOkAndCorrect(new InstructionIdentifier("_test123"), IDENTIFIER.apply(new StringInputStream("_test123")));
		checkOkAndCorrect(new InstructionIdentifier("hello_howru2_day"), IDENTIFIER.apply(new StringInputStream("hello_howru2_day")));
		Assertions.assertFalse(IDENTIFIER.apply(new StringInputStream("\"test\"")).isOk, "Detected string");
		Assertions.assertFalse(IDENTIFIER.apply(new StringInputStream("`test`")).isOk, "Detected path");
		Assertions.assertFalse(IDENTIFIER.apply(new StringInputStream("12")).isOk, "Detected int");
	}


}