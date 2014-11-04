package org.compiler.arbolito;

import java.util.LinkedList;
import java.util.List;

import org.compiler.asm.CodeGenerator;

public class NodoSinTipo extends Arbol {
	
	private Arbol hijoIzq;
	private Arbol hijoDer;
	
	public NodoSinTipo(String elemento, Arbol i, Arbol d) {
		super(elemento);
		this.hijoDer = d;
		this.hijoIzq = i;
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
		return mostrar("",true);
	}
	
	public String getTipo() {
		return null;
	}


	@Override
	public String mostrar(String prefix, boolean isTail) {
	    StringBuilder ret = new StringBuilder();
		ret.append(prefix + (isTail ? "└── " : "├── ") + this.getElem() + "\n");

		ret.append(hijoIzq.mostrar(prefix + (isTail ? "      " : "|     "), false)+ "\n");
		ret.append(hijoDer.mostrar(prefix + (isTail ? "      " : "|     "), true));

		
		return ret.toString();
	}


	@Override
	public List<String> getSentencias() {
		List<String> ret = new LinkedList<String>();
		
		if ("sentencia".equals(elemento)) {
			ret.addAll(hijoIzq.getSentencias());
			ret.addAll(hijoDer.getSentencias());
			return ret;
		} else if ( "si".equals(elemento)  ) {
			
			/*
			└── si
		      ├── condicion
		      │     └── =
		      │           ├── 1:entero
		      │           └── 32:entero
		      └── cuerpo
		            ├── entonces
		            │     └── imprimir:"asd"
		            └── sino
		                  └── imprimir:"sad"
		                  
		     mov ax,1
		     mov bx, 32
		     cmp ax,bx
		     jne sino
		     imprimir:"asd"
		     jmp finsi
		     sino:
		     imprimir:"sad"
		     finsi:
		      
		     */
			ret.addAll(hijoIzq.getSentencias()); //condicion
			CodeGenerator.pushLabel();
			String elseLabel = CodeGenerator.getLabel();
			
			if( "=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()  )) {
				ret.add( "JNE " + elseLabel );
			} else if ( ">".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JLE " + elseLabel );
			} else if ( "<".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JGE " + elseLabel );
			} else if ( "<=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JG " + elseLabel );
			} else if ( ">=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JL " + elseLabel );
			} else if ( "^=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JE " + elseLabel );
			}
			/*
		     mov ax,1
		     mov bx, 32
		     cmp ax,bx
		     jne sino
		     imprimir:"asd"
		     jmp finsi
		     sino:
		     imprimir:"sad"
		     finsi:
		     
		     mov ax,1
		     mov bx, 32
		     cmp ax,bx
		     jne sino
		     imprimir:"asd"
		     sino:
		     
			*/
			
			if( hijoDer instanceof NodoSinTipo  ) {
				ret.addAll( ((NodoSinTipo)hijoDer).getHijo_izq().getSentencias() ); //cuerpo
				
				CodeGenerator.pushLabel();
				ret.add( "JMP " + CodeGenerator.getLabel());
				
				ret.add( elseLabel+":" );
				
				ret.addAll( ((NodoSinTipo)hijoDer).getHijo_der().getSentencias() ); //sino
				ret.add( CodeGenerator.getLabel()+":" );
				CodeGenerator.popLabel();
				CodeGenerator.popLabel();
			} else if ( hijoDer instanceof NodoUnario ) {
				ret.addAll( ((NodoUnario)hijoDer).getHijo().getSentencias() ); //cuerpo

				ret.add( elseLabel+":" );
				
				CodeGenerator.popLabel();
			}

			
			
		} else if ( "iterar".equals(elemento) ) {
			/*
			 └── iterar
			      ├── condicion
			      │     └── =
			      │           ├── 1:entero
			      │           └── 32:entero
			      └── imprimir:"asd"
			      
			 iterar:
			 imprimir:"asd"
			 mov ax,1
		     mov bx, 32
		     cmp ax,bx
		     jne iterar
			 */
			
			CodeGenerator.pushLabel();
			ret.add( CodeGenerator.getLabel()+":" );
			ret.addAll( hijoDer.getSentencias() ); //CUERPO
			ret.addAll(hijoIzq.getSentencias()); //condicion
			
			if( "=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()  )) {
				ret.add( "JNE " + CodeGenerator.popLabel() );
			} else if ( ">".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JLE " + CodeGenerator.popLabel() );
			} else if ( "<".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JGE " + CodeGenerator.popLabel() );
			} else if ( "<=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JG " + CodeGenerator.popLabel() );
			} else if ( ">=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JL " + CodeGenerator.popLabel() );
			} else if ( "^=".equals( ((NodoUnario)hijoIzq).getHijo().getElem()))  {
				ret.add( "JE " + CodeGenerator.popLabel() );
			}
			
		}
		
		return ret;
	}



}
