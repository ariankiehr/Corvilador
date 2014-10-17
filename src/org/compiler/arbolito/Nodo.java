package org.compiler.arbolito;


public class Nodo extends NodoConTipo {

	private Arbol hijoIzq;
	private Arbol hijoDer;



	public Nodo(String e, Arbol i, Arbol d, String t){
		super(e,t);
		this.hijoIzq = i;
		this.hijoDer = d;
		
	}

		
	public Arbol getHijo_izq() {
		return hijoIzq;
	}

	public void setHijo_izq(Arbol hijo_izq) {
		this.hijoIzq = hijo_izq;
	}

	public Arbol getHijo_der() {
		return hijoDer;
	}

	public void setHijo_der(Arbol hijo_der) {
		this.hijoDer = hijo_der;
	}
	
	public String toString() {
		return mostrar("-");
	}
	
	public String mostrar(String tab) {
		String newTab = tab + "-";
		return this.getElem() + " " + this.getTipo() + "\n" + tab + this.getHijo_izq().mostrar(newTab) + 
				"\n" + tab + this.getHijo_der().mostrar(newTab);
	}
	
}
