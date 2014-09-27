package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;

public class ResetStateMachine extends SemanticAction {
	@Override
	public String execute(Character c)  throws LexicalReaderException {
		StateMachine s = StateMachine.getInstance();
		s.cleanString();
		s.setState(s.getInicialState());
		return null;
	}
}