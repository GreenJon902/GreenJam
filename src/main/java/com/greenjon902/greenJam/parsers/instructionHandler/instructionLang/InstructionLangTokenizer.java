package com.greenjon902.greenJam.parsers.instructionHandler.instructionLang;

import com.greenjon902.greenJam.api.InputStream;
import com.greenjon902.greenJam.api.exceptions.InstructionLangSyntaxError;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionHandlerIgnoredHelper;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionIdentifier;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionLiteral;
import com.greenjon902.greenJam.parsers.instructionHandler.InstructionOperator;
import com.greenjon902.greenJam.parsers.statementParserBase.StatementTokenizerHelper;
import com.greenjon902.greenJam.utils.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The tokenizer for the instructionLang, this works by checking all the token types and attempts to match one of them.
 */
public class InstructionLangTokenizer {
	protected static final Set<StatementTokenizerHelper<? extends InstructionLangTreeLeaf>> tokenizers = new HashSet<>() {{
		addAll(List.of(InstructionLiteral.values()));
		add(InstructionIdentifier.IDENTIFIER);
		addAll(List.of(InstructionOperator.values()));
	}};

	/**
	 * Tokenize a block of InstructionLang from the given input stream, it will tokenize until it exits the outer
	 * code block.
	 * @param inputStream The input stream
	 * @return The tokens
	 */
	public static Result<List<InstructionLangTreeLeaf>> tokenize(InputStream inputStream) {
		int codeBlockDepth = 0; // When this reaches zero, it means we've exited out the last code block (and therefor the InstructionLang)
		List<InstructionLangTreeLeaf> tokens = new ArrayList<>();

		outer:
		do {
			for (StatementTokenizerHelper<? extends InstructionLangTreeLeaf> tokenizer : tokenizers) {
				InstructionHandlerIgnoredHelper.whitespaceSkipper(inputStream); // TODO: Some sort of checking for this maybe? ( somin like string with int with no space)

				Result<? extends InstructionLangTreeLeaf> result = tokenizer.apply(inputStream);
				if (result.isOk) {
					InstructionLangTreeLeaf token = result.unwrap();
					tokens.add(token);

					// Keep track of these so we know when to exit
					if (token == InstructionOperator.START_CODE_BLOCK) {
						codeBlockDepth += 1;
					} else if (token == InstructionOperator.END_CODE_BLOCK) {
						codeBlockDepth -= 1;
					}

					continue outer;
				}
			}

			// If we are here then no tokenizers applied, which is bad. So throw error
			throw new InstructionLangSyntaxError(inputStream);

		} while (codeBlockDepth > 0);

		if (tokens.size() < 2) {
			return Result.fail();
		} else {
			return Result.ok(tokens);
		}
	}
}
