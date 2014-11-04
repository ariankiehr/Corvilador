package org.compiler.symboltable;

public class AttributeConTipo extends AttributeComun {
	protected String typeOfElement; // Si posee un tipo de dato (entero, entero_ss)

	public AttributeConTipo(String typeOfToken, String typeOfElement) {
		super(typeOfToken);
		this.typeOfElement = typeOfElement;
	}

	public String getTypeOfElement() {
		return typeOfElement;
	}

	public void setTypeOfElement(String typeOfElement) {
		this.typeOfElement = typeOfElement;
	}

	@Override
	public String toString() {
		return super.toString() + " , " + typeOfElement;
	}

	
}
