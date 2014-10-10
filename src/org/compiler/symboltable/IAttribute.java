package org.compiler.symboltable;

public class IAttribute {
	protected String typeOfToken;

	public IAttribute(String type) {
		this.typeOfToken = type;
	}

	@Override
	public String toString() {
		return typeOfToken;
	}

	public String getTypeOfToken() {
		return typeOfToken;
	}

	public void setTypeOfToken(String type) {
		this.typeOfToken = type;
	}
}