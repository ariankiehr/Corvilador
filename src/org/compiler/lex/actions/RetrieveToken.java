package org.compiler.lex.actions;

import org.compiler.lex.StateMachine;

public class RetrieveToken extends SemanticAction {

	@Override
	public String execute(Character c) {
		String ret = StateMachine.getInstance().getActualString();
		StateMachine.getInstance().cleanString();
		return ret;
	}

}
