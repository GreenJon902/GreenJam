package org.greenJam.parsers.instructionHandler;

import org.greenJam.api.InputStream;

import java.util.List;

// TODO: Tests for these

/**
 * Utils for ignoring of things in instructions.
 */
public class InstructionHandlerIgnoredHelper {
	private static final List<String> ignoredChars = List.of(" ", "\t", "\n");
	private static final String commentStart = "//";
	private static final String commentEnd = "\n";

	/**
	 * This skips any whitespace stored in {@link #ignoredChars}.
	 * @param stream The stream to skip from
	 */
	public static void whitespaceSkipper(InputStream stream) {
		while (true) {
			if (ignoredChars.stream().noneMatch(stream::consumeIf)) break;
		}
	}

	public static void commentSkipper(InputStream inputStream) {  // TODO: Block comments
		if (inputStream.consumeIf(commentStart)) {
			while (!inputStream.consumeIf(commentEnd)) {
				inputStream.skip(1);  // End may be multiple chars, but we still need to check every index
			}
		}
	}
}
