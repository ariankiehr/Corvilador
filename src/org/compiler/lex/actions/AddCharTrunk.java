package org.compiler.lex.actions;

import org.compiler.lex.LexicalAnalyzer;
import org.compiler.lex.StateMachine;

public class AddCharTrunk extends SemanticAction {

	public String execute(Character c) {

		String act = StateMachine.getInstance().getActualString();
		if (act.length() < 12) {
			StateMachine.getInstance().addChar(c);
		} else {
			LexicalAnalyzer.warnings.add("El identificador " + act
					+ " se trunco con caracter " + c);
		}

		return null;
	}

}
