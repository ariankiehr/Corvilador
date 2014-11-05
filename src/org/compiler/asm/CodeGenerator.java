package org.compiler.asm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.compiler.symboltable.AttributeCad;
import org.compiler.symboltable.AttributeComun;
import org.compiler.symboltable.AttributeVariableID;
import org.compiler.symboltable.AttributeVector;
import org.compiler.symboltable.SymbolTable;
import org.compiler.syntactic.Parser;

public class CodeGenerator {
	
	private PrintWriter fileWriter;
	private static Stack<String> labels;
	private static Integer labelId = 0;


	public CodeGenerator(File file) {
		initializeFile(file);
		labels = new Stack<String>();
		
		//encabezado
		fileWriter.println(".386");
		fileWriter.println(".model flat, stdcall");
		fileWriter.println("option casemap :none");
		fileWriter.println("include \\masm32\\include\\windows.inc");
		fileWriter.println("include \\masm32\\include\\kernel32.inc");
		fileWriter.println("include \\masm32\\include\\user32.inc");
		fileWriter.println("includelib \\masm32\\lib\\kernel32.lib");
		fileWriter.println("includelib \\masm32\\lib\\user32.lib");
		fileWriter.println(".DATA");
		List<String> declaraciones = generarDeclaraciones();
		for (String declaracion : declaraciones) {
			fileWriter.println( declaracion );
		}

		fileWriter.println("overflowProducto db \"Overflow en la multiplicacion\",0");
		fileWriter.println("indiceFueraDeRangoSup db \"Indice fuera de rango superior\",0");
		fileWriter.println("indiceFueraDeRangoInf db \"Indice fuera de rango inferior\",0");

		fileWriter.println(".CODE");
		fileWriter.println("START:");


		List<String> sentencias = Parser.tree.getSentencias();
		for (String sentencia : sentencias) {
			fileWriter.println(sentencia);
		}
		
		fileWriter.println("invoke ExitProcess, 0");
		fileWriter.println("overflow:");
		fileWriter.println("invoke MessageBox, NULL, addr overflowProducto, addr overflowProducto, MB_OK");
		fileWriter.println("invoke ExitProcess, 0");
		fileWriter.println("indiceFueraRangoSup:");
		fileWriter.println("invoke MessageBox, NULL, addr indiceFueraDeRangoSup, addr indiceFueraDeRangoSup, MB_OK");
		fileWriter.println("invoke ExitProcess, 0");
		fileWriter.println("indiceFueraRangoInf:");
		fileWriter.println("invoke MessageBox, NULL, addr indiceFueraDeRangoInf, addr indiceFueraDeRangoInf, MB_OK");
		fileWriter.println("invoke ExitProcess, 0");
		fileWriter.println("END START");
		
		fileWriter.close();
	}
	
	
	public static String popLabel() {
		return labels.pop();
	}
	
	public static String getLabel() {
		return labels.peek();
	}
	
	public static void pushLabel() {
		labels.push("label"+(labelId++));
	}
	


	public List<String> generarDeclaraciones() {

		List<String> ret = new LinkedList<String>();
		List<String> keys = SymbolTable.getInstance().getAllKeys();
		int contadorCadena = 1;
		ret.add("@swap_AX DW 0");
		for (String key : keys) {
			AttributeComun att = SymbolTable.getInstance().get(key);
			
			if( "id".equals(att.getTypeOfToken())  ) {
				AttributeVariableID attv = (AttributeVariableID) att;
				
				if( "variable".equals(attv.getTypeOfId()) ) {
					ret.add("_" + key + " DW 0" );
				} else if( "vector".equals(attv.getTypeOfId()) ) {
					AttributeVector attvect = (AttributeVector)attv;
					Long size = (attvect.getLimSuperior() - attvect.getLimInferior() + 1);
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

	public void initializeFile(File file) {
		
		String nombreArchivo = file.getName().substring(0, file.getName().indexOf('.'));
		
		File arch = new File(file.getParentFile() + File.separator + nombreArchivo + ".asm");

		try {
			fileWriter = new PrintWriter(arch);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	
}
