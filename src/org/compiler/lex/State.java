package org.compiler.lex;

import java.util.LinkedList;
import java.util.List;


class Pair {
    private PossibleChars pc;
    private Transition t;
    
    public PossibleChars getPossibleChar() {
        return pc;
    }

    public void setPossibleChar(PossibleChars pc) {
        this.pc = pc;
    }

    public Transition getTransition() {
        return t;
    }

    public void setTransition(Transition t) {
        this.t = t;
    }

    public Pair(PossibleChars pc, Transition t) {
	this.pc = pc;
	this.t = t;
    }
}

public class State {
    
    private List<Pair> transitions;
    
    public State() {
	transitions = new LinkedList<Pair>();
    }
    
    public void addTransition(PossibleChars key, Transition t) {
	transitions.add(new Pair(key, t));
    }
    
    
    public String put(Character c) throws LexicalReaderException {
	
	Transition t = null;
	
	for (Pair p : transitions) {
	    if( p.getPossibleChar().match(c) ) {
		t = p.getTransition();
	    }
	}
	
	if (t==null) {
	    throw new LexicalReaderException("Caracter erroneo");
	}
	
	StateMachine.setState(t.getNewState());
	return t.getSemanticAction().execute(c); 

    }
    
}