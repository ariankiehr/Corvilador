package org.compiler.symboltable;

public class AttributeVector extends IAttribute {
	private String typeOfElement;
	private String typeOfId;
	private Long limInferior;
	private Long limSuperior;

	
	public AttributeVector (String type, String typee, String typei, Long limInf, Long limSup) {
		super(type);
		this.typeOfElement = typee;
		this.limInferior = limInf;
		this.limSuperior = limSup;
		this.typeOfId = typei;
		
	}
	
	public String toString() {
		return  super.toString() + "," + typeOfElement + "," + typeOfId + "," + limInferior + "," + limSuperior;
	}

	public String getTypeOfElement() {
		return typeOfElement;
	}

	public void setTypeOfElement(String typeOfElement) {
		this.typeOfElement = typeOfElement;
	}

	public String getTypeOfId() {
		return typeOfId;
	}

	public void setTypeOfId(String typeOfId) {
		this.typeOfId = typeOfId;
	}

	public Long getLimInferior() {
		return limInferior;
	}

	public void setLimInferior(Long limInferior) {
		this.limInferior = limInferior;
	}

	public Long getLimSuperior() {
		return limSuperior;
	}

	public void setLimSuperior(Long limSuperior) {
		this.limSuperior = limSuperior;
	}


}
