package org.compiler.arbolito;

import java.util.List;

import org.compiler.symboltable.AttributeComun;
import org.compiler.symboltable.SymbolTable;

public abstract class Arbol {
	
	protected String elemento; 
	
	public Arbol( String elemento) {
		super();
		this.elemento = elemento;
	}
	
	
	public AttributeComun getPtrElem() {
		return SymbolTable.getInstance().get(elemento);
	}
	
	public String getElem() {
		return elemento;
	}
	
	public abstract String mostrar(String tab, boolean isTail);
	public abstract String getTipo();
	public abstract List<String> getSentencias();
	public abstract boolean isLeaf();
	
}
