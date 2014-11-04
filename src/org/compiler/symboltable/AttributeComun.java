package org.compiler.symboltable;


//Se usa para palabras reservadas o detecciones tempranas de elementos, cadenas etc
public class AttributeComun {
	protected String typeOfToken; //La forma en que ese token se reconoce dentro del lenguaje

	public AttributeComun(String typeOfToken) {
		super();
		this.typeOfToken = typeOfToken;
	}

	public String getTypeOfToken() {
		return typeOfToken;
	}

	public void setTypeOfToken(String typeOfToken) {
		this.typeOfToken = typeOfToken;
	}

	@Override
	public String toString() {
		return typeOfToken;
	}

}