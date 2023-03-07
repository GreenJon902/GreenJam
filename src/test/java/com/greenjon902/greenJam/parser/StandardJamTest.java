package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.instructionHandler.StandardInstructionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.greenjon902.greenJam.parser.ParserTestResources.getPack;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StandardJamTest {
	Parser parser;

	@BeforeEach
	void setup() throws IOException {
		SyntaxContext syntaxContext = new SyntaxContext();
		StandardInstructionHandler instructionHandler = new StandardInstructionHandler(syntaxContext);
		parser = new Parser(instructionHandler, syntaxContext);

		StringInputStream pack = StringInputStream.from(getPack("jam.jam"));
		parser.parse(pack);
	}

	@Test
	void testIntegers() {
		printParseResult(parser, "integer_literal", "12");
		printParseResult(parser, "integer_literal", "+12");
		printParseResult(parser, "integer_literal", "-12");
	}

	@Test
	void testIdentifiers() {
		printParseResult(parser, "identifier", "tes_t43");
		printParseResult(parser, "identifier", "f");
		printParseResult(parser, "identifier", "0hey", true);
	}

	@Test
	void testVariables() {
		printParseResult(parser, "variable_declaration", "public String test");
		printParseResult(parser, "variable_declaration", "private static String test");
		printParseResult(parser, "variable_declaration", "String test");
		printParseResult(parser, "variable_setting", "static String test = 5");
		printParseResult(parser, "variable_setting", "test = another_test_var");
	}

	@Test
	void testExpressions() {
		printParseResult(parser, "expression", "foo * bar + 5");
		printParseResult(parser, "expression", "foo->bar * (6 + 3)");
	}

	@Test
	void testFullLinesAndCodeblocks() {
		printParseResult(parser, "variable_setting", "test = test->bar * 6");
		printParseResult(parser, "line", "test = test->bar * 6;");
		printParseResult(parser, "code_block", "{test = test->bar * 6;}");
	}

	@Test
	void testFunctions() {
		printParseResult(parser, "full_function_arguments", "A b");
		printParseResult(parser, "full_function_arguments", "A b, C d");
		printParseResult(parser, "full_function_call_arguments", "a, b");
		printParseResult(parser, "function_call", "foo(bar, baz)");
		printParseResult(parser, "line", "bar = foo->test(bar, baz);");
		printParseResult(parser, "function_declaration",
				"""
						public String foo(Bar a, Baz b) {
							int result = a->calculator->calculate(b);
							return String(result);
						}
						""");
	}

	void printParseResult(Parser parser, String root_node, String string, boolean shouldBeNull) {
		StringInputStream stringStream = new StringInputStream("<test1>",
				";; ROOT_NODE SET " + root_node + ";" +
						string);
		AstNode result = parser.parse(stringStream);

		System.out.println(root_node);
		System.out.println(string);
		System.out.println(result);
		if (result == null) {
			System.out.println();
		} else {
			System.out.println(result.format());
		}
		System.out.println(stringStream.location + "/" + stringStream.string.length());
		if (shouldBeNull) {
			assertNull(result);
		} else {
			assertNotNull(result);
		}
		System.out.println();
	}

	void printParseResult(Parser parser, String root_node, String string) {
		printParseResult(parser, root_node, string, false);
	}
}
