package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;




import org.compiler.asm.ASMArreglo;
import org.compiler.asm.ASMAsignacion;
import org.compiler.asm.ASMComparacion;
import org.compiler.asm.ASMDivision;
import org.compiler.asm.ASMMultiplicacion;
import org.compiler.asm.ASMResta;
import org.compiler.asm.ASMSuma;
import org.compiler.asm.RegistryManager;


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
		
		ret.addAll(hijoIzq.getSentencias());
		ret.addAll(hijoDer.getSentencias());
		
		String elemIzq = hijoIzq.getElem();
		String elemDer = hijoDer.getElem();
		
		
		if( "+".equals(elemento) ) {
	//		System.out.println("antes suma: " + RegistryManager.getInstance().toString());
			ret.addAll( ASMSuma.getInstance().generarSuma(elemIzq, elemDer) );
			this.elemento = ASMSuma.getInstance().getElemento();
	//		System.out.println("despues suma: " + RegistryManager.getInstance().toString());
			
		} else if ( "*".equals(elemento) ) {
			//TODO verificar el overflow si es entero o entero_ss
		
			
			
		//	System.out.println("antes por: " + RegistryManager.getInstance().toString());
			ret.addAll( ASMMultiplicacion.getInstance().generarMultiplicacion(elemIzq, elemDer,this.tipo) );
			this.elemento = ASMMultiplicacion.getInstance().getElemento();
	//		System.out.println("despues por: " + RegistryManager.getInstance().toString());
	
		} else if( "-".equals(elemento) ) {
	//		System.out.println("antes meno: " + RegistryManager.getInstance().toString());
			ret.addAll(ASMResta.getInstance().generarResta(elemIzq, elemDer));
			this.elemento = ASMResta.getInstance().getElemento();
		//	System.out.println("despues menos: " + RegistryManager.getInstance().toString());
				
		} 
		
		//DIVISION
		else if( "/".equals(elemento) ) {
		//	System.out.println("antes div: " + RegistryManager.getInstance().toString());
			ret.addAll(ASMDivision.getInstance().generarDivision(elemIzq, elemDer));
			this.elemento = ASMDivision.getInstance().getElemento();
		//	System.out.println("despues dic: " + RegistryManager.getInstance().toString());

		} else if( "<".equals(elemento) || ">".equals(elemento) || "=".equals(elemento) || "^=".equals(elemento) ||
				"<=".equals(elemento) || ">=".equals(elemento) ) {
	//		System.out.println("antes comp: " + RegistryManager.getInstance().toString());
			ret.addAll(ASMComparacion.getInstance().generarComparacion(elemIzq, elemDer));
			//this.elemento = ASMComparacion.getInstance().getElemento();
		//	System.out.println("despues conp: " + RegistryManager.getInstance().toString());

			
		} else if( ":=".equals(elemento) ) {
		//	System.out.println("antes asig: " + RegistryManager.getInstance().toString());
			ret.addAll(ASMAsignacion.getInstance().generarAsignacion(elemIzq, elemDer));
			this.elemento = ASMAsignacion.getInstance().getElemento();
		//	System.out.println("despues asig: " + RegistryManager.getInstance().toString());
			
		} else {
		//	System.out.println("antes vector: " + RegistryManager.getInstance().toString());
			//es un vector donde el elemento es el id del mismo
			ret.addAll(ASMArreglo.getInstance().generarArreglo(elemento,elemDer));
			this.elemento = ASMArreglo.getInstance().getElemento();
		//	System.out.println("despues vect: " + RegistryManager.getInstance().toString());

		}
		
		
		return ret;
	}


}
