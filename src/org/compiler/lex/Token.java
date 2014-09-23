package org.compiler.lex;

public class Token {
    private String type;
    private String lexem;
    private int line;

    public Token(String type, String lexem, int line) {
	super();
	this.type = type;
	this.lexem = lexem;
	this.line = line;
    }
    
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }


    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getLexem() {
	return lexem;
    }

    public void setLexem(String lexem) {
	this.lexem = lexem;
    }

    public boolean isEOF() {
	return lexem.equals("EOF");
    }

    @Override
    public String toString() {
	return "Token [type=" + type + ", lexem=" + lexem + ", line=" + line
		+ "]";
    }

}
