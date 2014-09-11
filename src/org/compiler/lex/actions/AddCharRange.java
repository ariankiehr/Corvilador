package org.compiler.lex.actions;

import org.compiler.lex.StateMachine;

public class AddCharRange extends SemanticAction {

		public String execute(Character c) throws NumberFormatException {
			StateMachine.getInstance().addChar(c);
			try {
				Long l = Long.parseLong( StateMachine.getInstance().getActualString() );
				if(  l > 65535 ) throw new NumberFormatException();
			} catch(NumberFormatException e) {
				throw e;
			}
			return null;
		}
}
