package org.compiler.symboltable;

public class AttributeCad extends AttributeComun {
	
	private String nombreAsm;

	public AttributeCad(String typeOfToken, String nombreAsm) {
		super(typeOfToken);
		this.nombreAsm = nombreAsm;
	}

	public String getNombreAsm() {
		return nombreAsm;
	}

	public void setNombreAsm(String nombreAsm) {
		this.nombreAsm = nombreAsm;
	}

	@Override
	public String toString() {
		return super.toString() + " , " + nombreAsm;
	}
	
}
