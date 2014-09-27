package org.compiler.lex.actions;

import org.compiler.lex.StateMachine;

public class ClearString extends SemanticAction {
	@Override
	public String execute(Character c) {
		StateMachine.getInstance().cleanString();
		return null;
	}
}
