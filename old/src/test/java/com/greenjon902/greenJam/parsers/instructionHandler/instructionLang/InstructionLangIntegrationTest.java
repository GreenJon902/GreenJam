package org.greenJam.parsers.instructionHandler.instructionLang;

import org.greenJam.parsers.instructionHandler.InstructionIdentifier;
import org.greenJam.utils.Result;
import org.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.greenJam.parsers.instructionHandler.InstructionLiteral.Boolean;
import static org.greenJam.parsers.instructionHandler.InstructionLiteral.Integer;
import static org.greenJam.parsers.instructionHandler.InstructionOperator.*;
import static org.greenJam.parsers.instructionHandler.instructionLang.InstructionLangKeyword.*;
import static org.greenJam.testUtils.ResultUtils.checkOkAndListCorrect;

public class InstructionLangIntegrationTest {
	@ParameterizedTest(name="[{index}] {0}")
	@MethodSource
	public void algorithms(java.lang.String name, java.lang.String code, List<InstructionLangTreeLeaf> expectedTokens, InstructionLangTreeLeaf expectedTree) {
		Result<List<InstructionLangTreeLeaf>> actualTokens = InstructionLangTokenizer.tokenize(new StringInputStream(code));
		checkOkAndListCorrect(expectedTokens, actualTokens);

		CodeBlock actualTree = InstructionLangParser.parse(actualTokens.unwrap());
		Assertions.assertEquals(expectedTree, actualTree);

		// TODO: Check what happens upon running
	}

	/**
	 * Returns a stream with the test and verify data for various algorithms to test the various features of
	 * InstructionLang.
	 */
	private static Stream<? extends Arguments> algorithms() {

		// To make it more readable
		InstructionIdentifier swapped = new InstructionIdentifier("swapped");
		InstructionIdentifier i = new InstructionIdentifier("i");
		InstructionIdentifier range = new InstructionIdentifier("range");
		InstructionIdentifier a = new InstructionIdentifier("a");
		InstructionIdentifier temp = new InstructionIdentifier("temp");
		InstructionIdentifier length = new InstructionIdentifier("length");
		InstructionIdentifier n = new InstructionIdentifier("n");
		ExpressionOperator nMinus1 = new ExpressionOperator(n, new Integer(1), ExpressionOperation.SUBTRACT);
		ExpressionOperator iMinus1 = new ExpressionOperator(i, new Integer(1), ExpressionOperation.SUBTRACT);


		return Stream.of(
				Arguments.of("bubbleSort",
             			"""
						{
							let n = a.length();
							do {
								let swapped = False;
								for i in range(1, n-1) {
									if (a[i-1] > a[i]) {  // This pair is out of order
									
										let temp = a[i - 1];
										a[i-1] = a[i];
										a[i] = temp;
										
										swapped = True;
									}
								}
							} while (swapped);
						}
						""",
						List.of(
								START_CODE_BLOCK,
									VARIABLE, n, ASSIGNMENT, a, ATTRIBUTE, length, OPEN_BRACKET, CLOSE_BRACKET, END_LINE,
									DO, START_CODE_BLOCK,
										VARIABLE, swapped, ASSIGNMENT, new Boolean(false), END_LINE,
										FOR, i, IN, range, OPEN_BRACKET, new Integer(1), LIST_DELIMITER, n, SUBTRACT, new Integer(1), CLOSE_BRACKET, START_CODE_BLOCK,
											IF, OPEN_BRACKET, a, START_GET_ITEM, i, SUBTRACT, new Integer(1), END_GET_ITEM, GREATER_THAN, a, START_GET_ITEM, i, END_GET_ITEM, CLOSE_BRACKET, START_CODE_BLOCK,

												VARIABLE, temp, ASSIGNMENT, a, START_GET_ITEM, i, SUBTRACT, new Integer(1), END_GET_ITEM, END_LINE,
												a, START_GET_ITEM, i, SUBTRACT, new Integer(1), END_GET_ITEM, ASSIGNMENT, a, START_GET_ITEM, i, END_GET_ITEM, END_LINE,
												a, START_GET_ITEM, i, END_GET_ITEM, ASSIGNMENT, temp, END_LINE,

												swapped, ASSIGNMENT, new Boolean(true), END_LINE,

											END_CODE_BLOCK,
										END_CODE_BLOCK,
									END_CODE_BLOCK, WHILE, OPEN_BRACKET, swapped, CLOSE_BRACKET, END_LINE,
								END_CODE_BLOCK
						),
						CodeBlock.of(
								new CodeBlock(
										new Declaration(n),
										new Assignment(n, new InstructionLangFunctionCall(new Attribute(a, length)))
								),
								new Loop(
									swapped,
									new CodeBlock(
											new CodeBlock(
													new Declaration(swapped),
													new Assignment(swapped, new Boolean(false))
											),
											new ForLoop(i, new InstructionLangFunctionCall(range, new Integer(1), nMinus1),
													new CodeBlock(
															Collections.emptyList(),
															new If(
																	List.of(new ExpressionOperator(new Item(a, iMinus1),
																			new Item(a, i),
																			ExpressionOperation.GREATER_THAN)),
																	List.of(CodeBlock.of(
																			new CodeBlock(
																					new Declaration(temp),
																					new Assignment(temp, new Item(a, iMinus1))
																			),
																			new Assignment(new Item(a, iMinus1), new Item(a, i)),
																			new Assignment(new Item(a, i), temp),

																			new Assignment(swapped, new Boolean(true))
																	))
															)
													)
											)
										),
									false
								)
						)
				)
		);
	}
}
