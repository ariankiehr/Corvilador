package org.compiler.symboltable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.compiler.lex.DomainOfDiscurse;

public class SymbolTable {

	private Map<String, Attribute> table;

	private static SymbolTable instance = null;

	public static SymbolTable getInstance() {
		if (instance == null) {
			instance = new SymbolTable();
		}
		return instance;
	}

	private SymbolTable() {
		table = new HashMap<String, Attribute>();
		for (String palabraReservada : DomainOfDiscurse.palabrasReservadas) {
			addSymbol(palabraReservada, new Attribute("Palabra Reservada"));
		}

		for (String simbolo : DomainOfDiscurse.simbolos) {
			addSymbol(simbolo, new Attribute("Simbolo"));
		}

	}

	public void addSymbol(String s, Attribute a) {
		table.put(s, a);
	}

	public Attribute get(String key) {
		return table.get(key);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Attribute>> iter = table.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Attribute> entry = iter.next();
			sb.append(entry.getKey());
			sb.append(" de tipo ");
			sb.append(entry.getValue());
			if (iter.hasNext()) {
				sb.append('\n');
			}

		}
		return sb.toString();

	}

}
