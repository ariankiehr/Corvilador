%{
import org.compiler.lex.LexicalAnalyzer;
import java.util.*;
import org.compiler.symboltable.*;
import org.compiler.lex.Token;
import org.compiler.arbolito.*;
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
	| sentencias_ejecutables {
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			tree = (Arbol)$1.obj;
		}
	}
	| sentencias_declarativas sentencias_ejecutables {
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			tree = (Arbol)$2.obj;
		}
	}
;

sentencias_declarativas : 	sentencias_declarativas_simples
						| 	sentencias_declarativas sentencias_declarativas_simples
;

sentencias_declarativas_simples : 
					tipo variables PUNTOCOMA {
							List<String> vars = (List<String>)$2.obj;
							for( String s : vars ) {
								SymbolTable.getInstance().addSymbol( s , new AttributeVariableID( 
									SymbolTable.getInstance().get(s).getTypeOfToken(),  $1.sval, "variable" ) );
								if (!estaDeclarada(s)) {
									declaradas.add(s);
								}
								else {
									
										yyerror("Error: La variable ya esta declarada en la linea: " + lineNumber);
								}
							}
		 
							add("Declaracion de variable comun en la linea " + lineNumber);
						}


					| tipo variables error {yyerror("Error: Falta ';' en la declaracion de variables en la linea " + lineNumber);}

					| 	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA {
								

								if ( !estaDeclarada($1.sval) ) {
									declaradas.add($1.sval);
								}
								else {
									yyerror("Error: La variable '" + $1.sval + "' ya esta declarada en la linea: " + lineNumber);
								}
		
		
								if ( !esEntero((Long)$3.obj) || !esEntero((Long)$5.obj)) {
									yyerror("Error: El/Los limites del vector no es/son entero/s en la linea: " + lineNumber);
								}
								else if ( (Long)$3.obj < 0 ) {
									yyerror("Error: La constante es menor que 0 en la linea: " + lineNumber);
								}
								else if ( (Long)$3.obj > (Long)$5.obj ){
									yyerror("Error: El limite inferior es mayor al limite superior en la linea: " + lineNumber);
								}
								else {
									SymbolTable.getInstance().addSymbol( String.valueOf((Long)$3.obj), new AttributeConTipo( 
										SymbolTable.getInstance().get(String.valueOf((Long)$3.obj)).getTypeOfToken(), "entero") );
									SymbolTable.getInstance().addSymbol( String.valueOf((Long)$5.obj), new AttributeConTipo( 
										SymbolTable.getInstance().get(String.valueOf((Long)$5.obj)).getTypeOfToken(), "entero") );
									String typeOfToken = SymbolTable.getInstance().get($1.sval).getTypeOfToken();
									SymbolTable.getInstance().removeSymbol($1.sval);
									SymbolTable.getInstance().addSymbol( $1.sval, new AttributeVector( 
										typeOfToken, $9.sval, "vector", (Long)$3.obj, (Long)$5.obj ) ); 
								}
										
						add("Declaracion de variable vector en linea " + lineNumber); }
					| 	ID ABRECOR CTE  CTE CIERRACOR VECTOR OF tipo PUNTOCOMA  {yyerror("Error: Se esperaba '..' en la linea " + lineNumber);}
					|	ID ABRECOR CTE DOSPUNTO CTE  VECTOR OF tipo PUNTOCOMA  {yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
					|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR OF tipo PUNTOCOMA  {yyerror("Error: Falta la palabra reservada 'VECTOR' en la linea " + lineNumber);}	
					|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR  tipo PUNTOCOMA  {yyerror("Error: Falta la palabra reservada 'DE' en la linea " + lineNumber);}		
					|   ID ABRECOR DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA {yyerror("Error: Se esperaba una constante en la linea " + lineNumber);}
					|	ID ABRECOR CTE DOSPUNTO CIERRACOR VECTOR OF tipo PUNTOCOMA {yyerror("Error: Se esperaba una constante en la linea " + lineNumber);}
			
					|	ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF  PUNTOCOMA  {yyerror("Error: Falta el tipo del vector en la linea " + lineNumber);}

					|	ID ABRECOR  CIERRACOR VECTOR OF tipo PUNTOCOMA  {yyerror("Error: Falta el tipo del vector en la linea " + lineNumber);}

					| ID ID  PUNTOCOMA {yyerror("Error: Palabra reservada mal escrita en la linea " + lineNumber);}
					| tipo tipo PUNTOCOMA {yyerror("Error: Nombre de variable igual al tipo en la linea " + lineNumber);}
					| ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo {yyerror("Error: Se detecto declaracion de vector erronea, falta ';' en la linea " + lineNumber);  }
;

tipo : INT { 
			$$ = new ParserVal("entero");  
	}
	|	UINT { 
			$$ = new ParserVal("entero_ss"); 
	}
;

variables : ID { 
				List<String> vars = new LinkedList<String>(); 
				vars.add( $1.sval );
				$$ = new ParserVal(vars); 


		}
		|	variables COMA ID { List<String> vars = new LinkedList<String>(); 
								vars.add( $3.sval );
								vars.addAll( (LinkedList<String>)$1.obj );
								$$ = new ParserVal(vars);
		}		


;

bloque_sentencias : sentencia PUNTOCOMA { 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							$$ = $1; 
						} else {
							$$ = new ParserVal( new Hoja( "error", "syntax error" ));
						}		
					}
					
					|	ABRELLAV sentencias_ejecutables CIERRALLAV { 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								$$ = $2; 
						} else {
							$$ = new ParserVal( new Hoja( "error", "syntax error" ));
						}
					}

					
					|	sentencia error  {yyerror("Error: falta ';' en una sentencia dentro del IF, en la linea " + lineNumber);}

					|	error sentencias_ejecutables CIERRALLAV {yyerror("Error: Se esperaba '{' en la linea " + lineNumber);}
					
					|	ABRELLAV sentencias_ejecutables error {yyerror("Error: Se esperaba '}' en la linea " + lineNumber);}
				





