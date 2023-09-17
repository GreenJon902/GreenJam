package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parser information for any identifiers in Instructions or InstructionLang.
 */
public record InstructionIdentifier(@NotNull String name) implements StatementTokenizerHelper<InstructionIdentifier>, InstructionLangTreeLeaf {
	public static final InstructionIdentifier IDENTIFIER = new InstructionIdentifier("There has been an error");
		// So it can act like the other enums

	private static final Set<Character> identifierCharacters = new HashSet<>(List.of(ArrayUtils.toObject(
			"abcdefghijklmnopqrstuvwxyz_1234567890"
					.toCharArray())));

	private static final Set<Character> firstCharacters = new HashSet<>(List.of(ArrayUtils.toObject(
			"abcdefghijklmnopqrstuvwxyz_"
					.toCharArray())));  // Which characters can come first (not numbers)


	@Override
	public @NotNull Result<InstructionIdentifier> apply(InputStream inputStream) {
		Set<Character> set = firstCharacters;

		List<Character> identifier = new ArrayList<>();
		while (inputStream.hasNext(1) && set.contains(inputStream.peek(1).charAt(0))) {
			identifier.add(inputStream.next(1).charAt(0));
			set = identifierCharacters;
		}

		if (identifier.isEmpty()) {
			return Result.fail();
		}
		return Result.ok(new InstructionIdentifier(new String(ArrayUtils.toPrimitive(identifier.toArray(Character[]::new)))));
	}
}
