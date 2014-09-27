package org.compiler.lex.actions;

import org.compiler.lex.LexicalReaderException;

public abstract class SemanticAction {
	public abstract String execute(Character c) throws LexicalReaderException;
}
