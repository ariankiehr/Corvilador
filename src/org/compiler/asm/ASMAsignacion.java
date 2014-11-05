package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMAsignacion {
	private static ASMAsignacion instance = null;
	private List<String> sentencias; 
	private String elemento;
	
	private ASMAsignacion() {
		this.sentencias = new LinkedList<String> ();
	}
		
	

	public static ASMAsignacion getInstance() {
			if (instance == null) {
				instance = new ASMAsignacion();
			}
			return instance;
		
	}
	
	public String getElemento() {
		return this.elemento;
	}
	
	public List<String> generarAsignacion(String elemIzq, String elemDer) {
		this.sentencias = new LinkedList<String> ();
		
		if ( RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) != null ) {
			//es un registro la izq (por el vector puede ser)
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				// registro der
				sentencias.add( "MOV "+ Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
				RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
				RegistryManager.getInstance().desocuparRegistro(Names.getName(elemIzq));
			} else {
				//variable der
				String regaux = RegistryManager.getInstance().obtenerRegistro();
				RegistryManager.getInstance().ocuparRegistro(regaux);
				sentencias.add( "MOV "+ regaux + ", " + Names.getName(elemDer) );
				sentencias.add( "MOV "+ Names.getName(elemIzq) + ", " + regaux );
				RegistryManager.getInstance().desocuparRegistro(regaux);
				RegistryManager.getInstance().desocuparRegistro(Names.getName(elemIzq));
			}
		} else {
			//no es registro el de la izq (no es vector)
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				// registro der
				sentencias.add( "MOV "+ Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
				RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
			} else {
				//variable der
				String regaux = RegistryManager.getInstance().obtenerRegistro();
				RegistryManager.getInstance().ocuparRegistro(regaux);
				sentencias.add( "MOV "+ regaux + ", " + Names.getName(elemDer) );
				sentencias.add( "MOV "+ Names.getName(elemIzq) + ", " + regaux );
				RegistryManager.getInstance().desocuparRegistro(regaux);
		
			}
		}
	
		return this.sentencias;
	}
}
