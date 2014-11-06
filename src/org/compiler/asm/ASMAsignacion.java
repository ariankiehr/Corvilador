package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

import org.compiler.symboltable.SymbolTable;

public class ASMAsignacion {
	private static ASMAsignacion instance = null;
	private List<String> sentencias;
	private String elemento;

	private ASMAsignacion() {
		this.sentencias = new LinkedList<String>();
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
		
		String aux = elemIzq;
		elemIzq = elemDer;
		elemDer = aux;
		
		
		this.sentencias = new LinkedList<String>();

		if (RegistryManager.getInstance().estaLibre(Names.getReg(elemIzq)) != null) {
			// es un registro la izq (por el vector puede ser)
			if (RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null) {
				// registro der
				
				if(elemDer.contains("[")) {
					sentencias.add( "MOV " + Names.getReg(elemDer) +", " + elemDer );
					sentencias.add("MOV " + Names.getName(elemIzq) + ", "
							+ Names.getReg(elemDer));
					RegistryManager.getInstance().desocuparRegistro(
							Names.getReg(elemDer));
					RegistryManager.getInstance().desocuparRegistro(
							Names.getReg(elemIzq));
				} else {
					sentencias.add("MOV " + Names.getName(elemIzq) + ", "
							+ Names.getName(elemDer));
					RegistryManager.getInstance().desocuparRegistro(
							Names.getReg(elemDer));
					RegistryManager.getInstance().desocuparRegistro(
							Names.getReg(elemIzq));
				}
				
				
				
			} else {
				// variable der
				if ("id".equals(SymbolTable.getInstance().get(elemDer).getTypeOfToken()) ){
					if(elemIzq.contains("[")) { 
						//el lado izq es un vector(viene con registro), der es variable	
						String reg = null;
						try {
							reg = RegistryManager.getInstance().obtenerRegistro();
						} catch (FullRegistersException e) {
							e.printStackTrace();
						}
						sentencias.add( "MOV " + reg +", " + Names.getName(elemDer) + "; entre");
						sentencias.add("MOV " + Names.getName(elemIzq) + ", " + reg);
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
						RegistryManager.getInstance().desocuparRegistro(reg);
					} else {
						//el lado izq no es un vector, la der es variable
						sentencias.add("MOV " + Names.getName(elemIzq) + ", "
								+ Names.getName(elemDer));
						RegistryManager.getInstance().desocuparRegistro(
								Names.getReg(elemDer));
						RegistryManager.getInstance().desocuparRegistro(
								Names.getReg(elemIzq));
					
					}
				}else{
					//der es constante
						sentencias.add("MOV " + Names.getName(elemIzq) + ", " + Names.getName(elemDer));
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
						
					}
			} 
		}else {
			// no es registro el de la izq (no es vector)
			if (RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null) {
				// registro der
				sentencias.add("MOV " + Names.getName(elemIzq) + ", "
						+ Names.getName(elemDer));
				RegistryManager.getInstance().desocuparRegistro(
						Names.getReg(elemDer));
			} else {
				// variable o conts der
				
				if ("id".equals( SymbolTable.getInstance().get(elemDer).getTypeOfToken() )) {
					
					String reg = null;
					try {
						reg = RegistryManager.getInstance().obtenerRegistro();
					} catch (FullRegistersException e) {
						System.out.println( e.getMessage() );
					}

					RegistryManager.getInstance().ocuparRegistro(reg);
					
					sentencias.add("MOV " + reg + ", " + Names.getName(elemDer));
					sentencias.add("MOV " + Names.getName(elemIzq) + ", " + reg);
					
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(reg));

					
				} else {
					sentencias.add("MOV " + Names.getName(elemIzq) + ", " + Names.getName(elemDer));
				}
				
				
			}
		}

		return this.sentencias;
	}
}
