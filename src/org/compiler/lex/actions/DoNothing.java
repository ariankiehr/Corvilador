package org.compiler.lex.actions;

public class DoNothing extends SemanticAction {

	@Override
	public String execute(Character c) {
		return null;
	}

}
