package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;

public class DoNothing extends SemanticAction {

	@Override
	public String execute(Character c)  throws LexicalReaderException {
		return null;
	}

}
