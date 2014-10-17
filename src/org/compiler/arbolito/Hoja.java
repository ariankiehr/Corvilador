package org.compiler.arbolito;

public class Hoja extends NodoConTipo {
	
	public Hoja(String elemento, String tipo ) {
		super(elemento,tipo);
	}
	
	public String toString() {
		return elemento;
	}

		
	@Override
	public String mostrar(String tab) {
		return elemento + " " + this.getTipo();
	}
}
