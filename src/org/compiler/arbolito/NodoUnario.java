package org.compiler.arbolito;

public class NodoUnario extends Arbol{

	private Arbol hijo;

	public NodoUnario(String e, Arbol h){
		super(e);
		this.hijo = h;	 
		
	}

	public Arbol getHijo() {
		return hijo;
	}

	public void setHijo(Arbol h) {
		this.hijo = h;
	}

	public String toString() {
		return mostrar("-");
	}
	
	public String mostrar(String tab) {
		String newTab = tab + "-";
		return this.getElem() + " " + "\n" + tab + this.getHijo().mostrar(newTab)  ;
	}
	
	public String getTipo() {
		return null;
	}
	
	
}
