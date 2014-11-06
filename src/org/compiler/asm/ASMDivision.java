package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMDivision {
	
		private static final String regDivAX = "AX";
		private static final String regDivDX = "DX";
		private static ASMDivision instance = null;
		private List<String> sentencias; 
		private String elemento;
		
		private ASMDivision() {
			this.sentencias = new LinkedList<String> ();
		}
			
		public static ASMDivision getInstance() {
				if (instance == null) {
					instance = new ASMDivision();
				}
				return instance;
			
		}
		
		public String getElemento() {
			return this.elemento;
		}

		
		public List<String> generarDivision(String elemIzq, String elemDer) {
			this.sentencias = new LinkedList<String> ();
			boolean dxUsado = false;
			
			sentencias.add( "; comienzo division" );
			
			if(!RegistryManager.getInstance().estaLibre(regDivDX)){ // DX usado 
				CodeGenerator.useSwapDX();
				sentencias.add("MOV @swap_DX , " + regDivDX);
				dxUsado = true;
			}
			//devolver valor a dx
			sentencias.add("XOR "+ regDivDX + ", " + regDivDX);
			RegistryManager.getInstance().ocuparRegistro(regDivDX); //ocupar siempre para que nadie me saque los 0

			if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemIzq)) != null ) {
				//es un registro izq
				if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ) {
					//es registro der
					
					//REG - REG 3
					if (regDivAX.equals(elemDer)){
						CodeGenerator.useSwapAX();
						sentencias.add("MOV @swap_AX , " + regDivAX);
						sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq) + "  ; ocupe AX");
						sentencias.add("CWD");
						sentencias.add("IDIV " + "@swap_AX");
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
						this.elemento = regDivAX;
					
					} else {
						if (regDivAX.equals(elemIzq)){
							sentencias.add("CWD");
							sentencias.add("IDIV " + Names.getName(elemDer));
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
							this.elemento = regDivAX;
						
						} else{//si ninguno es AX
						
							if (!RegistryManager.getInstance().estaLibre(regDivAX)){//si esta usado AX
								CodeGenerator.useSwapAX();
								sentencias.add("MOV @swap_AX , " + regDivAX);
								sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
								sentencias.add("CWD");
								sentencias.add("IDIV " + Names.getName(elemDer));
								RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
								RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));

								String reg = null;
								try {
									reg = RegistryManager.getInstance().obtenerRegistro();
								} catch (FullRegistersException e) {
									System.out.println( e.getMessage() );
								}

								RegistryManager.getInstance().ocuparRegistro(reg);

								sentencias.add("MOV " + reg + ", " + regDivAX); 
								sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
								this.elemento = reg;
							
							} else {//si AX esta libre lo tengo que ocupar con lo q tiene el izq
								sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
								RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
								RegistryManager.getInstance().ocuparRegistro(regDivAX);

								String reg = null;
								try {
									reg = RegistryManager.getInstance().obtenerRegistro();
								} catch (FullRegistersException e) {
									System.out.println( e.getMessage() );
								}  // comprobar si hay libres

								RegistryManager.getInstance().ocuparRegistro(reg);

								sentencias.add("MOV " + reg +" , " + Names.getName(elemDer));
								RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
								sentencias.add("CWD");
								sentencias.add("IDIV " + reg);
								RegistryManager.getInstance().desocuparRegistro(reg);
								this.elemento = regDivAX;
							}
						
						
						}
					}
					
				} else {
					// REG - VAR
					if (regDivAX.equals(elemIzq)){ //REG izq es AX

						String reg = null;
						try {
							reg = RegistryManager.getInstance().obtenerRegistro();
						} catch (FullRegistersException e) {
							System.out.println( e.getMessage() );
						}  // comprobar si hay libres

						RegistryManager.getInstance().ocuparRegistro(reg);

						sentencias.add("MOV " + reg +" , " + Names.getName(elemDer));
						sentencias.add("CWD");
						sentencias.add("IDIV " + reg);
						RegistryManager.getInstance().desocuparRegistro(Names.getReg(reg));
						this.elemento = regDivAX;
					
					} else {// Reg izq no es AX
						if (!RegistryManager.getInstance().estaLibre(regDivAX)){//si esta usado AX
							CodeGenerator.useSwapAX();
							sentencias.add("MOV @swap_AX , " + regDivAX);
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));

							String regaux = null;
							try {
								regaux = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e) {
								System.out.println( e.getMessage() );
							}  // comprobar si hay libres

							RegistryManager.getInstance().ocuparRegistro(regaux);

							sentencias.add("MOV " + regaux +" , " + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + regaux);
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(regaux));
							

							String reg = null;
							try {
								reg = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e) {
								System.out.println( e.getMessage() );
							}

							RegistryManager.getInstance().ocuparRegistro(reg);

							sentencias.add("MOV " + reg + ", " + regDivAX); 
							sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
							this.elemento = reg;
						
						} else{// si AX esta libre no necesitamos hacer swap
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemIzq));
							RegistryManager.getInstance().ocuparRegistro(regDivAX);

							String reg = null;
							try {
								reg = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e) {
								System.out.println( e.getMessage() );
							}

							RegistryManager.getInstance().ocuparRegistro(reg);

							sentencias.add("MOV " + reg +" , " + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + reg);
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(reg));
							this.elemento = regDivAX;
						}

						
					}
					
					
				}	

					
				} else {
					if ( RegistryManager.getInstance().estaLibre(Names.getReg(elemDer)) != null ){
						// VAR - REG
						if( RegistryManager.getInstance().estaLibre(regDivAX) ) { //AX LIBRE
							sentencias.add("MOV " + regDivAX + " ," + Names.getName(elemIzq));
							RegistryManager.getInstance().ocuparRegistro(regDivAX);
							sentencias.add("CWD");
							sentencias.add("IDIV " + Names.getName(elemDer));
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));
							this.elemento = regDivAX;
						
						}else {// AX OCUPADO
							CodeGenerator.useSwapAX();
							sentencias.add("MOV @swap_AX , " + regDivAX);
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							if( regDivAX.equals(Names.getName(elemDer)) ) { //elemDer era AX
								sentencias.add("CWD");
								sentencias.add("IDIV @swap_AX");
							}else { //elemDer no era AX
								sentencias.add("CWD");
								sentencias.add("IDIV " + Names.getName(elemDer));
								RegistryManager.getInstance().desocuparRegistro(Names.getReg(elemDer));

							}

							String reg = null;
							try {
								reg = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e) {
								System.out.println( e.getMessage() );
							}

							RegistryManager.getInstance().ocuparRegistro(reg);

							sentencias.add("MOV " + reg + ", " + regDivAX); 
							sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
							this.elemento = reg;
						}
					} else{
						// VAR - VAR
						if( RegistryManager.getInstance().estaLibre(regDivAX) ) { //AX LIBRE
							sentencias.add("MOV " + regDivAX + " ," + Names.getName(elemIzq));
							RegistryManager.getInstance().ocuparRegistro(regDivAX);

							String reg = null;
							try {
								reg = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e) {
								System.out.println( e.getMessage() );
							}


							RegistryManager.getInstance().ocuparRegistro(reg);

							sentencias.add("MOV " + reg + " ," + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + reg);
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(reg));
							this.elemento = regDivAX;
						
						}else {// AX OCUPADO
							CodeGenerator.useSwapAX();
							sentencias.add("MOV @swap_AX , " + regDivAX);
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));

							String aux = null;
							try {
								aux = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e1) {
								e1.printStackTrace();
							}

							RegistryManager.getInstance().ocuparRegistro(aux);

							sentencias.add("MOV " + aux + " ," + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + aux);
							RegistryManager.getInstance().desocuparRegistro(Names.getReg(aux));

							String reg = null;
							try {
								reg = RegistryManager.getInstance().obtenerRegistro();
							} catch (FullRegistersException e) {
								System.out.println( e.getMessage() );
							}

							RegistryManager.getInstance().ocuparRegistro(reg);

							sentencias.add("MOV " + reg + ", " + regDivAX); 
							sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
							this.elemento = reg;
						}
					}
				}
					
			if ( dxUsado ) { // devuelvo el valor a dx si estaba usado
				sentencias.add("MOV " + regDivDX + ", @swap_DX");				
			}
			
			else {
				RegistryManager.getInstance().desocuparRegistro(regDivDX);
			}
			
			sentencias.add( "; fin de division" );
			
			return this.sentencias;
		}				
		
}