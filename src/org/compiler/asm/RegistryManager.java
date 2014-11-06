package org.compiler.asm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RegistryManager {
	
	private Map<String,Boolean> registros;
	private static RegistryManager instance;
	
	private RegistryManager (){
		
		this.registros = new HashMap<String,Boolean> ();
		this.registros.put("AX", new Boolean(true));
		this.registros.put("BX", new Boolean(true));
		this.registros.put("CX", new Boolean(true));
		this.registros.put("DX", new Boolean(true));

	}
	
	public static RegistryManager getInstance() {
		if( instance == null ) {
			instance = new RegistryManager();
		}
		return instance;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Boolean>> iter = this.registros.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Boolean> entry = iter.next();

			sb.append(entry.getKey());
			sb.append("->");
			sb.append(entry.getValue());
			sb.append(" : ");
		}
		sb.append('\n');

		return sb.toString();
		
	}
	
	
	public Boolean estaLibre(String registro) {
		
		return this.registros.get(registro);
	}
	
	public String obtenerRegistro() throws FullRegistersException {
		
		if( this.estaLibre("CX") ) {
			return "CX";
		}
		else if( this.estaLibre("BX") ){
			return "BX";
		}
		else if( this.estaLibre("DX" ) ){
			return "DX";
		}
		else if( this.estaLibre("AX" ) ){
			return "AX";
		}
		
		throw new FullRegistersException("No hay mas registros libres");
		//return null;
	}
	
	public void ocuparRegistro(String registro) {
		
		if( registro == null ) {
			System.exit(1);
		}
		
		this.registros.put(registro,false);
	}
	
	public void desocuparRegistro(String registro) {
		
		if( registro == null ) {
	
			System.exit(1);
		}
		
		this.registros.put(registro,true);
	}
	
}
