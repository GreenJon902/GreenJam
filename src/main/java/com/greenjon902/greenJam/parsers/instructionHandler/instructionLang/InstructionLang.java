package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;

import java.util.List;

/**
 * See {@link #apply(InputStream)}.
 */
public enum InstructionLang implements StatementTokenizerHelper<CodeBlock> {
	LANG;

	/**
	 * Parses some InstructionLang, see the docs for information on this.
	 * //TODO: Make these docs
	 *
	 * @param inputStream The input stream to read from
	 * @return The return value (result of last line)
	 */
	@Override
	public Result<CodeBlock> apply(InputStream inputStream) {
		Result<List<InstructionLangTreeLeaf>> result = InstructionLangTokenizer.tokenize(inputStream);
		if (!result.isOk) {
			return Result.fail();
		}
		List<InstructionLangTreeLeaf> tokens = result.unwrap();
		InstructionLangParser.parse(tokens);


		return Result.ok(null);  // TODO: this
	}
}
