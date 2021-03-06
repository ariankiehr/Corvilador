package org.compiler.lex.actions;

import org.compiler.lex.DomainOfDiscurse;
import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;
import org.compiler.symboltable.AttributeVariableID;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolID extends SemanticAction {

	@Override
	public String execute(Character c)  throws LexicalReaderException {

		String ret = StateMachine.getInstance().getActualString();
		if (!DomainOfDiscurse.palabrasReservadas.contains(ret)) {
			SymbolTable.getInstance().addSymbol(ret, new AttributeVariableID("id",null,null));
		}

		StateMachine.getInstance().cleanString();
		return ret;
	}

}
