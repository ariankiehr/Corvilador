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
		AttributeVector att = (AttributeVector) SymbolTable.getInstance().get(elem);
		String reg1 = null;

		if (RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) == null) {
			//si no es registro

			try {
				reg1 = RegistryManager.getInstance().obtenerRegistro();
			} catch (FullRegistersException e) {
				System.out.println(e.getMessage());
			}
			RegistryManager.getInstance().ocuparRegistro(reg1);
			sentencias.add("MOV " + reg1 + ", " + Names.getName(elemDer));
		} else {
			//es registro
			reg1 = elemDer;
		}

		// verificamos limites
		// inf

		sentencias.add("CMP " + reg1 + ", " + att.getLimInferior() + " ; Se comparan los limites");
		
		sentencias.add("JL indiceFueraRangoInf");
		
		
		
		// sup

		sentencias.add("CMP " + reg1 + ", " +  att.getLimSuperior());

		sentencias.add("JG indiceFueraRangoSup" + " ; Fin comparacion de limites");
		
		

		String regvec = null;
		if( Names.getReg(reg1).equals(reg1) ) {
			regvec = reg1;
		} else {
			regvec = Names.getReg(reg1);
			sentencias.add("MOV " + regvec + ", " + reg1 );
		}
		
		sentencias.add("SUB "+ Names.getReg(regvec) + ", " + att.getLimInferior() + " ; calcula desplazamiento" ); 

		sentencias.add("AND E" + Names.getReg(regvec) + ", 0000ffffh; limpio la parte alta del registro que utilizo para acceder a la memoria");
		sentencias.add("SHL " + Names.getReg(regvec) + ", 1");
	

		this.elemento = "[ " + Names.getName(elem) + "+" + Names.getReg(regvec) + " ]";

		return this.sentencias;
	}
}
