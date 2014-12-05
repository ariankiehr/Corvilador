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
	private File archivoAsm, archivoObj;
	private static Stack<String> labels;
	private static Integer labelId, contadorCadena;
	private static boolean swapAX, swapDX, swapCX, swapGen;


	public CodeGenerator(File file) {
		initializeFile(file);
		labels = new Stack<String>();
		swapAX = false;
		swapDX = false;
		swapCX = false;
		swapGen = false;
		labelId = 0;
		contadorCadena = 0;
		
		List<String> sentencias = Parser.tree.getSentencias();
		
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
	
	
	public static int getContadorCadena() {
		return contadorCadena++;
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
	
	
	public static void useSwapAX() {
		swapAX = true;
	}
	
	public static void useSwapDX() {
		swapDX = true;
	}
	
	public static void useSwapCX() {
		swapCX = true;
	}
	
	public static void useSwapGen() {
		swapGen = true;
	}
	
	
	public List<String> generarDeclaraciones() {

		List<String> ret = new LinkedList<String>();
		List<String> keys = SymbolTable.getInstance().getAllKeys();
		

		if( swapDX == true) {
			ret.add("@swap_DX DW 0");
		}
		
		if( swapAX == true) {
			ret.add("@swap_AX DW 0");
		}
		
		if( swapCX == true) {
			ret.add("@swap_CX DW 0");
		}
		
		if( swapGen == true) {
			ret.add("@swapgen DW 0");
		}
		
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
				ret.add(   ((AttributeCad)att).getNombreAsm() + " DB " + key + ",0");
			}
			
			
		}
		
		return ret;
	}

	public void initializeFile(File file) {
		String nombreArchivo = null;
		if(file.getName().indexOf('.')>0) {
			nombreArchivo = file.getName().substring(0, file.getName().indexOf('.'));
		} else {
			nombreArchivo = file.getName();
		}

		
		archivoAsm = new File(file.getParentFile() + File.separator + nombreArchivo + ".asm");
		archivoObj = new File(file.getParentFile() + File.separator + nombreArchivo + ".obj");

		try {
			fileWriter = new PrintWriter(archivoAsm);
		} catch (FileNotFoundException e) {
			System.out.println( e.getMessage() );
		}

	}


	public File getArchivoAsm() {
		return archivoAsm;
	}


	public void setArchivoAsm(File archivoAsm) {
		this.archivoAsm = archivoAsm;
	}


	public File getArchivoObj() {
		return archivoObj;
	}


	public void setArchivoObj(File archivoObj) {
		this.archivoObj = archivoObj;
	}
	
	
}
