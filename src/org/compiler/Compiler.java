package org.compiler;

import java.io.File;

import org.compiler.lex.LexicalAnalyzer;
import org.compiler.lex.Token;

public class Compiler {

	public static void main(String[] args) {

		LexicalAnalyzer lex = new LexicalAnalyzer(
				new File(
						"C:\\Users\\Mariel\\Desktop\\Corvilador\\Corvilador\\src\\keywords"));
		
		//lex.printSymbolTable();
		
		/*Token tk;
		while( !(tk = lex.getNextToken()).isEOF() ) {
			System.out.println(tk);
		}*/
		

	}

}
