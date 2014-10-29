package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;

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
	public String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
		ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem() + "\n");

		ret.append(hijoIzq.mostrar(prefix + (isTail ? "    " : "│          "), false));
		ret.append(hijoDer.mostrar(prefix + (isTail ? "    " : "│          "), true)+ "\n");

		
		return ret.toString();
	}


	@Override
	public List<String> getSentencias() {
		List<String> ret = new LinkedList<String>();
		
		ret.addAll(hijoIzq.getSentencias());
		ret.addAll(hijoDer.getSentencias());
		
		if ("sentencia".equals(elemento)) {
			return ret;
		}
		
		return ret;
	}


	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

}
