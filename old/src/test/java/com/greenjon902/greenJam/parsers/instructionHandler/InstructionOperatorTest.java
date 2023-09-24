package org.greenJam.parsers.instructionHandler;

import org.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class InstructionOperatorTest {
	@ParameterizedTest(name="[{index}] {0}")
	@MethodSource
	public void operators(String name, boolean isOk, InstructionOperator parser, String data) {
		Assertions.assertEquals(isOk, parser.apply(new StringInputStream(data)).isOk);
	}

	private static Stream<? extends Arguments> operators() {
		List<Arguments> values = new ArrayList<>();

		InstructionOperator[] operators = InstructionOperator.values();
		for (InstructionOperator parser : operators) {
			for (InstructionOperator data : operators) {
				boolean isOk = parser == data;
				String name = "Should_" + (isOk ? "BeOk" : "Fail") + "_When_ParserIs" + parser + "_And_DataIs" + data;
				values.add(Arguments.of(name, isOk, parser, data.chars));
			}
		}

		return values.stream();
	}
}