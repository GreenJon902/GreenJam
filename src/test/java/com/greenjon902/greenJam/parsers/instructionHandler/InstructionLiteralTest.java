package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionLiteral.STRING;
import static com.greenjon902.greenJam.testUtils.ResultUtils.checkOkAndCorrect;

//TODO: This could probably be compacted, also test names

class InstructionLiteralTest {
	@Test
	public void strings() {
		checkOkAndCorrect(new InstructionLiteral.String("test"), STRING.apply(new StringInputStream("\"test\"")));
		checkOkAndCorrect(new InstructionLiteral.String("powe8ry 07w09)(&£$Y)_( PF((££ PDFHOIAH(&*£U£  "),
				STRING.apply(new StringInputStream("\"powe8ry 07w09)(&£$Y)_( PF((££ PDFHOIAH(&*£U£  \"")));
		Assertions.assertFalse(STRING.apply(new StringInputStream("test")).isOk, "Detected identifier");
		Assertions.assertFalse(STRING.apply(new StringInputStream("`test`")).isOk, "Detected path");
		Assertions.assertFalse(STRING.apply(new StringInputStream("1")).isOk, "Detected int");
	}

	// TODO: String escape codes
}