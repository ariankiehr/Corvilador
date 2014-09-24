%{
import org.compiler.lex.LexicalAnalyzer;
import java.util.*;
import org.compiler.symboltable.SymbolTable;
import org.compiler.lex.Token;
%}

/* YACC Declarations */
%token IF ELSE PRINT INT UINT DO UNTIL VECTOR OF THEN CTE ID CAD
%token MAYORIGUAL MENORIGUAL ASIG IGUAL ABREPAR CIERRAPAR
%token ABRELLAV CIERRALLAV ABRECOR CIERRACOR MENOS MAS POR DIV MENOR MAYOR PUNTOCOMA COMA IGUALRARO DOSPUNTO

%nonassoc LOWER_THAN_ELSE
%nonassoc ELSE

%start programa

/* Grammar follows */
%%
			
programa : 
 	| sentencias_declarativas 
	| sentencias_ejecutables  
	| sentencias_declarativas sentencias_ejecutables
;

sentencias_declarativas : sentencias_declarativas_simples PUNTOCOMA
| sentencias_declarativas sentencias_declarativas_simples PUNTOCOMA
;

sentencias_declarativas_simples : tipo variables { detections.add("Declaracion de variable comun en linea "+previousTokenLineNumber); }
| ID ABRECOR INT DOSPUNTO INT CIERRACOR VECTOR OF tipo { detections.add("Declaracion de variable vector en linea "+previousTokenLineNumber); }
| error { yyerror("Declaracion de variables mal hecha en " + previousTokenLineNumber); }
;

tipo : INT
| UINT
;

variables : ID
| variables COMA ID
;

bloque_sentencias : sentencia
| ABRELLAV sentencias_ejecutables CIERRALLAV
;

sentencias_ejecutables : sentencia
| sentencias_ejecutables sentencia
;

sentencia : PRINT CAD PUNTOCOMA { detections.add("Declaracion imprimir en "+previousTokenLineNumber + " cadena "+ $2.sval); }
| seleccion
| asignacion PUNTOCOMA
| iteracion PUNTOCOMA
;

asignacion : variable ASIG expresion
;

iteracion : DO bloque_sentencias UNTIL condicion 
;


seleccion : cabecera_seleccion THEN cuerpo_seleccion
| error { yyerror("if mal hecho en " + previousTokenLineNumber); }
;	

cuerpo_seleccion : 	bloque_then bloque_else
				 | bloque_final
				 ;

bloque_then : bloque_sentencias ELSE				 
			;

bloque_final : bloque_sentencias %prec LOWER_THAN_ELSE { detections.add("Declaracion if en "+previousTokenLineNumber ); }		
;
			
bloque_else : bloque_sentencias { detections.add("Declaracion if else en "+previousTokenLineNumber); }
			;

cabecera_seleccion : IF ABREPAR condicion CIERRAPAR
				   ;

condicion : expresion comparador expresion;

expresion : expresion MAS termino
		  | expresion MENOS termino
		  | termino
		  ;
 
termino : termino POR factor
		| termino DIV factor
		| factor
		;
		
variable :  ID
| ID ABRECOR expresion CIERRACOR
;
		
factor : ID
	   | CTE
	   | ID ABRECOR expresion CIERRACOR
	   ;
	   
comparador : IGUAL 
|MAYORIGUAL 
|MENORIGUAL
|MENOR 
|MAYOR 
;

%%

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private int previousTokenLineNumber = 0;


void yyerror(String s) {
	errors.add(s);
}

int yylex() {
	String s;
	int tok;
	
	if (!la.hasMoreTokens()) {
		 	return 0;
	}
	
	Token t = la.nextToken();
	s = t.getLexem();
	
	lineNumber = t.getLine();
	asd=s;
	previousTokenLineNumber = lineNumber; //subir arriba para que ande con anterior
	String type = SymbolTable.getInstance().get(s).getType();
	
	

	if( type.equals("id") ) {
	    tok = ID;
	} else if (type.equals("const")) {
	    tok = CTE; /*diferenciar con uint*/
	    yylval = new ParserVal( Integer.parseInt(s) ) ; 
	} else if (type.equals("cadena")) {
	    tok = CAD;
	    yylval = new ParserVal( s ) ; 
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



public void dotest(LexicalAnalyzer lex) {
 la = lex;
 errors = new LinkedList<String>();
 detections = new LinkedList<String>();
 yyparse();
}
