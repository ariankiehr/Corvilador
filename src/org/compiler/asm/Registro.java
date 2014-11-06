package org.compiler.asm;

class Registro {
	private boolean libre;
	private String owner;
	
	public Registro(boolean ocupado, String owner) {
		super();
		this.libre = ocupado;
		this.owner = owner;
	}

	public boolean isLibre() {
		return libre;
	}

	public void setLibre(boolean libre) {
		this.libre = libre;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Registro [libre=" + libre + ", owner=" + owner + "]";
	}
	
	
	
	
}