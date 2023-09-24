package org.greenJam.parsers.instructionHandler;

import org.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Test;

class InstructionHandlerTest {
	@Test
	public void test() { // TODO: Check this
		InstructionHandler ih = new InstructionHandler();
		ih.handle(new StringInputStream("STORE abc {\"test\"}"));
	}

}