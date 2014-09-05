package org.compiler.lex.actions;

import org.compiler.lex.StateMachine;

public class AddChar extends SemanticAction { 
    
    public String execute(Character c) {
	StateMachine.addChar(c);
	return null;
    }
    
}
