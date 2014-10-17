package org.compiler.arbolito;

import org.compiler.symboltable.IAttribute;
import org.compiler.symboltable.SymbolTable;

public abstract class Arbol {
	
	protected String elemento;
	
	public Arbol( String elemento) {
		super();
		this.elemento = elemento;
	}
	
	
	public IAttribute getPtrElem() {
		return SymbolTable.getInstance().get(elemento);
	}
	
	public String getElem() {
		return elemento;
	}
	
	protected abstract String mostrar(String tab, boolean isTail);
	public abstract String getTipo();
	
}
