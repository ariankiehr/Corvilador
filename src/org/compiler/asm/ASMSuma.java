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
		if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemIzq)) != null ) {
			//es un registro izq
			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
				//es registro der
				
				//REG - REG 3
				if(elemIzq.contains("[")) {
					sentencias.add( "MOV " + Names.getReg(elemIzq) +", " + elemIzq );
				}

				
				if(elemDer.contains("[")) {
					sentencias.add( "MOV " + Names.getReg(elemDer) +", " + elemDer );
					sentencias.add( "ADD " + Names.getReg(elemIzq) + ", " + Names.getReg(elemDer) );
				} else {

					sentencias.add( "ADD " + Names.getReg(elemIzq) + ", " + Names.getName(elemDer) );
				}
				
				RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
				this.elemento = Names.getReg(elemIzq);
				
				
			} else {
				//es variable o consta der
				//REG - VAR 2
				if(elemIzq.contains("[")) {
					sentencias.add( "MOV " + Names.getReg(elemIzq) +", " + elemIzq );
				}
				sentencias.add("ADD " + Names.getReg(elemIzq) + ", " + Names.getName(elemDer) );
				this.elemento = Names.getReg(elemIzq);
			}
			
		} else {
			//es variable o constante izq
			
			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
				//es registro der
				//VAR -REG 4
				sentencias.add( "ADD " + Names.getName(elemDer) + ", " + Names.getName(elemIzq) );
				this.elemento = Names.getName(elemDer);
				
			} else {
				//es variable o consta der
				//VAR - VAR 1
				String regaux = null;
				try {
					regaux = RegistryManager.getInstance().obtenerRegistro();
				} catch (FullRegistersException e) {
					System.out.println( e.getMessage() );
				}
				RegistryManager.getInstance().ocuparRegistro(regaux);
				sentencias.add( "MOV " + regaux + ", " + Names.getName(elemIzq) );
				sentencias.add( "ADD " + regaux + ", " + Names.getName(elemDer) );
				this.elemento = regaux;
			}
			
		}
		return this.sentencias;
	}
}
