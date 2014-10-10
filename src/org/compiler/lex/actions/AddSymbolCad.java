package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;
import org.compiler.symboltable.IAttribute;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolCad extends SemanticAction {

	@Override
	public String execute(Character c) throws LexicalReaderException {

		String ret = StateMachine.getInstance().getActualString();
		SymbolTable.getInstance().addSymbol(ret, new IAttribute("cadena"));

		StateMachine.getInstance().cleanString();
		return ret;
	}

}
