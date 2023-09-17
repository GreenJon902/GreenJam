package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Test;

class InstructionHandlerTest {
	@Test
	public void test() { // TODO: Check this
		InstructionHandler ih = new InstructionHandler();
		ih.handle(new StringInputStream("STORE abc {\"test\"}"));
	}

}