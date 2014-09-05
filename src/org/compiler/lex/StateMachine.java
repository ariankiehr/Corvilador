package org.compiler.lex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.compiler.lex.actions.AddChar;
import org.compiler.lex.actions.ClearString;
import org.compiler.lex.actions.DoNothing;
import org.compiler.lex.actions.RetrieveToken;


public class StateMachine {
    
    private State initial;
    private static State actualState;
    private static String actualString = "";
    
    public StateMachine() {    
	
	State estadoInicial = new State();
	State operadorCompuestoOpcional = new State();
	State operador = new State();
	State estadoFinal = new State();
	State operadorCompuesto = new State();
	State numero = new State();
	State identificador = new State();
	State cadena = new State();
	State finCadena = new State();
	State comienzoComentario = new State();
	State comentario = new State();
	State comienzoFinComentario = new State();
	State finComentario = new State();
	
	Transition t1 = new Transition(estadoInicial, new DoNothing());
	
	Transition t2 = new Transition(operador, new AddChar());
	
	Transition t3 = new Transition(estadoFinal, new RetrieveToken());
	
	Transition t4 = new Transition(operadorCompuesto, new AddChar());
	
	Transition t5 = new Transition(operador, new AddChar());
	
	Transition t6 = new Transition(operadorCompuestoOpcional, new AddChar());
	
	Transition t7 = new Transition(numero, new AddChar());
	
	Transition t8 = new Transition(identificador, new AddChar());
	
	Transition t9 = new Transition(cadena, new AddChar());
	
	Transition t10 = new Transition(finCadena, new AddChar());
	
	Transition t11 = new Transition(comienzoComentario, new AddChar());
	Transition t12 = new Transition(comentario, new ClearString());
	Transition t13 = new Transition(comienzoFinComentario, new ClearString());
	Transition t14 = new Transition(finComentario, new ClearString());
	Transition t15 = new Transition(estadoFinal, new RetrieveToken());
	
	//TODO los comentarios andan pero devuelven un "" (esto es porque uso retrieveToken para
	//que se vuelva al estado inicial, hay que ver si esto afecta en algo o no)
	
	estadoInicial.addTransition(new PossibleChars('['), t11);
	
	LinkedList<Character> l = new LinkedList<Character>(DomainOfDiscurse.todosCaracteresValidos);
	l.remove(new Character('-'));
	
	comienzoComentario.addTransition(new PossibleChars('-'), t12);
	comienzoComentario.addTransition(new PossibleChars(l), t3);
	

	comentario.addTransition(new PossibleChars(l), t12);
	comentario.addTransition(new PossibleChars('-'), t13);
	
	l = new LinkedList<Character>(DomainOfDiscurse.todosCaracteresValidos);
	l.remove(new Character(']'));
	comienzoFinComentario.addTransition(new PossibleChars(']'), t14);
	comienzoFinComentario.addTransition(new PossibleChars(l), t12);
	
	finComentario.addTransition(new PossibleChars(DomainOfDiscurse.todosCaracteresValidos), t15);
	
	estadoInicial.addTransition(new PossibleChars('\''), t9);
	
	cadena.addTransition(new PossibleChars('\''), t10);
	
	
	//TODO deberia ser cualquier cosa
	l = new LinkedList<Character>(DomainOfDiscurse.todosCaracteresValidos);
	l.remove(new Character('\n'));
	l.remove(new Character('\''));
	cadena.addTransition(new PossibleChars(l), t9);
	
	finCadena.addTransition(new PossibleChars(DomainOfDiscurse.todosCaracteresValidos), t3);
	
	
	
	//TODO hacer los warnings
	
	//TODO truncar
	estadoInicial.addTransition(new PossibleChars('l'), t8);
	identificador.addTransition(new PossibleChars('l','&','_','n'), t8);
	
	
	l = new LinkedList<Character>(DomainOfDiscurse.todosCaracteresValidos);
	l.remove(new Character('l'));
	l.remove(new Character('n'));
	l.remove(new Character('_'));
	l.remove(new Character('&'));
	identificador.addTransition(new PossibleChars(l), t3);
	
	

	//TODO verificar rango
	estadoInicial.addTransition(new PossibleChars('n'), t7);
	numero.addTransition(new PossibleChars('n'), t7);
	
	
	l = new LinkedList<Character>(DomainOfDiscurse.todosCaracteresValidos);
	l.remove(new Character('n'));
	numero.addTransition(new PossibleChars(l), t3);
	
	
	//Con espacios en blanco se queda en el lugar sin hacer nada
	estadoInicial.addTransition(new PossibleChars('\n','\t',' '), t1);
	
	estadoInicial.addTransition(new PossibleChars(':','^'), t4);
	
	estadoInicial.addTransition(new PossibleChars('+','-','*','/','=',',',';','{','}','(',')',']'), t2);
	
	operador.addTransition(new PossibleChars(DomainOfDiscurse.todosCaracteresValidos), t3);
	
	operadorCompuesto.addTransition(new PossibleChars('='), t5);
	
	l = new LinkedList<Character>(DomainOfDiscurse.todosCaracteresValidos);
	l.remove(new Character('='));
	operadorCompuestoOpcional.addTransition(new PossibleChars(l), t3);
	
	operadorCompuestoOpcional.addTransition(new PossibleChars('='), t2);
	
	estadoInicial.addTransition(new PossibleChars('<','>'), t6);

	initial = estadoInicial;
	actualState = estadoInicial;
	
    }

    public String put(Character c) throws LexicalReaderException {
	
	String s = null ;
	try{
	    s = actualState.put(c);
	} catch(LexicalReaderException e) {
	    actualString = "";
	    actualState = initial;
	    throw e;
	}
	
	if( s != null ) {
	    actualString = "";
	    actualState = initial;
	}
	
	return s;
    }
    
    public static void setState(State s) {
	actualState = s;
    }
    
    public static void addChar(Character c) {
	actualString += c.toString();
    }
    
    public static String getActualString() {
	String ret = actualString;
	return ret;
    }
    
    public static void cleanString() {
	actualString = "";
    }
    

}
