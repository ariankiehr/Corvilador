package org.compiler.lex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.compiler.symboltable.Attribute;
import org.compiler.symboltable.SymbolTable;

public class LexicalAnalyzer {

    private List<Character> input;
    private List<String> tokens;
    public static List<String> errors;
    public static List<String> warnings;

    private int nextToken;

    public LexicalAnalyzer(File source) {
	errors = new LinkedList<String>();
	warnings = new LinkedList<String>();
	input = new LinkedList<Character>();
	tokens = new LinkedList<String>();
	nextToken = 0;
	readFile(source);
	generateTokens();
    }

    public void setNextToken(int v) {
	nextToken = v;
    }

    public Token nextToken() {
	String tk = tokens.get(nextToken++);
	Attribute a = SymbolTable.getInstance().get(tk);
	return new Token((a != null) ? a.getType() : null, tk);
    }

    public boolean hasMoreTokens() {
	return !tokens.get(nextToken).equals("EOF");
    }

    private void generateTokens() {

	StateMachine fsm = StateMachine.getInstance();
	String tsr;
	int next = 1;

	for (int i = 0; i < input.size(); ++i) {

	    if (input.get(i).equals('\n'))
		next++;

	    try {
		if ((tsr = fsm.put(input.get(i))) != null) {
		    tokens.add(tsr);
		    i--;
		}
	    } catch (LexicalReaderException e) {

		if (input.get(i).equals('\n')) {
		    errors.add(e.getMessage() + " en linea " + next
			    + " Caracter \"salto de linea\"");
		} else {
		    errors.add(e.getMessage() + " en linea " + next
			    + " Caracter \"" + input.get(i) + "\"");
		}

	    }

	}

	tokens.add("EOF");
    }

    private void readFile(File source) {
	Charset encoding = Charset.defaultCharset();
	try (InputStream in = new FileInputStream(source);
		Reader reader = new InputStreamReader(in, encoding);
		Reader buffer = new BufferedReader(reader)) {
	    int r;
	    while ((r = reader.read()) != -1) {
		Character ch = (char) r;
		input.add(ch);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	input.add('\n'); // para finalizar

    }

}
