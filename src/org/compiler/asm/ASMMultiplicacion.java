package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMMultiplicacion {
	private static final String regMul = "AX";
	private static final String regCX = "CX";
	private static ASMMultiplicacion instance = null;
	private List<String> sentencias; 
	private String elemento;
	private boolean sinRegistro = false;
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
	
	public List<String> generarMultiplicacion(String elemIzq, String elemDer, String tipo) {
		this.sentencias = new LinkedList<String> ();
		if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemIzq)) != null ) {
			//es un registro izq
			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
				//es registro der
				
				//REG - REG 3
				
				if ( regMul.equals(Names.getReg(elemDer)) ){
					//REG de la derecha tiene a AX 
						if(elemDer.contains("[")) {
							//lo tiene dentro del vector
							sentencias.add("MOV "+ regMul + ", " + elemDer);
						}
					//no esta en el vector	
					sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemIzq) );
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
					this.elemento = regMul;	
				
				}else if ( regMul.equals(Names.getReg(elemIzq)) ) {
					//REG izq tiene de AX
					if(elemDer.contains("[")) {
						//esta dentro del vector
						sentencias.add("MOV "+ regMul + ", " + elemDer);
					}
					//no esta en el vector
					sentencias.add( "IMUL " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
					this.elemento = Names.getName(elemIzq);					
				
				} else if ( RegistryManager.getInstance().estaLibre(regMul) ) {
					//AX esta libre
					RegistryManager.getInstance().ocuparRegistro(regMul);
					sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
					sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
					this.elemento = regMul;
				
				} else {
					//AX no lo tiene ni IZQ ni DER pero esta ocupado
					CodeGenerator.useSwapAX();
					sentencias.add("MOV @swap_AX , " + regMul);
					sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
					sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
					RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
					
					//nunca va a quedarse sin registro porque libero 2 antes de pedir
					String reg = null;
					
					try {
						reg = RegistryManager.getInstance().obtenerRegistro();
					} catch (FullRegistersException e) {
						System.out.println( e.getMessage());
										
					}

					RegistryManager.getInstance().ocuparRegistro(reg);

					sentencias.add("MOV "+ reg + ", " + regMul);
					sentencias.add("MOV " + regMul + ", " + "@swap_AX");
					this.elemento = reg;
				}
		
			} else {
				//es variable o consta der
				//REG - VAR 2
				
				if( regMul.equals(Names.getReg(elemIzq)) ) {
					//AX esta en el REG IZQ
					if( elemIzq.contains("[") ) {
						sentencias.add("MOV " + regMul + ", " + Names.getName(elemIzq));
					}
					//AX es REG IZQ
					sentencias.add("IMUL " + regMul + ", " + Names.getName(elemDer));
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					this.elemento = regMul;	
				}
				else {
					if ( RegistryManager.getInstance().estaLibre(regMul) ) {
						//AX libre
						RegistryManager.getInstance().ocuparRegistro(regMul);
						sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
						sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
						//verificar el overflow de los distintos tipos
						if ("entero_ss".equals(tipo)) {
							sentencias.add("CMP " + regMul + " , 65535" );
							sentencias.add("JG overflow");
						}else {
							sentencias.add("JO overflow" );
							
						}
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
						this.elemento = regMul;
					}
					else {
						//AX ocupado
						CodeGenerator.useSwapAX();
						sentencias.add("MOV @swap_AX , " + regMul);
						sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
						sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
						//verificar el overflow de los distintos tipos
						if ("entero_ss".equals(tipo)) {
							sentencias.add("CMP " + regMul + " , 65535" );
							sentencias.add("JG overflow");
						}else {
							sentencias.add("JO overflow" );
							
						}
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));

						//nunca va a quedarse sin registro porque libero 1 antes de pedir

						String reg = null;
						try {
							reg = RegistryManager.getInstance().obtenerRegistro();
						} catch (FullRegistersException e) {
							System.out.println( e.getMessage()  );
						}

						RegistryManager.getInstance().ocuparRegistro(reg);

						sentencias.add("MOV "+ reg + ", " + regMul);
						sentencias.add("MOV " + regMul + ", " + "@swap_AX");
						this.elemento = reg;
					}
				}
				
			}
			
		} else {
			//es variable o constante izq
			
			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
				//es registro der
				//VAR -REG 4
				if ( regMul.equals(Names.getReg(elemDer)) ) {
					//AX ocupado por reg DER
					if ( elemDer.contains("[") ) {
						//viene AX en el vector, lo tengo que pasar a AX
						sentencias.add("MOV " + regMul + ", " + Names.getName(elemDer));
					}
					//es AX reg derecho
					sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemIzq) );
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					this.elemento = regMul;
				
				} else {
					// AX no ocupado por REG DER
					if ( RegistryManager.getInstance().estaLibre(regMul) ) {
						//AX LIBRE
						
						RegistryManager.getInstance().ocuparRegistro(regMul);
						sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
						sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
						//verificar el overflow de los distintos tipos
						if ("entero_ss".equals(tipo)) {
							sentencias.add("CMP " + regMul + " , 65535" );
							sentencias.add("JG overflow");
						}else {
							sentencias.add("JO overflow" );
							
						}
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
						this.elemento = regMul;
					}
					else {
						//AX esta ocupado pero no por reg DER
						CodeGenerator.useSwapAX();
						sentencias.add("MOV @swap_AX , " + regMul);
						sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
						sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
						//verificar el overflow de los distintos tipos
						if ("entero_ss".equals(tipo)) {
							sentencias.add("CMP " + regMul + " , 65535" );
							sentencias.add("JG overflow");
						}else {
							sentencias.add("JO overflow" );
							
						}
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));

						//nunca va a quedarse sin registro porque libero 1 antes de pedir

						String reg = null;
						try {
							reg = RegistryManager.getInstance().obtenerRegistro();
						} catch (FullRegistersException e) {
							
							
							
							System.out.println( e.getMessage() );
						}

						RegistryManager.getInstance().ocuparRegistro(reg);

						sentencias.add("MOV "+ reg + ", " + regMul);
						sentencias.add("MOV " + regMul + ", " + "@swap_AX");
						this.elemento = reg;
					}
				}
			} else {
				//es variable o consta der
				//VAR - VAR 1
				if ( RegistryManager.getInstance().estaLibre(regMul) ) {
					//AX libre
					RegistryManager.getInstance().ocuparRegistro(regMul);
					sentencias.add( "MOV " + regMul + ", " + Names.getName(elemIzq) );
					sentencias.add( "IMUL " + regMul + ", " + Names.getName(elemDer) );
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					this.elemento = regMul;
				}
				else {
					//AX ocupado
					CodeGenerator.useSwapAX();
					sentencias.add("MOV @swap_AX , " + regMul);
					sentencias.add("MOV " + regMul +" , " + Names.getName(elemIzq));
					sentencias.add("IMUL " + regMul + " ," + Names.getName(elemDer));
					//verificar el overflow de los distintos tipos
					if ("entero_ss".equals(tipo)) {
						sentencias.add("CMP " + regMul + " , 65535" );
						sentencias.add("JG overflow");
					}else {
						sentencias.add("JO overflow" );
						
					}
					//posible problema me quedo sin registro
					String reg = null;
					try {
						reg = RegistryManager.getInstance().obtenerRegistro();
					} catch (FullRegistersException e) {
						System.out.println( e.getMessage() );
						sinRegistro = true;
						CodeGenerator.useSwapCX();
						sentencias.add("MOV @swap_CX , " + regCX);
						RegistryManager.getInstance().desocuparRegistro(regCX);
						reg = regCX;
					}

					RegistryManager.getInstance().ocuparRegistro(reg);

					sentencias.add("MOV "+ reg + ", " + regMul);
					sentencias.add("MOV " + regMul + ", " + "@swap_AX");
					
					if (sinRegistro) {
						//reg o regCX es igual
						//uso @swap_AX para almacenar el resultado de la multiplicacion sino tengo
						//registro y se que @swap_AX esta libre
						sentencias.add("MOV @swap_AX , " + reg);
						sentencias.add("MOV " + reg + " , " + "@swap_CX");
						this.elemento = "@swap_AX";
						
					} else {
						this.elemento = reg;
						
					}
					
				}
					
			}
				
		}
		
		return this.sentencias;
	}
}
