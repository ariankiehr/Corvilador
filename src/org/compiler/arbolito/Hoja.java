package org.compiler.arbolito;

public class Hoja extends NodoConTipo {
	
	public Hoja(String elemento, String tipo ) {
		super(elemento,tipo);
	}
	
	public String toString() {
		return elemento;
	}

	
	public String mostrar(String tab) {
		return elemento + " " + this.getTipo();
	}

	@Override
	protected String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
	    ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem() + ":" + this.getTipo());
	    return ret.toString();
	}
}
