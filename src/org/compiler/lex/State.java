package org.compiler.lex;

import java.util.LinkedList;
import java.util.List;

public class State {

	private List<Pair> transitions;
	private String name;

	public State(String name) {
		transitions = new LinkedList<Pair>();
		this.name = name;
	}

	public void addTransition(PossibleChars key, Transition t) {
		transitions.add(new Pair(key, t));
	}

	public String put(Character c) throws LexicalReaderException {

		Transition t = null;

		for (Pair p : transitions) {
			if (p.getPossibleChar().match(c)) {
				t = p.getTransition();
				break;
			}
		}

		if (t == null) {
			throw new LexicalReaderException("Caracter erroneo");
		}

		StateMachine.getInstance().setState(t.getNewState());

		String s = null;
		try {
			s = t.getSemanticAction().execute(c);
		} catch (Exception e) {
			throw e;
		}

		return s;

	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "State [name=" + name + "]";
	}
	
	

}