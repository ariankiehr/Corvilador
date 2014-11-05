package org.compiler.asm;

import org.compiler.symboltable.SymbolTable;

public class Names {
	
	public static String getReg(String s) {
		if(s!=null && s.contains("[" )) {
			s = s.substring(s.indexOf("+")+1, s.indexOf("]")-1);
			//System.out.println("getreg: " + s);
			return s.trim(); // saco espacios en blanco
		}else if ( SymbolTable.getInstance().get(s)!=null && "id".equals(SymbolTable.getInstance().get(s).getTypeOfToken())) {
			//System.out.println("getreg: " + s);
			return "_"+s;
		}
		//System.out.println("getreg: " + s);
		return s;
	}
	
	public static String getName(String s) {
		if (SymbolTable.getInstance().get(s)!=null && "id".equals(SymbolTable.getInstance().get(s).getTypeOfToken())) {
			//System.out.println("getname: " + s);
			return "_"+s;
		}
		//System.out.println("getname: " + s);
		return s;
	}

}
