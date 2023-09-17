package com.greenjon902.greenJam.parsers.instructionHandler;

import com.greenjon902.greenJam.api.InputStream;

import java.util.List;

/**
 * Utils for ignoring of things in instructions.
 */
public class InstructionHandlerIgnoredHelper {
	private static final List<String> ignoredChars = List.of(" ", "\t", "\n");

	/**
	 * This skips any whitespace stored in {@link #ignoredChars}.
	 * @param stream The stream to skip from
	 */
	public static void whitespaceSkipper(InputStream stream) {
		while (true) {
			if (ignoredChars.stream().noneMatch(stream::consumeIf)) break;
		}
	}
}
