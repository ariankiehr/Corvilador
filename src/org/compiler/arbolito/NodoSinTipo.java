package org.compiler.arbolito;

public class NodoSinTipo extends Arbol {
	
	private Arbol hijoIzq;
	private Arbol hijoDer;
	
	public NodoSinTipo(String elemento, Arbol i, Arbol d) {
		super(elemento);
		this.hijoDer = d;
		this.hijoIzq = i;
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
		return mostrar("",true);
	}
	
	public String getTipo() {
		return null;
	}


	@Override
	protected String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
		ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem() + "\n");

		ret.append(hijoIzq.mostrar(prefix + (isTail ? "    " : "│         "), false));
		ret.append(hijoDer.mostrar(prefix + (isTail ? "    " : "│         "), true)+ "\n");

		
		return ret.toString();
	}

}
