%{
import org.compiler.lex.LexicalAnalyzer;
import java.util.*;
import org.compiler.symboltable.*;
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

sentencias_declarativas : 	sentencias_declarativas_simples 
						| 	sentencias_declarativas sentencias_declarativas_simples 									
;

sentencias_declarativas_simples : 
					tipo variables PUNTOCOMA {
						List<String> vars = (List<String>)$2.obj;
						for( String s : vars ) {
							SymbolTable.getInstance().addSymbol( s , new AttributeId( 
								SymbolTable.getInstance().get(s).getTypeOfToken(),  $1.sval, "variable" , null ) );
							if (!estaDeclarada(s)) {
								declaradas.add(s);
							}
							else {
								yyerror("La variable ya esta declarada" + lineNumber);
							}
						}
	
						add("Declaracion de variable comun en linea " + lineNumber);
					}
					| 	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA {
	
							SymbolTable.getInstance().addSymbol( $1.sval, new AttributeVector( 
								SymbolTable.getInstance().get($1.sval).getTypeOfToken(), $9.sval, "vector", (Long)$3.obj, (Long)$3.obj ) ); 
		
								if ( !esEntero((Long)$3.obj) || !esEntero((Long)$5.obj)) {
									yyerror("no son enteros" + lineNumber);
								}
								else if ( (Long)$3.obj < 0 ) {
									yyerror("la constante es menor que 0" + lineNumber);
								}
								else if ( (Long)$3.obj > (Long)$5.obj ){
									yyerror("el limite inferior es mayor al limite superior" + lineNumber);
								}
								else {
									SymbolTable.getInstance().addSymbol( String.valueOf((Long)$3.obj), new AttributeConst( 
										SymbolTable.getInstance().get(String.valueOf((Long)$3.obj)).getTypeOfToken(), "entero") );
									SymbolTable.getInstance().addSymbol( String.valueOf((Long)$5.obj), new AttributeConst( 
										SymbolTable.getInstance().get(String.valueOf((Long)$5.obj)).getTypeOfToken(), "entero") );
									if ( !estaDeclarada($1.sval) ) {
										declaradas.add($1.sval);
									}
									else {
										yyerror("La variable ya esta declarada" + lineNumber);
									}
								}
										
						add("Declaracion de variable vector en linea " + lineNumber); }
					| 	ID ABRECOR error DOSPUNTO CTE CIERRACOR VECTOR OF tipo   {yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
					| 	ID ABRECOR CTE error CTE CIERRACOR VECTOR OF tipo   {yyerror("Error: Se esperaba '..' en linea " + lineNumber);}
					|	ID ABRECOR CTE DOSPUNTO error CIERRACOR VECTOR OF tipo   {yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
					|	ID ABRECOR CTE DOSPUNTO CTE error VECTOR OF tipo   {yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
					|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR error OF tipo   {yyerror("Error: Falta la palabra reservada 'VECTOR' en linea " + lineNumber);}	
					|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR error tipo   {yyerror("Error: Falta la palabra reservada 'DE' en linea " + lineNumber);}		
					|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF error   {yyerror("Error: Falta tipo del vector en linea " + lineNumber);}	
					|	error ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo   {yyerror("Error: Nombre variable en linea " + lineNumber);}
					| ID ID  {yyerror("Error: Palabra reservada mal escrita en linea " + lineNumber);}
					| tipo tipo  {yyerror("Error: Nombre de variable igual al tipo en linea " + lineNumber);}
;

tipo : INT { $$ = new ParserVal("entero");  }
	|	UINT { $$ = new ParserVal("entero_ss");  }
;

variables : ID { List<String> vars = new LinkedList<String>(); 
				vars.add( $1.sval );
				$$ = new ParserVal(vars); 
			}
		|	variables COMA ID { List<String> vars = new LinkedList<String>(); 
								vars.add( $3.sval );
								vars.addAll( (LinkedList<String>)$1.obj );
								$$ = new ParserVal(vars);
		}		
;

bloque_sentencias : sentencia PUNTOCOMA
					|	ABRELLAV sentencias_ejecutables CIERRALLAV
					|	error sentencias_ejecutables CIERRALLAV {yyerror("Error: Se esperaba '{' " + lineNumber);}
					|	ABRELLAV sentencias_ejecutables error {yyerror("Error: Se esperaba '}'" + lineNumber);}
					
;

sentencias_ejecutables : sentencia PUNTOCOMA
						| sentencias_ejecutables sentencia PUNTOCOMA
						| seleccion
						| sentencias_ejecutables seleccion
						| sentencias_ejecutables error PUNTOCOMA  {yyerror("Codigo erroneo en linea " + lineNumber);}
						| error PUNTOCOMA {yyerror("Codigo erroneo en linea " + lineNumber);}

;

sentencia : PRINT ABREPAR CAD CIERRAPAR { add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ $3.sval); }
		| 	asignacion 
		|	iteracion 			
		|	PRINT error CAD CIERRAPAR {yyerror("Error: Se espera un '(' " + lineNumber);}
		|	PRINT ABREPAR CAD error {yyerror("Error: Se espera un ')' " + lineNumber);}
		|	PRINT ABREPAR error CIERRAPAR {yyerror("Error: Se espera una 'cadena' " + lineNumber);}
;

asignacion : variable ASIG expresion {

							if ( !estaDeclarada($1.sval) ) {
								yyerror("La variable " + $1.sval + "que intenta utilizar no ha sido declarada en la linea " + lineNumber);
							}


							 add("Asignacion en linea  "+lineNumber); }

							 //errores de tipos. ASSEMBLER
							 //	declaracion de tipo de constantes
							 //errores de rango ASSEMBLER
							 //CONSTANTES QUE CORRESPONDEN A 2 TIPOS?? (EJ: b_ss = 1)




;

iteracion : DO bloque_sentencias UNTIL ABREPAR condicion CIERRAPAR { add("Iterar en linea  "+lineNumber); }
			|	DO  UNTIL ABREPAR condicion CIERRAPAR {yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);} 
			|	DO bloque_sentencias ABREPAR condicion CIERRAPAR {yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
			|	DO bloque_sentencias UNTIL condicion CIERRAPAR {yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);} 
			|	DO bloque_sentencias UNTIL ABREPAR condicion  {yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);} 		
;

seleccion : cabecera_seleccion THEN cuerpo_seleccion
	| cabecera_seleccion cuerpo_seleccion {yyerror("Error: falta entonces"+ lineNumber); }
; 
	

cuerpo_seleccion : 	bloque_then bloque_else
				|	bloque_final
;

bloque_then : bloque_sentencias ELSE				 
;

bloque_final : bloque_sentencias %prec LOWER_THAN_ELSE { add("Declaracion if en linea "+lineNumber); }		
;

bloque_else : bloque_sentencias { add("Declaracion if else en linea "+lineNumber); }
;

cabecera_seleccion : 	IF ABREPAR condicion CIERRAPAR
					|	IF error condicion CIERRAPAR {yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
					|	IF condicion CIERRAPAR {yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
;

condicion : expresion comparador expresion 
			
			//mismos tipos para la comparacion
;

expresion : expresion MAS termino
		  | expresion MENOS termino	  
		  | termino {

			$$ = new ParserVal($1);

		  }

		  //mismos tipos para las operaciones
		  //calcular la expresion ASSEMBLER
;
 
termino : termino POR factor
		| termino DIV factor
		| factor {

			$$ = new ParserVal($1);
		}

		//mismos tipos para las operaciones
		//calcular la expresion ASSEMBLER
;
		
variable :  ID {

			if( !estaDeclarada($1.sval) ) {
				yyerror("no esta declarada la variable");
			}
}
	| ID ABRECOR expresion CIERRACOR {

		if( !estaDeclarada($1.sval) ) {
				yyerror("no esta declarada la variable");
		}
	
		//tiene que ser entero, mayor que 0
		//tiene que estar dentro del rango

	}
	| ID ABRECOR error CIERRACOR   {yyerror("Error: Se espera una expresion entre los corchetes en linea "+ lineNumber);}
	| ID ABRECOR expresion  {yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
	| ID CIERRACOR {yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}


;
		
factor : ID {

			if( !estaDeclarada($1.sval) ) {
				yyerror("no esta declarada la variable");
			}
			//devolver el valor  que esta en la tabla de simbolos como nuevo atributo
			//si el id no tiene valor tirar error o DEFAULT?
}
	   | CTE {

	   		$$ = new ParserVal($1.obj);
	   		SymbolTable.getInstance().addSymbol( String.valueOf((Long)$1.obj), new AttributeConst( 
				SymbolTable.getInstance().get(String.valueOf((Long)$1.obj)).getTypeOfToken(), "entero") );
	   		//devolver el valor de la constante
			// QUE HACER CON EL TIPO esta jarcodeado ahora

	   }
	   | ID ABRECOR expresion CIERRACOR {

	   		//este declarada (en la lista)
			//tiene que ser entero, mayor que 0
			//tiene que estar dentro del rango
	   		//devolver el valor de la id

	   }
   	   | MENOS CTE {
   	   		if (((Long)$2.obj) > 32768 ) {
   	   			yyerror("Numero negativo debajo del rango en linea "+lineNumber); 
   	   			err = true;
   	   		} else {
   	   			SymbolTable.getInstance().addSymbol("-"+$2.obj, new AttributeConst("const","entero"));
   	   			constPendientes.add((Long)$2.obj);
   	   		}

   	   		//devolver el valor
   	   		//fijarte si eliminar la constante positiva

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
private boolean err = false;
private List<String> declaradas ;
private List<Long> constPendientes ;

void add(String s) {
	if(!err) {
		detections.add(s);
	}
	err=false;
}


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
	String type = SymbolTable.getInstance().get(s).getTypeOfToken();

	if( type.equals("id") ) {
	    tok = ID;
	    yylval = new ParserVal( s ) ;
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

boolean estaDeclarada (String variable) {
	return declaradas.contains(variable);
}

boolean esEntero(Long constante) {
	return (constante < 32768  );
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
 declaradas = new LinkedList<String>();
 constPendientes = new LinkedList<Long> ();
 yyparse();
 for(Long l : constPendientes) {
 	if( ((AttributeConst)SymbolTable.getInstance().get(l.toString())).getTypeOfElement() == null) {
 		SymbolTable.getInstance().removeSymbol(l.toString());
 	}
 }
}
