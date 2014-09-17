package org.compiler.lex.actions;

import org.compiler.lex.DomainOfDiscurse;
import org.compiler.lex.StateMachine;
import org.compiler.symboltable.Attribute;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolID extends SemanticAction {

    @Override
    public String execute(Character c) {

	String ret = StateMachine.getInstance().getActualString();
	if (!DomainOfDiscurse.palabrasReservadas.contains(ret)) {
	    SymbolTable.getInstance().addSymbol(ret, new Attribute("id"));
	}

	StateMachine.getInstance().cleanString();
	return ret;
    }

}
