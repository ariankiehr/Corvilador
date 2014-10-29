package org.compiler.symboltable;

public class AttributeVariableID extends AttributeConTipo {

	protected String typeOfId; //tipo de variable, vector o simple

	public AttributeVariableID(String typeOfToken, String typeOfElement, String typeOfId) {
		super(typeOfToken, typeOfElement);
		this.typeOfId = typeOfId;
	}

	public String getTypeOfId() {
		return typeOfId;
	}

	public void setTypeOfId(String typeOfId) {
		this.typeOfId = typeOfId;
	}

	@Override
	public String toString() {
		return "AttributeVariableID [typeOfId=" + typeOfId + ", typeOfElement="
				+ typeOfElement + ", typeOfToken=" + typeOfToken + "]";
	}
	
}
