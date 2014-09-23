%{
import java.lang.Math;
import org.compiler.lex.LexicalAnalyzer;
import java.util.*;
import org.compiler.symboltable.SymbolTable;
import org.compiler.lex.Token;
%}

/* YACC Declarations */
%token IF ELSE PRINT INT UINT DO UNTIL VECTOR OF THEN NUM ID CAD
%token MAYORIGUAL MENORIGUAL ASIG IGUAL ABREPAR CIERRAPAR
%token ABRELLAV CIERRALLAV ABRECOR CIERRACOR MENOS MAS POR DIV MENOR MAYOR PUNTOCOMA COMA IGUALRARO DOSPUNTO



/* Grammar follows */
%%
programa : 
 			| programa line
		 ;
		 
line : '\n'
			| assig PUNTOCOMA
;

assig : INT variables { detections.add("Declaracion de variables."+lineNumber); } 
;

variables : ID
			| variables COMA ID
			;


%%



String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;


void yyerror(String s)
{
 errors.add(s);
}

boolean newline;
int yylex()
{
String s;
int tok;
Double d;
 if (!la.hasMoreTokens())
 if (!newline)
 {
 newline=true;
 return '\n'; //So we look like classic YACC example
 }
 else
 return 0;
 Token t = la.nextToken();
 s = t.getLexem();
 lineNumber = t.getLine();
  String type = SymbolTable.getInstance().get(s).getType();
 if( type.equals("id") ) {
     tok = ID;
 } else if (type.equals("const")) {
     tok = INT; /*diferenciar con uint*/
 } else if (type.equals("cadena")) {
     tok = CAD; /*diferenciar con uint*/
 } else {
 	 tok = toInteger(s);
 }

 return tok;
}


private static Map<String, Integer> generateHash() {
	Map<String, Integer> hash = new HashMap<String, Integer>();

	hash.put("si", (int) Parser.IF) ;
	hash.put("entonces", (int) Parser.THEN) ;
	hash.put("sino", (int) Parser.ELSE) ;
	hash.put("imprimir", (int) Parser.PRINT) ;
	hash.put("entero", (int) Parser.INT) ;
	hash.put("entero_ss", (int) Parser.UINT) ;
	hash.put("iterar", (int) Parser.DO) ;
	hash.put("hasta", (int) Parser.UNTIL) ;
	hash.put("vector", (int) Parser.VECTOR) ;
	hash.put("de", (int) Parser.OF) ;
	hash.put(">=", (int) Parser.MAYORIGUAL) ;
	hash.put("<=", (int) Parser.MENORIGUAL) ;
	hash.put(":=", (int) Parser.ASIG) ;
	hash.put("=", (int) Parser.IGUAL) ;
	hash.put("(", (int) Parser.ABREPAR) ;
	hash.put(")", (int) Parser.CIERRAPAR) ;
	hash.put("{", (int) Parser.ABRELLAV) ;
	hash.put("}", (int) Parser.CIERRALLAV) ;
	hash.put("[", (int) Parser.ABRECOR) ;
	hash.put("]", (int) Parser.CIERRACOR) ;
	hash.put("-", (int) Parser.MENOS) ;
	hash.put("+", (int) Parser.MAS) ;
	hash.put("*", (int) Parser.POR) ;
	hash.put("/", (int) Parser.DIV) ;
	hash.put("<", (int) Parser.MENOR) ;
	hash.put(">", (int) Parser.MAYOR) ;
	hash.put(";", (int) Parser.PUNTOCOMA) ;
	hash.put(",", (int) Parser.COMA) ;
	hash.put("^=", (int) Parser.IGUALRARO) ;
	hash.put("..", (int) Parser.DOSPUNTO) ;

	
	
	return hash ;
}
    
private int toInteger( String token ) { 
	Integer value = hm.get( token ) ;
	if ( value == null ) {
		value = (int)token.charAt(0) ; //ASCII
	}
	return value ;		
}



public void dotest(LexicalAnalyzer lex)
{
 la = lex;
 errors = new LinkedList<String>();
 detections = new LinkedList<String>();
 newline=false;
 yyparse();
}
