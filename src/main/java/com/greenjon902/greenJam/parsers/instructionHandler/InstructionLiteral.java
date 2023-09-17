package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

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
	};

	public record String(java.lang.String value) implements InstructionLangTreeLeaf {};
}
