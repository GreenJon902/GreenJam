package org.greenJam.parsers.statementParserBase;

import org.greenJam.api.InputStream;
import org.greenJam.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Subclasses of this can be used for easy statement parsing, you can add the formats of your statements, and it will
 * figure out the correct statement to match.
 *
 * @implNote Order of operations may matter, so ensure you add longer items before shorter ones.
 */
public class StatementParserBase<T, R> {
	private final IntFunction<T[]> tGenerator;

	public StatementParserBase(IntFunction<T[]> tGenerator) {
		this.tGenerator = tGenerator;
	}

	protected boolean requireIgnored = false;

	// Indexes for pathways are same as handler, e.g. pathways[0] means handler[0]
	private final ArrayList<StatementTokenizerHelper<T>[]> pathways = new ArrayList<>();
	private final ArrayList<Function<T[], R>> handlers = new ArrayList<>();

	private final List<Consumer<InputStream>> ignoreFunctions = new ArrayList<>();

	/**
	 * Adds a specific format for a statement, the route is an array of functions that can parse the next item, and the
	 * handler is run if it was all parsed successfully.
	 * Route functions should return an {@link Result#isOk ok} result if it is successful, or else it should not be
	 * {@link Result#isOk ok}.
	 * @param route The format of the statement
	 * @param handler The consumer to run if it was matched successfully
	 */
	protected void addPathway(Function<T[], R> handler, StatementTokenizerHelper<T>... route) {
		pathways.add(route);
		handlers.add(handler);
	}

	/**
	 * Adds a function used to ignore certain characters.
	 * @param func A function taking an input stream, and advances the location till after the skipped characters.
	 */
	protected void addIgnoreFunction(Consumer<InputStream> func) {
		ignoreFunctions.add(func);
	}

	/**
	 * Handles an instruction with one of the predefined pathways.
	 */
	public Result<R> handle(InputStream inputStream) {
		for (int pi=0; (pi<pathways.size()); pi++) {
			inputStream.push();

			Function<InputStream, Result<T>>[] route = pathways.get(pi);

			T[] results = tGenerator.apply(route.length);
			boolean failed = false;

			for (int i=0; (i<route.length) && !failed; i++) {

				// Ignore any characters first
				int start = inputStream.location();
				for (Consumer<InputStream> func : ignoreFunctions) {
					func.accept(inputStream);
				}
				if (i != 0 && requireIgnored && start == inputStream.location()) {  // Check if we did ignore one and if it was required, we don't care about the first loop
					failed = true;


				} else {
					// Now try and parse a result
					Result<T> result = route[i].apply(inputStream);
					if (result.isOk) {
						results[i] = result.unwrap();
					} else {
						failed = true;
					}
				}
			}

			if (!failed) {
				return Result.ok(handlers.get(pi).apply(results));
			}

			inputStream.pop();  // Pop after return, so if successful we don't pop
		}

		return Result.fail();
	}
}
