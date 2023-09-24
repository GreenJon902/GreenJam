package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.jetbrains.annotations.NotNull;

/**
 * A keyword that can be used in instruction lang.
 * Note, this is not the same as an {@link com.greenjon902.greenJam.parsers.instructionHandler.InstructionKeyword}.
 */
public enum InstructionLangKeyword implements StatementTokenizerHelper<InstructionLangKeyword>, InstructionLangTreeLeaf {
	FUNCTION("def"), DO("do"), WHILE("while"), VARIABLE("let"), FOR("for"),
	IF("if"), IN("in");

	public final String stringRepr;

	InstructionLangKeyword() {
		this(null);
	}

	InstructionLangKeyword(String stringRepr) {
		if (stringRepr == null) {
			stringRepr = name();
		}
		this.stringRepr = stringRepr;
	}

	/**
	 * See {@link InstructionLangKeyword}.
	 */
	@Override
	public @NotNull Result<InstructionLangKeyword> apply(InputStream inputStream) {
		if (inputStream.consumeIf(stringRepr)) {
			return Result.ok(this);
		}
		return Result.fail();
	}

	public record Declaration(InstructionIdentifier name) implements InstructionLangTreeLeaf {}
}
