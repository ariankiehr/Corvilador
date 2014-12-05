package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;
import org.compiler.symboltable.AttributeConTipo;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolConst extends SemanticAction {

	@Override
	public String execute(Character c) throws LexicalReaderException {

		String ret = StateMachine.getInstance().getActualString();
		Long l = null;
		try {
			l = Long.parseLong(ret);
			if (l > 65535)
				throw new LexicalReaderException("Numero fuera de rango");
		} catch (NumberFormatException e) {
			throw e;
		}
		if ( l > 32767 ) {
			SymbolTable.getInstance().addSymbol(ret, new AttributeConTipo("const","entero_ss"));
		} else{
			SymbolTable.getInstance().addSymbol(ret, new AttributeConTipo("const","entero"));
		}
				
		StateMachine.getInstance().cleanString();
		return ret;
	}

}
