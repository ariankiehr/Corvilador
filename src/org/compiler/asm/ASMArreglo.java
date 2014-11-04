package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

import org.compiler.symboltable.AttributeVector;

public class ASMArreglo {
	private static ASMArreglo instance = null;
	private List<String> sentencias; 
	private String elemento;
	
	private ASMArreglo() {
		this.sentencias = new LinkedList<String> ();
	}
		
	

	public static ASMArreglo getInstance() {
			if (instance == null) {
				instance = new ASMArreglo();
			}
			return instance;
		
	}
	
	public String getElemento() {
		return this.elemento;
	}
	
	public List<String> generarArreglo(AttributeVector att) {
		this.sentencias = new LinkedList<String> ();

		/*
		mov ax, 3 ; se quiere acceder a 3 que es el segundo elemento
		cmp ax, 2 ; 2 liminf
		jl indiceFueraRangoInf
		cmp ax, 5 ; 5 limsup
		jg indiceFueraRangoSup
		mov ax, _a+1 ; 3-2 da 1 que es el segundo elemento
		*/

		return this.sentencias;
	}
}
