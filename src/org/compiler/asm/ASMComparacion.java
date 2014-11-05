package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMComparacion {

	private static ASMComparacion instance = null;
	private List<String> sentencias; 
	private String elemento;
	
	private ASMComparacion() {
		this.sentencias = new LinkedList<String> ();
	}
		
	

	public static ASMComparacion getInstance() {
			if (instance == null) {
				instance = new ASMComparacion();
			}
			return instance;
		
	}
	
	public String getElemento() {
		return this.elemento;
	}
	
	public List<String> generarComparacion(String elemIzq, String elemDer) {
		this.sentencias = new LinkedList<String> ();
		if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemIzq)) != null ) {
			//es un registro izq
			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
				//es registro der
				//REG - REG 3
				sentencias.add( "CMP " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
				RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
				RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
						
			} else{	//es variable o consta der
				//REG - VAR 2
				String reg = null;
				try {
					reg = RegistryManager.getInstance().obtenerRegistro();
				} catch (FullRegistersException e) {
					System.out.println( e.getMessage() );
				}
				RegistryManager.getInstance().ocuparRegistro(reg);
				sentencias.add( "MOV " + reg + ", " + Names.getName(elemDer) );
				sentencias.add("CMP " + Names.getName(elemIzq) + ", " + reg );
				RegistryManager.getInstance().desocuparRegistro(reg);

			}
		}else{
			//es variable o constante izq
			
			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
				//es registro der
				//VAR -REG 4
				String regaux = null;
				try {
					regaux = RegistryManager.getInstance().obtenerRegistro();
				} catch (FullRegistersException e) {
					System.out.println( e.getMessage() );
				}
				RegistryManager.getInstance().ocuparRegistro(regaux);
				sentencias.add( "MOV " + regaux + ", " + Names.getName(elemIzq) );
				sentencias.add( "CMP " + regaux + ", " + Names.getName(elemIzq) );
				RegistryManager.getInstance().desocuparRegistro(regaux);

		
			}else{
				//es variable o consta der
				//VAR - VAR 1
				String reg1 = null;
				try {
					reg1 = RegistryManager.getInstance().obtenerRegistro();
				} catch (FullRegistersException e) {
					System.out.println( e.getMessage() );
				}
				RegistryManager.getInstance().ocuparRegistro(reg1);
				String reg2 = null;
				try {
					reg2 = RegistryManager.getInstance().obtenerRegistro();
				} catch (FullRegistersException e) {
					System.out.println( e.getMessage() );
				}
				RegistryManager.getInstance().ocuparRegistro(reg2);
				sentencias.add( "MOV " + reg2 + ", " + Names.getName(elemDer) );
				sentencias.add( "MOV " + reg1 + ", " + Names.getName(elemIzq) );
				sentencias.add( "CMP " + reg1 + ", " + reg2 );
				RegistryManager.getInstance().desocuparRegistro(reg1);
				RegistryManager.getInstance().desocuparRegistro(reg2);

			}
		
		}
		
		return this.sentencias;
		
	}
}
