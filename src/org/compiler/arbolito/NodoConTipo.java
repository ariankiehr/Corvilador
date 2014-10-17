package org.compiler.arbolito;

public abstract class NodoConTipo extends Arbol {
	
	protected String tipo;

	public NodoConTipo (String e, String t) {
		super(e);
		this.tipo = t;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	

	
}
