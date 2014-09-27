package org.compiler.lex;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PossibleChars {

	private List<Character> possibleChars;

	public PossibleChars() {
		possibleChars = new LinkedList<Character>();
	}

	public PossibleChars(List<Character> characters) {
		possibleChars = new LinkedList<Character>();
		possibleChars.addAll(characters);
	}

	public PossibleChars(Character... c) {
		possibleChars = new LinkedList<Character>();
		List<Character> characters = Arrays.asList(c);
		possibleChars.addAll(characters);
	}

	public boolean match(Character c) {

		if (DomainOfDiscurse.letras.contains(c)) {
			c = DomainOfDiscurse.LETRA;
		}

		if (DomainOfDiscurse.numeros.contains(c)) {
			c = DomainOfDiscurse.NUMERO;
		}

		return possibleChars.contains(c);
	}

	public void addChar(Character c) {

		possibleChars.add(c);
	}

	public void addAllChar(List<Character> l) {
		possibleChars.addAll(l);
	}

	public void deleteChar(Character c) {
		possibleChars.remove(c);
	}

}
