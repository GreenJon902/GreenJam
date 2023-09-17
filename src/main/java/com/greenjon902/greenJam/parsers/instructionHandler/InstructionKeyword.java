package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.instructionHandler.instructionLang.InstructionLangTreeLeaf;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;
import org.jetbrains.annotations.NotNull;

/**
 * A keyword that can be used in an instruction. We extend function so this can be used by the
 * {@link com.greenjon902.greenJam.parsers.statementParserBase.StatementParserBase}.
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
