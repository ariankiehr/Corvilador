package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMSuma {
	
	private static ASMSuma instance = null;
	private List<String> sentencias; 
	private String elemento;
	
	private ASMSuma() {
		this.sentencias = new LinkedList<String> ();
	}
		
	

	public static ASMSuma getInstance() {
			if (instance == null) {
				instance = new ASMSuma();
			}
			return instance;
		
	}
	
	public String getElemento() {
		return this.elemento;
	}
	
	public List<String> generarSuma(String elemIzq, String elemDer) {
		this.sentencias = new LinkedList<String> ();
		if ( RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) != null ) {
			//es un registro izq
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				//es registro der
				
				//REG - REG 3
				sentencias.add( "ADD " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
				RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
				this.elemento = Names.getName(elemIzq);
				
			} else {
				//es variable o consta der
				//REG - VAR 2
				sentencias.add("ADD " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
				this.elemento = Names.getName(elemIzq);
			}
			
		} else {
			//es variable o constante izq
			
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				//es registro der
				//VAR -REG 4
				sentencias.add( "ADD " + Names.getName(elemDer) + ", " + Names.getName(elemIzq) );
				this.elemento = Names.getName(elemDer);
				
			} else {
				//es variable o consta der
				//VAR - VAR 1
				String regaux = RegistryManager.getInstance().obtenerRegistro();
				RegistryManager.getInstance().ocuparRegistro(regaux);
				sentencias.add( "MOV " + regaux + ", " + Names.getName(elemIzq) );
				sentencias.add( "ADD " + regaux + ", " + Names.getName(elemDer) );
				this.elemento = regaux;
			}
			
		}
		return this.sentencias;
	}
}