;

sentencias_ejecutables : sentencia  PUNTOCOMA{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								$$ = $1; 
							} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
						| sentencias_ejecutables sentencia  PUNTOCOMA{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								$$ = new ParserVal(new NodoSinTipo("sentencia",(Arbol)($1.obj),(Arbol)($2.obj))); 
							} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
					| seleccion { 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								$$ = $1; 
							} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
					| sentencias_ejecutables seleccion 	{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								$$ = new ParserVal(new NodoSinTipo("sentencia",(Arbol)($1.obj),(Arbol)($2.obj))); 
							} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}

					|	sentencias_ejecutables variable error   {yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}

					|	sentencia error {yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}

					| 	error  {yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}

;

sentencia : PRINT ABREPAR CAD CIERRAPAR { 
				add("Declaracion imprimir en la linea  "+lineNumber+" cadena "+ $3.sval); 
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						$$ = new ParserVal(new Hoja("imprimir", $3.sval));
				} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
	
		| 	asignacion { 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1; 
			} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}

		|	iteracion  { 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1;
			}  else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}

		|	PRINT error CAD CIERRAPAR {yyerror("Error: Se detecto un PRINT erroneo, se esperaba un '(' en la linea " + lineNumber);}
		|	PRINT ABREPAR CAD error {yyerror("Error: Se detecto un PRINT erroneo, se esperaba un ')' en la linea " + lineNumber);}
		|	PRINT ABREPAR error CIERRAPAR {yyerror("Error: Se detecto un PRINT erroneo, se esperaba una 'cadena' en la linea " + lineNumber);}
;

