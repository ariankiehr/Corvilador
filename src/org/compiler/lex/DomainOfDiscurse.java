package org.compiler.lex;

import java.util.Arrays;
import java.util.List;

public class DomainOfDiscurse {

    public static final Character LETRA = 'l';
    public static final Character NUMERO = 'n';

    public static List<Character> letras = Arrays.asList(new Character[] { 'a',
	    'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
	    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' });

    public static List<Character> numeros = Arrays.asList(new Character[] {
	    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' });

    public static List<String> palabrasReservadas = Arrays.asList(new String[] {
	    "si", "entonces", "sino", "imprimir", "entero", "entero_ss",
	    "iterar", "hasta", "vector", "de" });

    public static List<Character> todosCaracteresValidos = Arrays
	    .asList(new Character[] { LETRA, NUMERO, '_', '&', '(', ')', '{',
		    '}', '[', ']', '-', '+', '*', '/', '>', '<', '\'', ':',
		    '=', '^', ';', ',', '.', '\n', '\t', ' ' , '\r'});

    public static List<String> simbolos = Arrays.asList(new String[] { "(",
	    ")", "{", "}", "[", "]", "-", "+", "*", "/", ">", "<", ":=", "=",
	    "^=", ";", ",", "<=", ">=", ".." });

}
