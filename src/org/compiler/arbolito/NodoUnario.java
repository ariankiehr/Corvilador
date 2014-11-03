package org.compiler.arbolito;

import java.util.List;

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

	public String getTipo() {
		return null;
	}

	@Override
	public String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
	    ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem()+ "\n");

	    ret.append(hijo.mostrar(prefix + (isTail ? "      " : "│     "), true) );
	    return ret.toString();
	}

	@Override
	public List<String> getSentencias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
