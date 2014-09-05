package org.compiler.symboltable;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    private Map<String, Attribute> table;
    
    public SymbolTable() {
	table = new HashMap<String, Attribute>();
    }
    
    public void addSymbol(String s, Attribute a) {
	table.put( s, a);
    }
    
    public Attribute get(String key) {
	return table.get(key);
    }
    
    
    public static class TSReference {
	String s;
	Attribute a;
	
	public TSReference(String s, Attribute a) {
	    this.s = s;
	    this.a = a;
	}
	
	public boolean isEOF() {
	    return "EOF".equals(s);
	}
	
	@Override
	public String toString() {
	    return this.s +" : "+ a.toString();
	}
    }

}
