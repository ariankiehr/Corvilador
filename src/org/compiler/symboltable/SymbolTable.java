package org.compiler.symboltable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.compiler.lex.DomainOfDiscurse;

public class SymbolTable {

	private Map<String, IAttribute> table;

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
		table = new HashMap<String, IAttribute>();
		for (String palabraReservada : DomainOfDiscurse.palabrasReservadas) {
			addSymbol(palabraReservada, new IAttribute("Palabra Reservada"));
		}

		for (String simbolo : DomainOfDiscurse.simbolos) {
			addSymbol(simbolo, new IAttribute("Simbolo"));
		}

	}

	public void addSymbol(String s, IAttribute a) {
		table.put(s, a);
	}

	public IAttribute get(String key) {
		return table.get(key);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, IAttribute>> iter = table.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, IAttribute> entry = iter.next();
			sb.append(entry.getKey());
			sb.append(" de tipo ");
			sb.append(entry.getValue());
			if (iter.hasNext()) {
				sb.append('\n');
			}

		}
		return sb.toString();

	}
	
	public void removeSymbol(String key) {
		this.table.remove(key);
	}

}
