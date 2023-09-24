package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: Other literals

/**
 * Parser information for any literals in Instructions or InstructionLang.
 */
public enum InstructionLiteral implements StatementTokenizerHelper<InstructionLangTreeLeaf> {
	STRING {
		@Override
		public Result<InstructionLangTreeLeaf> apply(InputStream inputStream) {
			if (!inputStream.consumeIf("\"")) {
				return Result.fail();
			}

			List<Character> stringLiteral = new ArrayList<>();
			while (!inputStream.consumeIf("\"")) {

				Character next = inputStream.next(1).charAt(0);
				if (next == '\\') {
					throw new RuntimeException("Escape codes have not been implemented yet");
				} else {
					stringLiteral.add(next);
				}
			}

			return Result.ok(new InstructionLiteral.String(new java.lang.String(ArrayUtils.toPrimitive(stringLiteral.toArray(Character[]::new)))));
		}
	},
	INTEGER {
		public final static Map<Character, java.lang.Integer> numbers = Map.of(
				'0', 0,
				'1', 1,
				'2', 2,
				'3', 3,
				'4', 4,
				'5', 5,
				'6', 6,
				'7', 7,
				'8', 8,
				'9', 9
		);

		@Override
		public Result<InstructionLangTreeLeaf> apply(InputStream inputStream) {  // TODO: other bases (e.g. 16)
			int startLocation = inputStream.location();

			int number = 0;
			int base = 10;

			java.lang.Integer digit;
			while (inputStream.hasNext(1) && (digit = numbers.get(inputStream.peek(1).charAt(0))) != null) {
				inputStream.skip(1);
				number *= base;
				number += digit;
			}

			if (inputStream.location() == startLocation) {
				return Result.fail();
			}

			return Result.ok(new Integer(number));
		}
	},
	BOOLEAN {
		@Override
		public Result<InstructionLangTreeLeaf> apply(InputStream inputStream) {  // TODO: other bases (e.g. 16)
			if (inputStream.consumeIf("True")) {
				return Result.ok(new Boolean(true));
			} else if (inputStream.consumeIf("False")) {
				return Result.ok(new Boolean(false));
			} else {
				return Result.fail();
			}
		}
	};

	public interface Literal extends InstructionLangTreeLeaf {};

	public record String(java.lang.String value) implements Literal {}
	public record Integer(int value) implements Literal {}
	public record Boolean(boolean value) implements Literal {}
}