asignacion : variable ASIG expresion {
						if(  !"error".equals(((Arbol)$1.obj).getElem()) &&  !"error".equals(((Arbol)$3.obj).getElem()) ){
								if ( !estaDeclarada(   ((Arbol)$1.obj).getElem()) ) {
									yyerror("Error: La variable '" + ((Arbol)$1.obj).getElem() + "' no ha sido declarada en la linea " + lineNumber);
								}
							

								if( ! (((Arbol)$1.obj).getTipo().equals( ((Arbol)$3.obj).getTipo() )) ){
									yyerror ("Error: A la variable '" + ((Arbol)$1.obj).getElem() + "' se le esta asignando algo de otro tipo en la linea " + lineNumber);
									
								} else {
									add("Asignacion en linea  "+lineNumber); 
								}
						
								if(((Arbol)$1.obj).getElem().contains("-") ){
									yyerror("Error: La variable '" + ((Arbol)$1.obj).getElem() + "' no puede estar del lado izquierdo, en la linea " + lineNumber);
								}
								
								if( ((Arbol)$3.obj).getTipo().equals("entero_ss") && ((Arbol)$3.obj).getElem().charAt(0) == '-' && ((Arbol)$3.obj).getElem().length()>1 ) {
									yyerror("Error: mal uso de la negacion en la linea " + lineNumber);
								}
						
							}
							
							
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								$$ = new ParserVal( new Nodo(":=", (Arbol)$3.obj , (Arbol)$1.obj , ((Arbol)$3.obj).getTipo() ) );
						} else {
						$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
			| ASIG expresion {yyerror("Error: Se detecto una asignacion erronea, falta parte izquierda, en la linea " + lineNumber);}		
			
			| variable ASIG  {yyerror("Error: Se detecto una asignacion erronea, falta parte derecha, en la linea " + lineNumber);}			
	
;

iteracion : DO bloque_sentencias UNTIL ABREPAR condicion CIERRAPAR { 
					add("Iterar en linea  "+lineNumber);
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						$$ = new ParserVal(new NodoSinTipo("iterar", new NodoUnario("condicion",(Arbol)($5.obj)), (Arbol)($2.obj)));
					} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
			|	DO  UNTIL ABREPAR condicion CIERRAPAR {yyerror("Error: Se espera un bloque de sentencias en la linea "+ lineNumber);} 
			|	DO bloque_sentencias ABREPAR condicion CIERRAPAR {yyerror("Error: Se esperaba la palabra reservada 'HASTA' en la linea "+ lineNumber);}
			|	DO bloque_sentencias UNTIL condicion CIERRAPAR {yyerror("Error: Se espera un 'Parentesis abierto' en la linea "+ lineNumber);} 
			|	DO bloque_sentencias UNTIL ABREPAR condicion  {yyerror("Error: Se espera un 'Parentesis cerrado' en la linea "+ lineNumber);} 

			|	DO bloque_sentencias error ABREPAR condicion CIERRAPAR {yyerror("Error: Se esperaba la palabra reservada 'Hasta' en la linea "+ lineNumber); }
			|	DO bloque_sentencias UNTIL ABREPAR error CIERRAPAR {yyerror("Error: Falta la condicion el la iteracion en la linea "+ lineNumber); }

;

seleccion : cabecera_seleccion THEN cuerpo_seleccion {
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			$$ = new ParserVal( new NodoSinTipo("si", (Arbol)$1.obj , (Arbol)$3.obj ) );
		} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
	}
	| cabecera_seleccion cuerpo_seleccion error  {yyerror("Error: Se detecto IF erroneo, falta la palabra reservada 'ENTONCES' en la linea "+ lineNumber); }



; 
	

cuerpo_seleccion : 	bloque_then bloque_else { 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							$$ = new ParserVal( new NodoSinTipo("cuerpo", (Arbol)$1.obj , (Arbol)$2.obj ) ); 
						} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
				| bloque_final {
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							$$ = new ParserVal(new NodoUnario("cuerpo",(Arbol)($1.obj))); 
						} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
					}
				}


;

bloque_then : bloque_sentencias ELSE { 
	if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
		$$ = new ParserVal(new NodoUnario("entonces",(Arbol)($1.obj))); 
	} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
}			 
;

bloque_final : bloque_sentencias %prec LOWER_THAN_ELSE {
						add("Declaracion if en linea " + lineNumber); 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							$$ = new ParserVal(new NodoUnario("entonces",(Arbol)($1.obj))); 
						} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}

			}		
;

bloque_else : bloque_sentencias { 
					add("Declaracion if else en linea " + lineNumber); 
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						$$ = new ParserVal(new NodoUnario("sino",(Arbol)($1.obj)));
					} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
;

cabecera_seleccion : 	IF ABREPAR condicion CIERRAPAR{
									if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
										$$ = new ParserVal(new NodoUnario("condicion",(Arbol)($3.obj)));
									} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
								}

					|	IF error condicion CIERRAPAR {yyerror("Error: Se detecto IF erroneo despues del token if en la linea "+ lineNumber);}
					|	IF condicion CIERRAPAR {yyerror("Error: Se detecto IF erroneo, se esperaba '(' en la linea "+ lineNumber);}
					|	IF ABREPAR condicion THEN {yyerror("Error: Se detecto IF erroneo, se esperaba ')' en la linea "+ lineNumber);}
					|	IF ABREPAR error CIERRAPAR {yyerror("Error: Se detecto IF erroneo, falta la condicion en la linea "+ lineNumber);}

;

