package org.compiler.asm;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.compiler.symboltable.AttributeCad;
import org.compiler.symboltable.AttributeComun;
import org.compiler.symboltable.AttributeVariableID;
import org.compiler.symboltable.AttributeVector;
import org.compiler.symboltable.SymbolTable;
import org.compiler.syntactic.Parser;

public class CodeGenerator {
	
	private PrintWriter fileWriter;

	public CodeGenerator() {
		initializeFile();
		
		//encabezado
		fileWriter.println(".386");
		fileWriter.println(".model flat, stdcall");
		fileWriter.println("option casemap :none");
		fileWriter.println("include \\masm32\\include\\windows.inc");
		fileWriter.println("include \\masm32\\include\\kernel32.inc");
		fileWriter.println("include \\masm32\\include\\user32.inc");
		//fileWriter.println("include \\masm32\\include\\masm32.inc");
		fileWriter.println("includelib \\masm32\\lib\\kernel32.lib");
		fileWriter.println("includelib \\masm32\\lib\\user32.lib");
		//fileWriter.println("includelib \\masm32\\lib\\masm32.lib");
		fileWriter.println(".DATA");
		List<String> declaraciones = generarDeclaraciones();
		for (String declaracion : declaraciones) {
			fileWriter.println( declaracion );
		}
		//Mensajes de errores
		fileWriter.println("indiceFueraDeRango db \"Indice fuera de rango\",0");
		//TODO TODOS LOS POSIBLES MENSAJES DE ERRORES
		fileWriter.println(".CODE");
		fileWriter.println("START:");
		List<String> sentencias = Parser.tree.getSentencias();
		for (String sentencia : sentencias) {
			fileWriter.println(sentencia);
		}
		fileWriter.println("END START");
		
		fileWriter.close();
	}
	
	
	public List<String> generarDeclaraciones() {

		List<String> ret = new LinkedList<String>();
		List<String> keys = SymbolTable.getInstance().getAllKeys();
		int contadorCadena = 1;
		
		for (String key : keys) {
			AttributeComun att = SymbolTable.getInstance().get(key);
			
			if( "id".equals(att.getTypeOfToken())  ) {
				AttributeVariableID attv = (AttributeVariableID) att;
				
				if( "variable".equals(attv.getTypeOfId()) ) {
					ret.add("_" + key + " DW 0" );
				} else if( "vector".equals(attv.getTypeOfId()) ) {
					AttributeVector attvect = (AttributeVector)attv;
					Long size = (attvect.getLimSuperior() - attvect.getLimInferior() + 1) * 2;
					ret.add("_" + key + " DW " + size + " DUP(0)");
				}
				
			}
			if( "cadena".equals(att.getTypeOfToken())  ) {
				String nombre = "_cadena"+ contadorCadena;
				ret.add(nombre + " DB " + key + ",0");
				SymbolTable.getInstance().removeSymbol(key);
				SymbolTable.getInstance().addSymbol(key, new AttributeCad("cadena", nombre));
				contadorCadena++;
			}
			
			
		}
		
		return ret;
	}

	public void initializeFile() {
		try {
			fileWriter = new PrintWriter("source.asm", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
}
