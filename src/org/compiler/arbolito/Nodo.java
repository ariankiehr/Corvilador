package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;


import org.compiler.asm.ASMArreglo;
import org.compiler.asm.ASMAsignacion;
import org.compiler.asm.ASMDivision;
import org.compiler.asm.ASMMultiplicacion;
import org.compiler.asm.ASMResta;
import org.compiler.asm.ASMSuma;
import org.compiler.asm.Names;
import org.compiler.asm.RegistryManager;
import org.compiler.symboltable.AttributeVector;
import org.compiler.symboltable.SymbolTable;

public class Nodo extends NodoConTipo {

    private Arbol hijoIzq;
    private Arbol hijoDer;
    private String registro;

    public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

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

    public String mostrar(String prefix, boolean isTail) {
	StringBuilder ret = new StringBuilder();
	ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem()+ "\n");

	ret.append(hijoIzq.mostrar(prefix + (isTail ? "      " : "|     "), false)+ "\n");
	ret.append(hijoDer.mostrar(prefix + (isTail ? "      " : "|     "), true));

	return ret.toString();
    }

	@Override
	public List<String> getSentencias() {
		List<String> ret = new LinkedList<String>();
		
		if( !hijoIzq.isLeaf()) {
			ret.addAll(hijoIzq.getSentencias());
		}
		
		if( !hijoDer.isLeaf()) {
			ret.addAll(hijoDer.getSentencias());
		}
		
		String elemIzq = hijoIzq.getElem();
		String elemDer = hijoDer.getElem();
		
		
		if( "+".equals(elemento) ) {
			ret.addAll( ASMSuma.getInstance().generarSuma(elemIzq, elemDer) );
			this.elemento = ASMSuma.getInstance().getElemento();
			
		} else if ( "*".equals(elemento) ) {
			ret.addAll( ASMMultiplicacion.getInstance().generarMultiplicacion(elemIzq, elemDer) );
			this.elemento = ASMMultiplicacion.getInstance().getElemento();
	
		} else if( "-".equals(elemento) ) {
			ret.addAll(ASMResta.getInstance().generarResta(elemIzq, elemDer));
			this.elemento = ASMResta.getInstance().getElemento();
				
		} 
		
		//DIVISION
		else if( "/".equals(elemento) ) {
			ret.addAll(ASMDivision.getInstance().generarDivision(elemIzq, elemDer));
			this.elemento = ASMDivision.getInstance().getElemento();
			
		} else if( "/".equals(elemento) ) {
			//corivliano
		} else if( "<".equals(elemento) || ">".equals(elemento) || "=".equals(elemento) || "^=".equals(elemento) ||
				"<=".equals(elemento) || ">=".equals(elemento) ) {
			
			//TODO ESTO ESTA JARCODEADO
			ret.add( "MOV AX, " + elemIzq );
			ret.add( "MOV BX, " + elemDer );
				
			ret.add("CMP AX, BX"); 

			
		} else if( ":=".equals(elemento) ) {
			ret.addAll(ASMAsignacion.getInstance().generarAsignacion(elemIzq, elemDer));
			this.elemento = ASMAsignacion.getInstance().getElemento();
			
		} else {
			//es un vector donde el elemento es el id del mismo
			AttributeVector att = (AttributeVector)SymbolTable.getInstance().get(elemento);
			ret.addAll(ASMArreglo.getInstance().generarArreglo(att));
			this.elemento = ASMArreglo.getInstance().getElemento();

		}
		
		
		return ret;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

}
