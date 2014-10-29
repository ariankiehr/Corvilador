package org.compiler.asm;

import java.util.HashMap;
import java.util.Map;

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
	
	public Boolean estaLibre(String registro) {
		
		return this.registros.get(registro);
	}
	
	public String obtenerRegistro() {
		
		if( this.estaLibre("AX") ){
			return "AX";
		}
		else if( this.estaLibre("BX") ){
			return "BX";
		}
		else if( this.estaLibre("CX" ) ){
			return "CX";
		}
		else if( this.estaLibre("DX" ) ){
			return "DX";
		}
		return null;
	}
	
	public void ocuparRegistro(String registro) {
		
		this.registros.put(registro,false);
	}
	
	public void desocuparRegistro(String registro) {
		this.registros.put(registro,true);
	}
	
}
