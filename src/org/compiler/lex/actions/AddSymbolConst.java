package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;
import org.compiler.lex.StateMachine;
import org.compiler.symboltable.AttributeConst;
import org.compiler.symboltable.SymbolTable;

public class AddSymbolConst extends SemanticAction {

	@Override
	public String execute(Character c) throws LexicalReaderException {

		String ret = StateMachine.getInstance().getActualString();

		try {
			Long l = Long.parseLong(ret);
			if (l > 65535)
				throw new LexicalReaderException("Numero fuera de rango");
		} catch (NumberFormatException e) {
			throw e;
		}

		SymbolTable.getInstance().addSymbol(ret, new AttributeConst("const",null));

		StateMachine.getInstance().cleanString();
		return ret;
	}

}
