package org.compiler.symboltable;

public class AttributeVector extends AttributeVariableID {

	private Long limInferior;
	private Long limSuperior;
	
	public AttributeVector(String typeOfToken, String typeOfElement, String typeOfId, Long limInferior,
			Long limSuperior) {
		super(typeOfToken, typeOfElement, typeOfId);
		this.limInferior = limInferior;
		this.limSuperior = limSuperior;
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

	@Override
	public String toString() {
		return super.toString() + " , " + limInferior + " , "+ limSuperior;
	}
	

}
