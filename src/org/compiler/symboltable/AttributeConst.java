package org.compiler.symboltable;

public class AttributeConst extends IAttribute {
	private String typeOfElement;
	
	public AttributeConst(String typet, String typee) {
		super(typet);
		this.typeOfElement = typee;
	}

	public String getTypeOfElement() {
		return typeOfElement;
	}

	public void setTypeOfElement(String typeOfElement) {
		this.typeOfElement = typeOfElement;
	}
	
	public String toString() {
		return  super.toString() + "," + typeOfElement ;
	}
	
}
