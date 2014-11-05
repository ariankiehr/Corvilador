package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

import org.compiler.symboltable.AttributeVector;
import org.compiler.symboltable.SymbolTable;

public class ASMArreglo {
	private static ASMArreglo instance = null;
	private List<String> sentencias;
	private String elemento;

	private ASMArreglo() {
		this.sentencias = new LinkedList<String>();
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

	public List<String> generarArreglo(String elem, String elemDer) {
		this.sentencias = new LinkedList<String>();
		AttributeVector att = (AttributeVector) SymbolTable.getInstance().get(
				elem);// ver el asm si es la variable del vector
		String reg1 = null;

		//si no es registro
		if (RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) == null) {
			try {
				reg1 = RegistryManager.getInstance().obtenerRegistro();
			} catch (FullRegistersException e) {
				System.out.println(e.getMessage());
			}
			RegistryManager.getInstance().ocuparRegistro(reg1);
			sentencias.add("MOV " + reg1 + ", " + Names.getName(elemDer));
		} else {
			reg1 = elemDer;
		}

		// verificamos limites
		// inf
		String regLimInf = null;
		try {
			regLimInf = RegistryManager.getInstance().obtenerRegistro();
		} catch (FullRegistersException e) {
			System.out.println(e.getMessage());
		}
		RegistryManager.getInstance().ocuparRegistro(regLimInf);
		sentencias.add("MOV " + regLimInf + ", " + att.getLimInferior() + " ; Se comparan los limites");
		sentencias.add("CMP " + reg1 + ", " + regLimInf);
		RegistryManager.getInstance().desocuparRegistro(regLimInf);
		sentencias.add("JL indiceFueraRangoInf");
		// sup
		String regLimSup = null;
		try {
			regLimSup = RegistryManager.getInstance().obtenerRegistro();
		} catch (FullRegistersException e) {
			System.out.println(e.getMessage());
		}
		RegistryManager.getInstance().ocuparRegistro(regLimSup);
		sentencias.add("MOV " + regLimSup + ", " + att.getLimSuperior());
		sentencias.add("CMP " + reg1 + ", " + regLimSup);
		RegistryManager.getInstance().desocuparRegistro(regLimSup);
		sentencias.add("JG indiceFueraRangoSup" + " ; Fin comparacion de limites");

		// calcular posicion
		/*String regLimInfResta = null;
		try {
			regLimInfResta = RegistryManager.getInstance().obtenerRegistro();
		} catch (FullRegistersException e) {
			System.out.println(e.getMessage());
		}
		RegistryManager.getInstance().ocuparRegistro(regLimInfResta);
		sentencias.add("MOV " + regLimInfResta + ", " + att.getLimInferior());*/
		
		String regvec = null;
		if( Names.getReg(reg1).equals(reg1) ) {
			regvec = reg1;
		} else {
			regvec = Names.getReg(reg1);
			sentencias.add("MOV " + regvec + ", " + reg1 );
		}
		
		sentencias.add("SUB "+ Names.getReg(regvec) + ", " + att.getLimInferior() + " ; calcula desplazamiento" ); 
		//sentencias.addAll(ASMResta.getInstance().generarResta(reg1, regLimInfResta));
		//String cantMov = ASMResta.getInstance().getElemento(); //calcula dezplazamiento

		//RegistryManager.getInstance().desocuparRegistro(regLimInfResta);
		// String registroValor =
		// RegistryManager.getInstance().obtenerRegistro();
		// RegistryManager.getInstance().ocuparRegistro(regLimInfResta);
		// sentencias.add("MOV " + );

		// sentencias.addAll(ASMSuma.getInstance().generarSuma(cantMov, elem));
		sentencias.add("AND E" + Names.getReg(regvec) + ", 0000ffffh; limpio la parte alta del registro que utilizo para acceder a la memoria");
		sentencias.add("SHL " + Names.getReg(regvec) + ", 1");
		//sentencias.add("MOV " + reg1 + ", [ " + Names.getName(elem) + "+" + reg1 + " ]"  );

		this.elemento = "[ " + Names.getName(elem) + "+" + Names.getReg(regvec) + " ]";
		/*
		 * mov ax, 3 ; se quiere acceder a 3 que es el segundo elemento cmp ax,
		 * 2 ; 2 liminf jl indiceFueraRangoInf cmp ax, 5 ; 5 limsup jg
		 * indiceFueraRangoSup mov ax, _a+1 ; 3-2 da 1 que es el segundo
		 * elemento
		 */

		return this.sentencias;
	}
}
