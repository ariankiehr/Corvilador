package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMMultiplicacion {
	private static final String regMul = "AX";
	private static ASMMultiplicacion instance = null;
	private List<String> sentencias; 
	private String elemento;
	
	private ASMMultiplicacion() {
		this.sentencias = new LinkedList<String> ();
	}
		
	

	public static ASMMultiplicacion getInstance() {
			if (instance == null) {
				instance = new ASMMultiplicacion();
			}
			return instance;
		
	}
	
	public String getElemento() {
		return this.elemento;
	}
	
	public List<String> generarMultiplicacion(String elemIzq, String elemDer) {
		this.sentencias = new LinkedList<String> ();
		if ( RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) != null ) {
			//es un registro izq
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				//es registro der
				
				//REG - REG 3
				
				if ( regMul.equals(elemDer) ){
					sentencias.add( "IMUL " + Names.getName(elemDer) + ", " + Names.getName(elemIzq) );
					sentencias.add("JO overflow" );
					this.elemento = Names.getName(elemDer);	
				
				}else if ( regMul.equals(elemIzq) ) {
					sentencias.add( "IMUL " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					sentencias.add("JO overflow" );
					this.elemento = Names.getName(elemIzq);					
				
				} else if ( RegistryManager.getInstance().estaLibre(regMul) ) {
					RegistryManager.getInstance().ocuparRegistro(regMul);
					sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
					sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
					sentencias.add("JO overflow" );
					RegistryManager.getInstance().desocuparRegistro(elemDer);
					RegistryManager.getInstance().desocuparRegistro(elemIzq);
					this.elemento = regMul;
				
				} else {
					sentencias.add("MOV @swap_AX , " + regMul);
					sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
					sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
					sentencias.add("JO overflow" );
					String reg = RegistryManager.getInstance().obtenerRegistro();
					RegistryManager.getInstance().ocuparRegistro(reg);
					sentencias.add("MOV "+ reg + ", " + regMul);
					sentencias.add("MOV " + regMul + ", " + "@swap_AX");
					RegistryManager.getInstance().desocuparRegistro(elemDer);
					RegistryManager.getInstance().desocuparRegistro(elemIzq);
					this.elemento = reg;
				}
		
			} else {
				//es variable o consta der
				//REG - VAR 2
				if( regMul.equals(elemIzq) ) {
					sentencias.add( "IMUL " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					sentencias.add("JO overflow" );
					this.elemento = Names.getName(elemIzq);	
				}
				else {
					if ( RegistryManager.getInstance().estaLibre(regMul) ) {
						RegistryManager.getInstance().ocuparRegistro(regMul);
						sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
						sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
						sentencias.add("JO overflow" );
						this.elemento = regMul;
					}
					else {
						sentencias.add("MOV @swap_AX , " + regMul);
						sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
						sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
						sentencias.add("JO overflow" );
						String reg = RegistryManager.getInstance().obtenerRegistro();
						RegistryManager.getInstance().ocuparRegistro(reg);
						sentencias.add("MOV "+ reg + ", " + regMul);
						sentencias.add("MOV " + regMul + ", " + "@swap_AX");
						this.elemento = reg;
					}
				}
				
			}
			
		} else {
			//es variable o constante izq
			
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				//es registro der
				//VAR -REG 4
				if ( regMul.equals(elemDer) ) {
					sentencias.add( "IMUL " + Names.getName(elemDer) + ", " + Names.getName(elemIzq) );
					sentencias.add("JO overflow" );
					this.elemento = Names.getName(elemDer);
				
				} else {
					
					if ( RegistryManager.getInstance().estaLibre(regMul) ) {
						RegistryManager.getInstance().ocuparRegistro(regMul);
						sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
						sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
						sentencias.add("JO overflow" );
						RegistryManager.getInstance().desocuparRegistro(elemDer);
						this.elemento = regMul;
					}
					else {
						sentencias.add("MOV @swap_AX , " + regMul);
						sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
						sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
						sentencias.add("JO overflow" );
						String reg = RegistryManager.getInstance().obtenerRegistro();
						RegistryManager.getInstance().ocuparRegistro(reg);
						sentencias.add("MOV "+ reg + ", " + regMul);
						sentencias.add("MOV " + regMul + ", " + "@swap_AX");
						RegistryManager.getInstance().desocuparRegistro(elemDer);
						this.elemento = reg;
					}
				}
			} else {
				//es variable o consta der
				//VAR - VAR 1
				if ( RegistryManager.getInstance().estaLibre(regMul) ) {
					RegistryManager.getInstance().ocuparRegistro(regMul);
					sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
					sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
					sentencias.add("JO overflow" );
					this.elemento = regMul;
				}
				else {
					sentencias.add("MOV @swap_AX , " + regMul);
					sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
					sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
					sentencias.add("JO overflow" );
					String reg = RegistryManager.getInstance().obtenerRegistro();
					RegistryManager.getInstance().ocuparRegistro(reg);
					sentencias.add("MOV "+ reg + ", " + regMul);
					sentencias.add("MOV " + regMul + ", " + "@swap_AX");
					this.elemento = reg;
				}
					
			}
				
		}
		
		
		return this.sentencias;
	}
}
