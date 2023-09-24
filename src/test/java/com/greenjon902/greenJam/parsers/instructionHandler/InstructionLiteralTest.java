package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.greenjon902.greenJam.parsers.instructionHandler.InstructionLiteral.INTEGER;
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

	@Test
	public void integers() {
		checkOkAndCorrect(new InstructionLiteral.Integer(0), INTEGER.apply(new StringInputStream("0")));
		checkOkAndCorrect(new InstructionLiteral.Integer(245),
				INTEGER.apply(new StringInputStream("245")));
		Assertions.assertFalse(INTEGER.apply(new StringInputStream("test")).isOk, "Detected identifier");
		Assertions.assertFalse(INTEGER.apply(new StringInputStream("`test`")).isOk, "Detected path");
		Assertions.assertFalse(INTEGER.apply(new StringInputStream("\"1\"")).isOk, "Detected string");
	}
}