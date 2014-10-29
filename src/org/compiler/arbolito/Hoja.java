package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;

import org.compiler.asm.RegistryManager;
import org.compiler.symboltable.AttributeCad;
import org.compiler.symboltable.SymbolTable;

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
	public String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
	    ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem() + ":" + this.getTipo());
	    return ret.toString();
	}

	@Override
	public List<String> getSentencias() {
		List<String> ret = new LinkedList<String>();
		
		if( "imprimir".equals(elemento) ) {
			AttributeCad att = (AttributeCad)SymbolTable.getInstance().get(tipo);
			String sentencia = "invoke MessageBox, NULL, addr " + att.getNombreAsm() +", addr "+ att.getNombreAsm() +", MB_OK";
			ret.add(sentencia);
		}

		
		return ret;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
}
