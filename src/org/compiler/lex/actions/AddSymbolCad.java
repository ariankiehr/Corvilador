package org.compiler.lex.actions;

import org.compiler.lex.StateMachine;
import org.compiler.symboltable.Attribute;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolCad extends SemanticAction {

    @Override
    public String execute(Character c) {

	String ret = StateMachine.getInstance().getActualString();
	SymbolTable.getInstance().addSymbol(ret, new Attribute("cadena"));

	StateMachine.getInstance().cleanString();
	return ret;
    }

}
