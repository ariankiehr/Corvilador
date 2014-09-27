package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;

public class AddChar extends SemanticAction {

	public String execute(Character c) throws LexicalReaderException {
		StateMachine.getInstance().addChar(c);
		return null;
	}

}
