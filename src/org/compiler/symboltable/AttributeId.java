package org.compiler.symboltable;

public class AttributeId extends IAttribute {
	private String typeOfElement;
	private String typeOfId;
	private Long valor;

	public AttributeId(String typet, String typee, String typei, Long val) {
		super(typet);
		this.typeOfElement = typee;
		this.typeOfId = typei;
		this.valor = val;
	}

	public Long getValor() {
		return this.valor;
	}
	
	public void setValor(Long val) {
		this.valor = val;
	}
	
	@Override
	public String toString() {
		return  super.toString() + "," + typeOfElement + "," + typeOfId + "," + valor;
	}
	
	public String getTypeOfElement() {
		return typeOfElement;
	}

	public void setTypeOfElement(String type) {
		this.typeOfElement = type;
	}
	
	public void setTypeOfId(String type) {
		this.typeOfId = type;
	}
	
	public String getTypeOfId() {
		return typeOfId;
	}
	
}
