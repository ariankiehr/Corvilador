package org.compiler.symboltable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.compiler.lex.DomainOfDiscurse;

public class SymbolTable {

	private Map<String, AttributeComun> table;

	private static SymbolTable instance = null;

	public static SymbolTable getInstance() {
		if (instance == null) {
			instance = new SymbolTable();
		}
		return instance;
	}
	
	public static void reset() {
		instance = null;
	}

	private SymbolTable() {
		table = new HashMap<String, AttributeComun>();
		for (String palabraReservada : DomainOfDiscurse.palabrasReservadas) {
			addSymbol(palabraReservada, new AttributeComun("Palabra Reservada"));
		}

		for (String simbolo : DomainOfDiscurse.simbolos) {
			addSymbol(simbolo, new AttributeComun("Simbolo"));
		}

	}

	public void addSymbol(String s, AttributeComun a) {
		table.put(s, a);
	}

	public AttributeComun get(String key) {
		return table.get(key);
	}
	
	public List<String> getAllKeys() {
		return new LinkedList<String>(table.keySet());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, AttributeComun>> iter = table.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, AttributeComun> entry = iter.next();
			if(!entry.getValue().getTypeOfToken().equals("Simbolo") 
				&& !entry.getValue().getTypeOfToken().equals("Palabra Reservada")) {
				sb.append(entry.getKey());
				sb.append(" de tipo ");
				sb.append(entry.getValue());
				if (iter.hasNext()) {
					sb.append('\n');
				}
			}
		}
		return sb.toString();

	}
	
	public void removeSymbol(String key) {
		this.table.remove(key);
	}

}
