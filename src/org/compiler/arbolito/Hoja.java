package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;

import org.compiler.asm.CodeGenerator;
import org.compiler.asm.FullRegistersException;
import org.compiler.asm.Names;
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
			if ( att.getNombreAsm() == null ) {
				att.setNombreAsm( "_cadena" + CodeGenerator.getContadorCadena() );
					
			}
			String sentencia = "invoke MessageBox, NULL, addr " + att.getNombreAsm() +", addr "+ att.getNombreAsm() +", MB_OK";
			ret.add(sentencia);
		}
		

		if( elemento.contains("-") ) {
			
			String regaux = null;
			try {
				regaux = RegistryManager.getInstance().obtenerRegistro();
			} catch (FullRegistersException e) {
				System.out.println( e.getMessage() );
			}
			RegistryManager.getInstance().ocuparRegistro(regaux);
			
			ret.add( "XOR " + regaux + "," + regaux );
			ret.add( "SUB " + regaux + "," + Names.getName(elemento.substring(1)) );
			
			this.elemento = regaux;
			
		}

		
		return ret;
	}

}
