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
			if(!RegistryManager.getInstance().estaLibre(regDivDX)){ // DX usado 
				sentencias.add("MOV @swap_DX , " + regDivDX);
				dxUsado = true;
			}
			//devolver valor a dx
			sentencias.add("XOR "+ regDivDX + ", " + regDivDX);
			RegistryManager.getInstance().ocuparRegistro(regDivDX); //ocupar siempre para que nadie me saque los 0

			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) != null ) {
				//es un registro izq
				if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
					//es registro der
					
					//REG - REG 3
					if (regDivAX.equals(elemDer)){
						sentencias.add("MOV @swap_AX , " + regDivAX);
						sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
						sentencias.add("CWD");
						sentencias.add("IDIV " + "@swap_AX");
						RegistryManager.getInstance().desocuparRegistro(Names.getName(elemIzq));
						this.elemento = regDivAX;
					
					} else {
						if (regDivAX.equals(elemIzq)){
							sentencias.add("CWD");
							sentencias.add("IDIV " + Names.getName(elemDer));
							RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
							this.elemento = regDivAX;
						
						} else{//si ninguno es AX
						
							if (!RegistryManager.getInstance().estaLibre(regDivAX)){//si esta usado AX
								sentencias.add("MOV @swap_AX , " + regDivAX);
								sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
								sentencias.add("CWD");
								sentencias.add("IDIV " + Names.getName(elemDer));
								RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
								String reg = RegistryManager.getInstance().obtenerRegistro();
								sentencias.add("MOV " + reg + ", " + regDivAX); 
								sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
								this.elemento = reg;
							
							} else {//si AX esta libre lo tengo que ocupar con lo q tiene el izq
								sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
								RegistryManager.getInstance().desocuparRegistro(Names.getName(elemIzq));
								RegistryManager.getInstance().ocuparRegistro(regDivAX);
								String reg = RegistryManager.getInstance().obtenerRegistro();  // comprobar si hay libres
								sentencias.add("MOV " + reg +" , " + Names.getName(elemDer));
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
						String reg = RegistryManager.getInstance().obtenerRegistro();  // comprobar si hay libres
						sentencias.add("MOV " + reg +" , " + Names.getName(elemDer));
						sentencias.add("CWD");
						sentencias.add("IDIV " + reg);
						RegistryManager.getInstance().desocuparRegistro(reg);
						this.elemento = regDivAX;
					
					} else {// Reg izq no es AX
						if (!RegistryManager.getInstance().estaLibre(regDivAX)){//si esta usado AX
							sentencias.add("MOV @swap_AX , " + regDivAX);
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							RegistryManager.getInstance().desocuparRegistro(Names.getName(elemIzq));
							String regaux = RegistryManager.getInstance().obtenerRegistro();  // comprobar si hay libres
							sentencias.add("MOV " + regaux +" , " + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + regaux);
							RegistryManager.getInstance().desocuparRegistro(regaux);
							
							String reg = RegistryManager.getInstance().obtenerRegistro();
							sentencias.add("MOV " + reg + ", " + regDivAX); 
							sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
							this.elemento = reg;
						
						} else{// si AX esta libre no necesitamos hacer swap
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							RegistryManager.getInstance().desocuparRegistro(Names.getName(elemIzq));
							RegistryManager.getInstance().ocuparRegistro(regDivAX);
							String reg = RegistryManager.getInstance().obtenerRegistro();  // comprobar si hay libres
							sentencias.add("MOV " + reg +" , " + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + reg);
							RegistryManager.getInstance().desocuparRegistro(reg);
							this.elemento = regDivAX;
						}

						
					}
					
					
				}	

					
				} else {
					if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ){
						// VAR - REG
						if( RegistryManager.getInstance().estaLibre(regDivAX) ) { //AX LIBRE
							sentencias.add("MOV " + regDivAX + " ," + Names.getName(elemIzq));
							RegistryManager.getInstance().ocuparRegistro(regDivAX);
							sentencias.add("CWD");
							sentencias.add("IDIV " + Names.getName(elemDer));
							RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
							this.elemento = regDivAX;
						
						}else {// AX OCUPADO
							sentencias.add("MOV @swap_AX , " + regDivAX);
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							if( regDivAX.equals(Names.getName(elemDer)) ) { //elemDer era AX
								sentencias.add("CWD");
								sentencias.add("IDIV @swap_AX");
							}else { //elemDer no era AX
								sentencias.add("CWD");
								sentencias.add("IDIV " + Names.getName(elemDer));
								RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));

							}
							String reg = RegistryManager.getInstance().obtenerRegistro();
							sentencias.add("MOV " + reg + ", " + regDivAX); 
							sentencias.add("MOV " + regDivAX +" , " + "@swap_AX"); //le devuelvo a AX el valor q tenia antes
							this.elemento = reg;
						}
					} else{
						// VAR - VAR
						if( RegistryManager.getInstance().estaLibre(regDivAX) ) { //AX LIBRE
							sentencias.add("MOV " + regDivAX + " ," + Names.getName(elemIzq));
							RegistryManager.getInstance().ocuparRegistro(regDivAX);
							String reg = RegistryManager.getInstance().obtenerRegistro();
							sentencias.add("MOV " + reg + " ," + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + reg);
							RegistryManager.getInstance().desocuparRegistro(reg);
							this.elemento = regDivAX;
						
						}else {// AX OCUPADO
							sentencias.add("MOV @swap_AX , " + regDivAX);
							sentencias.add("MOV " + regDivAX +" , " + Names.getName(elemIzq));
							String aux = RegistryManager.getInstance().obtenerRegistro();
							sentencias.add("MOV " + aux + " ," + Names.getName(elemDer));
							sentencias.add("CWD");
							sentencias.add("IDIV " + aux);
							RegistryManager.getInstance().desocuparRegistro(aux);
							String reg = RegistryManager.getInstance().obtenerRegistro();
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
			
			return this.sentencias;
		}				
		
}