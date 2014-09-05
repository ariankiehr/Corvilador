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


public class LexicalAnalyzer {

    private List<Character> input;
    private List<String> tokens;
    private int nextToken;

    public LexicalAnalyzer(File source) {
	input = new LinkedList<Character>();
	tokens = new LinkedList<String>();
	nextToken = 0;
	readFile(source);
	generateTokens();
    }

    public String getNextToken() {
	return tokens.get(nextToken++);
    }

    public String getSymbolTable() {
	return null;
    }

    private void generateTokens() {

	StateMachine fsm = new StateMachine();
	String tsr;
	int next=1;

	for (int i=0; i< input.size(); ++i) {
	    
	    if(input.get(i).equals('\n')) next++;
	    
	    try {
		if ((tsr = fsm.put(input.get(i))) != null) {
		    System.out.println(tsr);
		    tokens.add(tsr);
		    i--;
		}
	    } catch (LexicalReaderException e) {
		if( input.get(i).equals('\n') ) {
		    System.err.println( e.getMessage() + " en linea " + next + " Caracter \"salto de linea\"");
		} else {
		    System.err.println( e.getMessage() + " en linea " + next + " Caracter \"" + input.get(i) +"\"");
		}
		
	    }

	}
	
	//TODO tokens.add("EOF");
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
	input.add('\n'); //para finalizar

    }

}
