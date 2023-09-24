package org.greenJam.parsers.statementParserBase;

import org.greenJam.api.InputStream;
import org.greenJam.utils.Result;

import java.util.function.Function;

/**
 * A class that can be used by the statement tokenizer. It takes an input stream, and returns the result of type T.
 * @param <T> The type of the result
 */
public interface StatementTokenizerHelper<T> extends Function<InputStream, Result<T>> {
}
