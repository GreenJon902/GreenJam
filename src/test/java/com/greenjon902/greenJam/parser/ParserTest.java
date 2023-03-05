package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.instructionHandler.StandardInstructionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.greenjon902.greenJam.parser.ParserTestResources.getPack;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
	@Test
	void testSampleScript() {
		SyntaxContext syntaxContext = new SyntaxContext();
		StandardInstructionHandler instructionHandler = new StandardInstructionHandler(syntaxContext);
		Parser parser = new Parser(instructionHandler, syntaxContext);

		StringInputStream string = new StringInputStream("<TestFile.jam>",
				"""
						;; SYNTAX IGNORED ADD "\\n";
						
						;; SYNTAX RULE ADD character `a`;
						;; SYNTAX RULE ADD character `f`;
						;; SYNTAX RULE ADD character `o`;
						
						;; SYNTAX RULE ADD word REPEATING character;
						;; SYNTAX RULE ADD identifier `<[word]>`;
						
						;; SYNTAX RULE ADD keyword `<0public>0 `;
						;; SYNTAX RULE ADD keyword `<0private>0 `;
						;; SYNTAX RULE ADD keyword `<0static>0 `;
						;; SYNTAX RULE ADD keywords REPEATING keyword;
						
						;; SYNTAX RULE ADD number `1`;
						;; SYNTAX RULE ADD number `2`;
						;; SYNTAX RULE ADD number `3`;
						;; SYNTAX RULE ADD number `4`;
 
						;; SYNTAX RULE ADD list_item `<[number]>, `;
						;; SYNTAX RULE ADD repeating_list_item REPEATING list_item;
						;; SYNTAX RULE ADD recorded_number `<[number]>`;
						;; SYNTAX RULE ADD last_list_item `{recorded_number}`;
						;; SYNTAX RULE ADD list_contents JOIN repeating_list_item last_list_item;
						;; SYNTAX RULE ADD list `\\{{list_contents}\\}`;
						
						;; SYNTAX RULE ADD variable_declaration `{1keywords}{0identifier}`;
						;; SYNTAX RULE ADD variable_declaration `{identifier}`;
						;; SYNTAX RULE ADD variable_assignment `{0variable_declaration} = {1list}`;
						
						;; ROOT_NODE SET variable_assignment;
						
						public static foo = {1, 2, 3, 4}
						"""
		);
		AstNode node = parser.parse(string);
		System.out.println(node.toString());
		System.out.println(node.format());

		assertTrue(string.isEnd());
	}

	@Test
	void testWithStandardJam() throws IOException {
		SyntaxContext syntaxContext = new SyntaxContext();
		StandardInstructionHandler instructionHandler = new StandardInstructionHandler(syntaxContext);
		Parser parser = new Parser(instructionHandler, syntaxContext);

		StringInputStream pack = StringInputStream.from(getPack("jam.jam"));
		parser.parse(pack);
		printParseResult(parser, "integer_literal", "12");
		printParseResult(parser, "integer_literal", "+12");
		printParseResult(parser, "integer_literal", "-12");
		printParseResult(parser, "identifier", "tes_t43");
		printParseResult(parser, "identifier", "f");
		printParseResult(parser, "identifier", "0hey");
		printParseResult(parser, "variable_declaration", "public String test");
		printParseResult(parser, "variable_declaration", "private static String test");
		printParseResult(parser, "variable_declaration", "String test");
		printParseResult(parser, "variable_setting", "static String test = 5");
		printParseResult(parser, "variable_setting", "test = another_test_var");
		printParseResult(parser, "expression", "foo * bar + 5");
		printParseResult(parser, "expression", "foo->bar * (6 + 3)");
		printParseResult(parser, "variable_setting", "test = test->bar * 6");
		printParseResult(parser, "line", "test = test->bar * 6;");
		printParseResult(parser, "code_block", "{test = test->bar * 6;}");

		printParseResult(parser, "full_function_arguments", "A b");
		printParseResult(parser, "full_function_arguments", "A b, C d");
		printParseResult(parser, "full_function_call_arguments", "a, b");
		printParseResult(parser, "function_call", "foo(bar, baz)");
		printParseResult(parser, "function_declaration",
				"""
						public String foo(Bar a, Baz b) {
							int result = a->calculate(b);
							return String(result);
						}
						""");
	}

	void printParseResult(Parser parser, String root_node, String string) {
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
		System.out.println();
	}
}
