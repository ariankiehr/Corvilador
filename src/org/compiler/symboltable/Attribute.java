package org.compiler.symboltable;

public class Attribute {
    private String type;

    public Attribute(String type) {
	this.type = type;
    }

    @Override
    public String toString() {
	return type;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }
}
