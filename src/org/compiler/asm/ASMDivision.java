package org.compiler.asm;

import java.util.LinkedList;
import java.util.List;

public class ASMDivision {
	
		private static final String regDivAX = "AX";
		private static final String regDivDX = "DX";
		private static ASMDivision instance = null;
		private List<String> sentencias; 
		private String elemento;
		
		private ASMDivision() {
			this.sentencias = new LinkedList<String> ();
		}
			
		

		public static ASMDivision getInstance() {
				if (instance == null) {
					instance = new ASMDivision();
				}
				return instance;
			
		}
		
		public String getElemento() {
			return this.elemento;
		}

}