package org.greenJam.parsers.instructionHandler;

import org.greenJam.api.InputStream;
import org.greenJam.parsers.instructionHandler.instructionLang.InstructionLangKeyword;
import org.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import org.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import org.greenJam.utils.Result;
import org.jetbrains.annotations.NotNull;

/**
 * A keyword that can be used in an instruction. We extend function so this can be used by the
 * {@link org.greenJam.parsers.statementParserBase.StatementParserBase}.
 * Note, this is not the same as an {@link InstructionLangKeyword}.
 */
public enum InstructionKeyword implements StatementTokenizerHelper<InstructionKeyword>, InstructionLangTreeLeaf {
	STORE, PRINT;

	public final String stringRepr;

	InstructionKeyword() {
		this(null);
	}

	InstructionKeyword(String stringRepr) {
		if (stringRepr == null) {
			stringRepr = name();
		}
		this.stringRepr = stringRepr;
	}

	/**
	 * See {@link InstructionKeyword}.
	 */
	@Override
	public @NotNull Result<InstructionKeyword> apply(InputStream inputStream) {
		if (inputStream.consumeIf(stringRepr)) {
			return Result.ok(this);
		}
		return Result.fail();
	}
}