condicion : expresion comparador expresion  {
				
				if(  !"error".equals(((Arbol)$1.obj).getElem()) &&  !"error".equals(((Arbol)$3.obj).getElem()) ){
						if(! ((Arbol)$1.obj).getTipo().equals( ((Arbol)$3.obj).getTipo() ) ) {
				
							yyerror("Error: Difieren los tipos en la linea " + lineNumber);
						}
				}

				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					$$ = new ParserVal( new Nodo( $2.sval, (Arbol)$1.obj , (Arbol)$3.obj , ((Arbol)$3.obj).getTipo() ) );
				} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}

			| comparador expresion  {yyerror("Error: Se detecto una comparacion erronea, falta parte izquierda, en la linea " + lineNumber);}
			| expresion comparador   {yyerror("Error: Se detecto una comparacion erronea, falta parte derecha, en la linea " + lineNumber);}
			| comparador   {yyerror("Error: Se detecto una comparacion erronea, no hay nada para comparar, en la linea " + lineNumber);}

;

expresion : expresion MAS termino {
				
				if(  !"error".equals(((Arbol)$1.obj).getElem()) &&  !"error".equals(((Arbol)$3.obj).getElem()) ){
					if( !((Arbol)$1.obj).getTipo().equals( ((Arbol)$3.obj).getTipo() ) ) {
						yyerror("Error: Diefieren los tipos de la suma en la linea " + lineNumber);
					} 
				}
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					$$ = new ParserVal( new Nodo( "+", (Arbol)$1.obj , (Arbol)$3.obj , ((Arbol)$3.obj).getTipo() ) );
				} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
		  | expresion MENOS termino	{
				if(  !"error".equals(((Arbol)$1.obj).getElem()) &&  !"error".equals(((Arbol)$3.obj).getElem()) ){
					if( !((Arbol)$1.obj).getTipo().equals( ((Arbol)$3.obj).getTipo() ) ) {
						yyerror("Error: Diefieren los tipos de la resta en la linea " + lineNumber);
					} 
				}
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					$$ = new ParserVal( new Nodo( "-", (Arbol)$1.obj , (Arbol)$3.obj , ((Arbol)$3.obj).getTipo() ) );
				} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				
			}
		  | termino {
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1;
			} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		  }

;
 
termino : termino POR factor {
				if( ((Arbol)$1.obj).getTipo().equals( ((Arbol)$3.obj).getTipo() ) ) {
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						$$ = new ParserVal( new Nodo( "*", (Arbol)$1.obj , (Arbol)$3.obj , ((Arbol)$3.obj).getTipo() ) );
					} else {
						$$ = new ParserVal( new Hoja( "error", "syntax error" ));
					}
				} else {
					if(  !"error".equals(((Arbol)$1.obj).getElem()) &&  !"error".equals(((Arbol)$3.obj).getElem()) ){
						yyerror("Error: Diefieren los tipos de la multiplicacion en la linea " + lineNumber);
					}
				}
				
			}
		| termino DIV factor {
			if( ((Arbol)$1.obj).getTipo().equals( ((Arbol)$3.obj).getTipo() ) ) {
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					$$ = new ParserVal( new Nodo( "/", (Arbol)$1.obj , (Arbol)$3.obj , ((Arbol)$3.obj).getTipo() ) );
				} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			} else {
					if(  !"error".equals(((Arbol)$1.obj).getElem()) &&  !"error".equals(((Arbol)$3.obj).getElem()) ){
						yyerror("Error: Diefieren los tipos de la division en la linea " + lineNumber);
				}			}
		}
		| factor {
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1;
			} else {
				$$ = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}

;
		
