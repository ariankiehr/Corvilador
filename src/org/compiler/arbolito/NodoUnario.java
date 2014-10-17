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

	public String getTipo() {
		return null;
	}

	@Override
	protected String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
	    ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem()+ "\n");

	    ret.append(hijo.mostrar(prefix + (isTail ? "    " : "│         "), true) );
	    return ret.toString();
	}
	
	
}
