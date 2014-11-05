package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;
import org.compiler.symboltable.AttributeCad;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolCad extends SemanticAction {

	@Override
	public String execute(Character c) throws LexicalReaderException {

		String ret = StateMachine.getInstance().getActualString();
		SymbolTable.getInstance().addSymbol(ret, new AttributeCad("cadena",null));

		StateMachine.getInstance().cleanString();
		return ret;
	}

}