variable :  id {
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1; 
			} else {
				$$ = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
	| usovector {
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			$$ = $1;
		} else {
			$$ = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}


;

id :  ID {

	
			if( !estaDeclarada($1.sval) ) {
				yyerror("Error: La variable '" + $1.sval + "' no esta declarada en la linea " + lineNumber);
			}

			if (!("variable".equals(((AttributeVariableID)SymbolTable.getInstance().get($1.sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo variable simple en la linea " + lineNumber);
			}

			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = new ParserVal(new Hoja(  $1.sval, ((AttributeVariableID)SymbolTable.getInstance().get($1.sval)).getTypeOfElement() )); 
			} else {
				$$ = new ParserVal( new Hoja( "error", "syntax error" ));
			}
			
		}
		
		| MENOS ID {

			if( !estaDeclarada($2.sval) ) {
				yyerror("Error: La variable '" + $2.sval + "' no esta declarada en la linea " + lineNumber);
			}

			if (!("variable".equals(((AttributeVariableID)SymbolTable.getInstance().get($2.sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo variable simple en la linea " + lineNumber);
			}

			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = new ParserVal(new Hoja("-"+$2.sval, ((AttributeVariableID)SymbolTable.getInstance().get($2.sval)).getTypeOfElement() )); 
			} else {
				$$ = new ParserVal( new Hoja( "error", "syntax error" ));
			}
			
		}
;

usovector : ID ABRECOR expresion CIERRACOR {
		if( !"error".equals(((Arbol)$3.obj).getElem()) ){
			
			if (!((Arbol)$3.obj).getTipo().equals("entero")) {
				yyerror("Error: El tipo del indice del vector es incorrecto en la linea "+ lineNumber);
			}
	
			if( !estaDeclarada($1.sval) ) {
					yyerror("Error: La variable no esta declarada en la linea " + lineNumber);
			}
			if (!("vector".equals(((AttributeVariableID)SymbolTable.getInstance().get($1.sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo vector e la linea " + lineNumber);
			}
		}
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				Arbol idv = new Hoja($1.sval, ((AttributeVector)SymbolTable.getInstance().get($1.sval)).getTypeOfElement() );
				$$ = new ParserVal(new Nodo($1.sval , idv, (Arbol)$3.obj, idv.getTipo()  ));
		} else {
			$$ = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}
 | MENOS ID ABRECOR expresion CIERRACOR {
		if( !"error".equals(((Arbol)$4.obj).getElem()) ){
			
			if (!((Arbol)$4.obj).getTipo().equals("entero")) {
				yyerror("Error: El tipo del indice del vector es incorrecto en la linea "+ lineNumber);
			}
	
			if( !estaDeclarada($2.sval) ) {
					yyerror("Error: La variable no esta declarada en la linea " + lineNumber);
			}
			if (!("vector".equals(((AttributeVariableID)SymbolTable.getInstance().get($2.sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo vector e la linea " + lineNumber);
			}
		}
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				Arbol idv = new Hoja($2.sval, ((AttributeVector)SymbolTable.getInstance().get($2.sval)).getTypeOfElement() );
				$$ = new ParserVal(new Nodo("-"+$2.sval , idv, (Arbol)$4.obj, idv.getTipo()  ));
		} else {
			$$ = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}


;
		
factor : id {
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1; 
			} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
		| CTE {
			positivosPendientes.add( (Long)$1.obj );
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = new ParserVal(new Hoja( $1.obj.toString(), 
				((AttributeConTipo)SymbolTable.getInstance().get(String.valueOf((Long)$1.obj))).getTypeOfElement() )); 
			} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
		| usovector {
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = $1;
			} else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
		| MENOS CTE {
	
			if (((Long)$2.obj) > 32768 ) {
				yyerror("Error: Numero negativo debajo del rango en la linea " + lineNumber); 
				err = true;
			} else {
				SymbolTable.getInstance().addSymbol("-"+$2.obj, new AttributeConTipo("const","entero"));
				negativosPendientes.add((Long)$2.obj);
			}
			
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				$$ = new ParserVal(new Hoja( "-" + $2.obj.toString(), 
				((AttributeConTipo)SymbolTable.getInstance().get("-" + String.valueOf((Long)$2.obj))).getTypeOfElement() ));
			}  else {
					$$ = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
;
	   
comparador : IGUAL { $$ = new ParserVal("=");}
			| MAYORIGUAL { $$ = new ParserVal(">=");}
			| MENORIGUAL { $$ = new ParserVal("<=");}
			| MENOR { $$ = new ParserVal("<");}
			| MAYOR { $$ = new ParserVal(">");}
			| DISTINTO { $$ = new ParserVal("^=");}
;


%%

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
public static Arbol tree;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private String s;
private boolean err = false;
private List<String> declaradas ;
private List<Long> negativosPendientes, positivosPendientes ;


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
 negativosPendientes = new LinkedList<Long> ();
 positivosPendientes = new LinkedList<Long> ();
 tree = null;
 yyparse();

 negativosPendientes.removeAll(positivosPendientes);

 for(Long l : negativosPendientes) {


	SymbolTable.getInstance().removeSymbol(l.toString());
 }
}