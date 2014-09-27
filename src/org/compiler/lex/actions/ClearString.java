package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;

public class ClearString extends SemanticAction {
	@Override
	public String execute(Character c)  throws LexicalReaderException {
		StateMachine.getInstance().cleanString();
		return null;
	}
}
