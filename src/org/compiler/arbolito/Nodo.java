package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;

import org.compiler.asm.Names;
import org.compiler.asm.RegistryManager;

public class Nodo extends NodoConTipo {

    private Arbol hijoIzq;
    private Arbol hijoDer;
    private String registro;

    public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public Nodo(String e, Arbol i, Arbol d, String t) {
	super(e, t);
	this.hijoIzq = i;
	this.hijoDer = d;

    }

    public Arbol getHijo_izq() {
	return hijoIzq;
    }

    public void setHijo_izq(Arbol hijo_izq) {
	this.hijoIzq = hijo_izq;
    }

    public Arbol getHijo_der() {
	return hijoDer;
    }

    public void setHijo_der(Arbol hijo_der) {
	this.hijoDer = hijo_der;
    }

    public String toString() {
	return mostrar("", true);
    }

    public String mostrar(String prefix, boolean isTail) {
	StringBuilder ret = new StringBuilder();
	ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem()+ "\n");

	ret.append(hijoIzq.mostrar(prefix + (isTail ? "      " : "│     "), false)+ "\n");
	ret.append(hijoDer.mostrar(prefix + (isTail ? "      " : "│     "), true));

	return ret.toString();
    }

	@Override
	public List<String> getSentencias() {
		List<String> ret = new LinkedList<String>();
		
		if( !hijoIzq.isLeaf()) {
			ret.addAll(hijoIzq.getSentencias());
		}
		
		if( !hijoDer.isLeaf()) {
			ret.addAll(hijoDer.getSentencias());
		}
		
		String elemIzq = hijoIzq.getElem();
		String elemDer = hijoDer.getElem();
		
		
		//SUMA
		if( "+".equals(elemento) ) {
			
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) != null ) {
				//es un registro izq
				if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
					//es registro der
					
					//REG - REG 3
					ret.add( "ADD " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
					this.elemento = Names.getName(elemIzq);
					
				} else {
					//es variable o consta der
					//REG - VAR 2
					ret.add("ADD " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					this.elemento = Names.getName(elemIzq);
				}
				
			} else {
				//es variable o constante izq
				
				if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
					//es registro der
					//VAR -REG 4
					ret.add( "ADD " + Names.getName(elemDer) + ", " + Names.getName(elemIzq) );
					this.elemento = Names.getName(elemDer);
					
				} else {
					//es variable o consta der
					//VAR - VAR 1
					String regaux = RegistryManager.getInstance().obtenerRegistro();
					RegistryManager.getInstance().ocuparRegistro(regaux);
					ret.add( "MOV " + regaux + ", " + Names.getName(elemIzq) );
					ret.add( "ADD " + regaux + ", " + Names.getName(elemDer) );
					this.elemento = regaux;
				}
			}
		
			//MULTIPLICACION
		} else if ( "*".equals(elemento) ) {
			
			if ( "AX".equals(Names.getName(elemIzq)) && RegistryManager.getInstance().estaLibre(Names.getName(elemIzq)) == true ) {
				//es un registro izq y es AX
				if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
					//es registro der
					
					//REG - REG 3
					ret.add( "IMUL " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
					this.elemento = Names.getName(elemIzq);
					
				} else {
					//es variable o consta der
					//REG - VAR 2
					ret.add("IMUL " + Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					this.elemento = Names.getName(elemIzq);
				}
				
			} else {
				//es variable o constante izq
				
				if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
					//es registro der
					//VAR -REG 4
					ret.add( "ADD " + Names.getName(elemDer) + ", " + Names.getName(elemIzq) );
					this.elemento = Names.getName(elemDer);
					
				} else {
					//es variable o consta der
					//VAR - VAR 1
					//debo ocupar si o si AX el lado izq.
					
					String regaux = RegistryManager.getInstance().obtenerRegistro();
					RegistryManager.getInstance().ocuparRegistro(regaux);
					ret.add( "MOV " + regaux + ", " + Names.getName(elemIzq) );
					ret.add( "ADD " + regaux + ", " + Names.getName(elemDer) );
					this.elemento = regaux;
				}
			}
			
			
		} else if( "-".equals(elemento) || "/".equals(elemento) ) {
			//ret.addAll(hijoDer.getSentencias());
			if ( RegistryManager.getInstance().estaLibre(elemIzq) != null ) {
				//izq una registro
				if( RegistryManager.getInstance().estaLibre(elemDer) != null ) {
					//der un registro
					// REG- REG 
					ret.add( (("-".equals(elemento))?"SUB ":"IDIV ")+ Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
					this.elemento = Names.getName(elemIzq);
					
					
				} else {
					//der variable
					// REG - VAR
					ret.add( (("-".equals(elemento))?"SUB ":"IDIV ")+ Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
					this.elemento = Names.getName(elemIzq);
					
				}
				
			} else {
				if ( RegistryManager.getInstance().estaLibre(elemDer) != null ) {
					//izq var y der registro
					// VAR - REG

					String regaux = RegistryManager.getInstance().obtenerRegistro();
					RegistryManager.getInstance().ocuparRegistro(regaux);
					ret.add( "MOV " + regaux + ", " + Names.getName(elemIzq) );
					ret.add( (("-".equals(elemento))?"SUB ":"IDIV ") + regaux + ", " + Names.getName(elemDer) );
					RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
					this.elemento = Names.getName(elemIzq);
		
				}else {
					//izq var y der var
					// VAR - VAR
					String regaux = RegistryManager.getInstance().obtenerRegistro();
					RegistryManager.getInstance().ocuparRegistro(regaux);
					ret.add( "MOV "+ regaux + ", " + Names.getName(elemIzq) );
					ret.add( (("-".equals(elemento))?"SUB ":"IDIV ")+ regaux + ", " + Names.getName(elemDer) );
					this.elemento = regaux;
					
					
				}
			}
			
			
		} else if( ":=".equals(elemento) ) {
			if ( RegistryManager.getInstance().estaLibre(Names.getName(elemDer)) != null ) {
				// registro
				ret.add( "MOV "+ Names.getName(elemIzq) + ", " + Names.getName(elemDer) );
				RegistryManager.getInstance().desocuparRegistro(Names.getName(elemDer));
			} else {
				//variable
				String regaux = RegistryManager.getInstance().obtenerRegistro();
				RegistryManager.getInstance().ocuparRegistro(regaux);
				ret.add( "MOV "+ regaux + ", " + Names.getName(elemDer) );
				ret.add( "MOV "+ Names.getName(elemIzq) + ", " + regaux );
				RegistryManager.getInstance().desocuparRegistro(regaux);
			}
			
			
		}
		
		
		return ret;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

}
