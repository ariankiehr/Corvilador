%{
import org.compiler.lex.LexicalAnalyzer;
import java.util.*;
import org.compiler.symboltable.SymbolTable;
import org.compiler.lex.Token;
%}

%token IF ELSE PRINT INT UINT DO UNTIL VECTOR OF THEN CTE ID CAD
%token MAYORIGUAL MENORIGUAL ASIG IGUAL ABREPAR CIERRAPAR
%token ABRELLAV CIERRALLAV ABRECOR CIERRACOR MENOS MAS POR DIV MENOR MAYOR PUNTOCOMA COMA DISTINTO DOSPUNTO

%nonassoc LOWER_THAN_ELSE
%nonassoc ELSE

%start programa
%%

programa : 
 	| sentencias_declarativas
	| sentencias_ejecutables
	| sentencias_declarativas sentencias_ejecutables
;

sentencias_declarativas : 	sentencias_declarativas_simples PUNTOCOMA
						| 	sentencias_declarativas sentencias_declarativas_simples PUNTOCOMA									
;

sentencias_declarativas_simples : tipo variables  { detections.add("Declaracion de variable comun en linea " + 										lineNumber); }
								| 	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo { detections.add("Declaracion de variable vector en linea " + lineNumber); }
								//|	tipo variables  {yyerror("Error: Falta ';' en la declaracion en linea " + lineNumber);}
								//| 	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo  {yyerror("Error: Falta ';' en la declaracion del vector en linea " + lineNumber);}
								//| 	ID error CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo {yyerror("Error: se esperaba un '[' eb linea "+ lineNumber);}
								| 	ID ABRECOR error DOSPUNTO CTE CIERRACOR VECTOR OF tipo   {yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
								| 	ID ABRECOR CTE error CTE CIERRACOR VECTOR OF tipo   {yyerror("Error: Se esperaba '..' en linea " + lineNumber);}
								|	ID ABRECOR CTE DOSPUNTO error CIERRACOR VECTOR OF tipo   {yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
								|	ID ABRECOR CTE DOSPUNTO CTE error VECTOR OF tipo   {yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
								|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR error OF tipo   {yyerror("Error: Falta la palabra reservada 'VECTOR' en linea " + lineNumber);}	
								|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR error tipo   {yyerror("Error: Falta la palabra reservada 'DE' en linea " + lineNumber);}		
								|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF error   {yyerror("Error: Falta tipo del vector en linea " + lineNumber);}	
								|	error ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo   {yyerror("Error: Nombre variable en linea " + lineNumber);}
;

tipo : INT
	|	UINT
;

variables : ID
		|	variables COMA ID		
;

bloque_sentencias : sentencia PUNTOCOMA
					|	ABRELLAV sentencias_ejecutables CIERRALLAV
					
;

sentencias_ejecutables : sentencia PUNTOCOMA
						| sentencias_ejecutables sentencia PUNTOCOMA
						| seleccion
						| sentencias_ejecutables seleccion
						| sentencias_ejecutables error PUNTOCOMA  {yyerror("Codigo erroneo" + lineNumber);}
						| error PUNTOCOMA {yyerror("Codigo erroneo" + lineNumber);}

;

sentencia : PRINT ABREPAR CAD CIERRAPAR { detections.add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ $3.sval); }
		| 	asignacion 
		|	iteracion 			
;

asignacion : variable ASIG expresion { detections.add("Asignacion en linea  "+lineNumber); }
;

iteracion : DO bloque_sentencias UNTIL ABREPAR condicion CIERRAPAR { detections.add("Iterar en linea  "+lineNumber); }
			|	DO  UNTIL ABREPAR condicion CIERRAPAR {yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);} 
			|	DO bloque_sentencias ABREPAR condicion CIERRAPAR {yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
			|	DO bloque_sentencias UNTIL condicion CIERRAPAR {yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);} 
			|	DO bloque_sentencias UNTIL ABREPAR condicion  {yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);} 		
;

seleccion : cabecera_seleccion THEN cuerpo_seleccion
	| cabecera_seleccion cuerpo_seleccion {yyerror("vazco gato"+ lineNumber); }
; 
	

