package org.compiler.arbolito;

public class Nodo extends NodoConTipo {

    private Arbol hijoIzq;
    private Arbol hijoDer;

    public Nodo(String e, Arbol i, Arbol d, String t) {
	super(e, t);
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
	return mostrar("", true);
    }

    protected String mostrar(String prefix, boolean isTail) {
	StringBuilder ret = new StringBuilder();
	ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem()+ "\n");

	ret.append(hijoIzq.mostrar(prefix + (isTail ? "    " : "│         "), false)+ "\n");
	ret.append(hijoDer.mostrar(prefix + (isTail ? "    " : "│         "), true)+ "\n");

	return ret.toString();
    }

}
