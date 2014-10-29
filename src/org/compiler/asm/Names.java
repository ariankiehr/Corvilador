package org.compiler.asm;

import org.compiler.symboltable.SymbolTable;

public class Names {
	
	public static String getName(String s) {
		try {
			if("id".equals(SymbolTable.getInstance().get(s).getTypeOfToken())) {
				return "_"+s;
			}
		} catch (NullPointerException e) {}

		return s;
	}

}