cuerpo_seleccion : 	bloque_then bloque_else
				|	bloque_final
;

bloque_then : bloque_sentencias ELSE				 
;

bloque_final : bloque_sentencias %prec LOWER_THAN_ELSE { detections.add("Declaracion if en linea "+lineNumber); }		
;

bloque_else : bloque_sentencias { detections.add("Declaracion if else en linea "+lineNumber); }
;

cabecera_seleccion : 	IF ABREPAR condicion CIERRAPAR
					|	IF error condicion CIERRAPAR {yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
					|	IF condicion CIERRAPAR {yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
;

condicion : expresion comparador expresion
;

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
	| ID ABRECOR CIERRACOR {yyerror("Error: Se espera una exprecion entre los corchetes en linea "+ lineNumber);}
	| ID ABRECOR expresion {yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
	| ID CIERRACOR {yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}
;
		
factor : ID
	   | CTE
	   | ID ABRECOR expresion CIERRACOR
   	   | MENOS CTE {
   	   		if ($2.ival > 32768 ) {
   	   			yyerror("Numero negativo debajo del rango en linea "+lineNumber); 
   	   		}
   	   		/* Aca hay que agregar a la tabla de simbolos el - y sacar el otro, pero hay que usar semantico */
   	   	}
;
	   
comparador : IGUAL 
			|MAYORIGUAL 
			|MENORIGUAL
			|MENOR 
			|MAYOR
			|DISTINTO
;


%%

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private String s;


void yyerror(String s) {
	if("syntax error".equals(s)) {
		errors.add("Error: linea erronea en "+ lineNumber);
	} else {
		errors.add(s);	
	}

}

int yylex() {

	int tok;
	
	if (!la.hasMoreTokens()) {
		 	return 0;
	}
	
	Token t = la.nextToken();
	s = t.getLexem();
	
	lineNumber = t.getLine();
	String type = SymbolTable.getInstance().get(s).getType();

	if( type.equals("id") ) {
	    tok = ID;
	} else if (type.equals("const")) {
	    tok = CTE;
	    yylval = new ParserVal( new Long(Long.parseLong(s)) ) ; 
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

	hash.put("si", (int) Parser.IF);
	hash.put("entonces", (int) Parser.THEN);
	hash.put("sino", (int) Parser.ELSE);
	hash.put("imprimir", (int) Parser.PRINT);
	hash.put("entero", (int) Parser.INT);
	hash.put("entero_ss", (int) Parser.UINT);
	hash.put("iterar", (int) Parser.DO);
	hash.put("hasta", (int) Parser.UNTIL);
	hash.put("vector", (int) Parser.VECTOR);
	hash.put("de", (int) Parser.OF);
	hash.put(">=", (int) Parser.MAYORIGUAL);
	hash.put("<=", (int) Parser.MENORIGUAL);
	hash.put(":=", (int) Parser.ASIG);
	hash.put("=", (int) Parser.IGUAL);
	hash.put("(", (int) Parser.ABREPAR);
	hash.put(")", (int) Parser.CIERRAPAR);
	hash.put("{", (int) Parser.ABRELLAV);
	hash.put("}", (int) Parser.CIERRALLAV);
	hash.put("[", (int) Parser.ABRECOR);
	hash.put("]", (int) Parser.CIERRACOR);
	hash.put("-", (int) Parser.MENOS);
	hash.put("+", (int) Parser.MAS);
	hash.put("*", (int) Parser.POR);
	hash.put("/", (int) Parser.DIV);
	hash.put("<", (int) Parser.MENOR);
	hash.put(">", (int) Parser.MAYOR);
	hash.put(";", (int) Parser.PUNTOCOMA);
	hash.put(",", (int) Parser.COMA);
	hash.put("^=", (int) Parser.DISTINTO);
	hash.put("..", (int) Parser.DOSPUNTO);

	return hash;
}
    
private int toInteger( String token ) { 
	Integer value = hm.get( token );
	if ( value == null ) {
		value = (int)token.charAt(0);
	}
	return value;		
}



public void parsear(LexicalAnalyzer lex) {
 la = lex;
 errors = new LinkedList<String>();
 detections = new LinkedList<String>();
 yyparse();
}
