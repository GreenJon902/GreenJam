package org.greenJam.parsers.instructionHandler;

import org.greenJam.parsers.statementParserBase.StatementParserBase;

import java.util.Arrays;

/**
 * The necessary instructions for the setup of language rules and other pre-processing functionality.
 * @implNote These should only be the necessary instructions, any derivatives (e.g. default values) can be done in Jam.
 */
public class InstructionHandler extends StatementParserBase<Object, Object> {
	public InstructionHandler() {  // Setup pathways and other required information
		super(Object[]::new);
		requireIgnored = true;  // We require spaces between information
		addIgnoreFunction(InstructionHandlerIgnoredHelper::whitespaceSkipper);

		//addPathway(this::print, STORE, IDENTIFIER, LANG);
	}

	private Object print(Object[] objects) {
		System.out.println(Arrays.toString(objects));
		return null;
	}
}
