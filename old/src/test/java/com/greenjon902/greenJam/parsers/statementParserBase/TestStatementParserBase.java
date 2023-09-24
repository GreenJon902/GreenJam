package org.greenJam.parsers.statementParserBase;

import org.greenJam.api.InputStream;
import org.greenJam.utils.Result;
import org.greenJam.utils.inputStream.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// TODO: Fix these tests (or at least their names)

public class TestStatementParserBase {
	private Result<String> funcOne(InputStream stream) { // Parse the keyword "TEST"
		if (stream.consumeIf("TEST")) {
			return Result.ok("TEST");
		}
		return Result.fail();
	}

	private Result<String> funcTwo(InputStream stream) { // Parse a 1-digit binary number
		if (stream.consumeIf("0")) {
			return Result.ok( "0");
		} else if (stream.consumeIf("1")) {
			return Result.ok("1");
		} else {
			return Result.fail();
		}
	}

	private void ignoreOne(InputStream stream) { // Ignores one space character
		stream.consumeIf(" ");
	}

	private Object handlerOne(String... objects) { // Returns parsed contents
		return objects;
	}  // Much easier for checkWhileOk if this returns an object

	/**
	 * Test a single pathway with no ignores.
	 */
	@Test
	public void testOnePath() {
		StatementParserBase<String, Object> parser = new StatementParserBase<>(String[]::new);
		parser.addPathway(this::handlerOne, this::funcOne, this::funcTwo);

		checkWhileOk(new Object[] {"TEST", "0"}, parser.handle(new StringInputStream("TEST0")));
		checkWhileOk(new Object[] {"TEST", "1"}, parser.handle(new StringInputStream("TEST1")));
		Assertions.assertFalse(parser.handle(new StringInputStream("TEST2")).isOk);
		Assertions.assertFalse(parser.handle(new StringInputStream("0")).isOk);
		Assertions.assertFalse(parser.handle(new StringInputStream("TEST")).isOk);
	}

	/**
	 * Test a single pathway with an optional, and then required ignore.
	 */
	@Test
	public void testOnePathWithIgnores() {
		StatementParserBase<String, Object> parser = new StatementParserBase<>(String[]::new);
		parser.addPathway(this::handlerOne, this::funcOne, this::funcTwo);

		parser.addIgnoreFunction(this::ignoreOne);

		parser.requireIgnored = false;
		checkWhileOk(new Object[] {"TEST", "0"}, parser.handle(new StringInputStream("TEST0")));
		checkWhileOk(new Object[] {"TEST", "1"}, parser.handle(new StringInputStream("TEST1")));
		checkWhileOk(new Object[] {"TEST", "0"}, parser.handle(new StringInputStream("TEST 0")));
		checkWhileOk(new Object[] {"TEST", "1"}, parser.handle(new StringInputStream("TEST 1")));

		parser.requireIgnored = true;
		checkWhileOk(new Object[] {"TEST", "0"}, parser.handle(new StringInputStream("TEST 0")));
		checkWhileOk(new Object[] {"TEST", "1"}, parser.handle(new StringInputStream("TEST 1")));
		Assertions.assertFalse(parser.handle(new StringInputStream("TEST0")).isOk);
		Assertions.assertFalse(parser.handle(new StringInputStream("TEST1")).isOk);
	}

	/**
	 * Test two patterns, where a specific order is required
	 */
	@Test
	public void testOrderedRoutes() {
		StatementParserBase<String, Object> parser = new StatementParserBase<>(String[]::new);
		parser.addPathway(this::handlerOne, this::funcOne, this::funcTwo, this::funcTwo);
		parser.addPathway(this::handlerOne, this::funcOne, this::funcTwo);

		checkWhileOk(new Object[] {"TEST", "0", "0"}, parser.handle(new StringInputStream("TEST00")));
		checkWhileOk(new Object[] {"TEST", "0", "1"}, parser.handle(new StringInputStream("TEST01")));
		checkWhileOk(new Object[] {"TEST", "1", "0"}, parser.handle(new StringInputStream("TEST10")));
		checkWhileOk(new Object[] {"TEST", "1", "1"}, parser.handle(new StringInputStream("TEST11")));
		checkWhileOk(new Object[] {"TEST", "0"}, parser.handle(new StringInputStream("TEST0")));
		checkWhileOk(new Object[] {"TEST", "1"}, parser.handle(new StringInputStream("TEST1")));
	}

	private void checkWhileOk(Object[] expected, Result<Object> actual) {
		Assertions.assertTrue(actual.isOk, "Was not ok");
		Object a = actual.unwrap();
		Assertions.assertInstanceOf(Object[].class, a, "Was not an array");
		Assertions.assertArrayEquals(expected, (Object[]) a, "Array had wrong contents");
	}
}
