package org.compiler.lex;

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