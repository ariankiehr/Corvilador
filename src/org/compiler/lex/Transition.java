package org.compiler.lex;

import org.compiler.lex.actions.SemanticAction;

public class Transition {
    
    private State newState;
    private SemanticAction semanticAction;
    
    public Transition(State newState, SemanticAction semanticAction) {
	super();
	this.newState = newState;
	this.semanticAction = semanticAction;
    }
    
    public State getNewState() {
        return newState;
    }
    public void setNewState(State newState) {
        this.newState = newState;
    }
    public SemanticAction getSemanticAction() {
        return semanticAction;
    }
    public void setSemanticAction(SemanticAction semanticAction) {
        this.semanticAction = semanticAction;
    }
    
}
