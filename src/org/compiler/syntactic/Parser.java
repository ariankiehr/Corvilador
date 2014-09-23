//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package org.compiler.syntactic;



//#line 2 "parser.y"
import org.compiler.lex.LexicalAnalyzer;
import java.util.*;
import org.compiler.symboltable.SymbolTable;
import org.compiler.lex.Token;
//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short ELSE=258;
public final static short PRINT=259;
public final static short INT=260;
public final static short UINT=261;
public final static short DO=262;
public final static short UNTIL=263;
public final static short VECTOR=264;
public final static short OF=265;
public final static short THEN=266;
public final static short CTE=267;
public final static short ID=268;
public final static short CAD=269;
public final static short MAYORIGUAL=270;
public final static short MENORIGUAL=271;
public final static short ASIG=272;
public final static short IGUAL=273;
public final static short ABREPAR=274;
public final static short CIERRAPAR=275;
public final static short ABRELLAV=276;
public final static short CIERRALLAV=277;
public final static short ABRECOR=278;
public final static short CIERRACOR=279;
public final static short MENOS=280;
public final static short MAS=281;
public final static short POR=282;
public final static short DIV=283;
public final static short MENOR=284;
public final static short MAYOR=285;
public final static short PUNTOCOMA=286;
public final static short COMA=287;
public final static short IGUALRARO=288;
public final static short DOSPUNTO=289;
public final static short LOWER_THAN_ELSE=290;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    3,    3,    5,
    5,    4,    4,    6,    6,    2,    2,    7,    7,    7,
    7,    9,   10,    8,    8,   15,   15,   16,   18,   17,
   14,   13,   12,   12,   12,   20,   20,   20,   11,   11,
   21,   21,   21,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    2,    3,    2,    9,    1,    1,
    1,    1,    3,    1,    3,    1,    2,    2,    1,    2,
    2,    3,    4,    3,    1,    2,    1,    2,    1,    1,
    4,    3,    3,    3,    1,    3,    3,    1,    1,    4,
    1,    1,    4,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   16,   19,    0,    0,    0,    0,    0,   18,   12,    0,
   25,    0,    0,    0,   14,    0,    0,    0,   17,    5,
   20,   21,    0,    0,   42,    0,    0,    0,    0,   38,
    0,    0,    0,    0,    0,    0,    6,    0,    0,   24,
    0,   27,    0,   45,   46,   44,    0,    0,   47,   48,
    0,   31,    0,    0,   13,   15,   23,    0,   40,   28,
   30,   26,    0,    0,    0,    0,   36,   37,    0,   43,
    0,    0,    0,   10,   11,    8,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   20,   86,   24,   11,   12,   13,   14,
   15,   37,   38,   16,   50,   51,   72,   52,   61,   39,
   40,
};
final static short yysindex[] = {                      -125,
    0, -271, -280, -260, -231, -267,    0, -125, -135, -270,
    0,    0, -268, -266, -245, -217, -232,    0,    0, -248,
    0, -212, -135, -195,    0, -227, -135, -235,    0,    0,
    0,    0, -232, -231,    0, -193, -155, -192, -239,    0,
 -179, -232, -247, -232, -187, -226,    0, -204, -158,    0,
 -231,    0, -232,    0,    0,    0, -232, -232,    0,    0,
 -232,    0, -232, -232,    0,    0,    0, -154,    0,    0,
    0,    0, -209, -239, -239, -204,    0,    0, -169,    0,
 -153, -148, -173,    0,    0,    0,
};
final static short yyrindex[] = {                       119,
    1,    0,    0,    0,    0, -152,    0,  123,  128,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -150,
    0, -152,    0,    0,    0,    0,  138,    0,    0,    0,
    0,    0,    0,    0,    0, -223,    0,    0, -206,    0,
    0,    0,    0,    0,    0,    0,    0, -147,    5,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -189, -172, -262,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    9,  132,    0,    0,  -32,   -5,    0,    0,    0,
    0,  -19,   97,    0,    0,    0,    0,    0,    0,   36,
   41,
};
final static int YYTABLESIZE=287;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         25,
   25,   49,   17,   29,   29,   18,   46,   19,   21,    2,
   26,    3,   32,   48,    5,   30,   27,   31,   71,   32,
   22,   29,   46,   32,   21,    2,   33,    3,   25,   66,
    5,   43,   45,   73,   35,   36,   22,   29,   41,   35,
   36,   76,   63,   64,   23,   25,   41,   41,   34,   41,
   47,   41,   69,   57,   58,   41,   41,   41,   41,   41,
   41,   41,   41,   35,   35,   42,   35,   44,   35,   80,
   57,   58,   35,   35,   35,   57,   58,   35,   35,   35,
   34,   34,   62,   34,   53,   34,   84,   85,   65,   34,
   34,   34,   74,   75,   34,   34,   34,   33,   33,   70,
   33,   68,   33,   77,   78,   79,   33,   33,   33,   81,
   82,   33,   33,   33,   54,   55,   83,   56,    1,   39,
   21,    2,    2,    3,   57,   58,    5,    3,   59,   60,
    1,    2,   22,    3,    4,    7,    5,    4,   22,   28,
   67,    0,    6,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,   25,    0,   25,
   29,   29,   25,   29,    0,    0,   29,   29,   25,    0,
    0,    0,   29,    0,    0,    0,    0,    0,    0,    0,
    0,   29,    0,    0,    0,    0,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,   34,  274,    9,    0,  286,   26,  268,  256,  257,
  278,  259,  275,   33,  262,  286,    8,  286,   51,  286,
  268,   27,   42,  286,  256,  257,  272,  259,   34,  277,
  262,   23,  260,   53,  267,  268,  268,   43,  287,  267,
  268,   61,  282,  283,  276,   51,  270,  271,  266,  273,
  286,  275,  279,  280,  281,  279,  280,  281,  282,  283,
  284,  285,  286,  270,  271,  278,  273,  263,  275,  279,
  280,  281,  279,  280,  281,  280,  281,  284,  285,  286,
  270,  271,  275,  273,  278,  275,  260,  261,  268,  279,
  280,  281,   57,   58,  284,  285,  286,  270,  271,  258,
  273,  289,  275,   63,   64,  260,  279,  280,  281,  279,
  264,  284,  285,  286,  270,  271,  265,  273,    0,  272,
  256,  257,    0,  259,  280,  281,  262,    0,  284,  285,
  256,  257,  268,  259,  260,  286,  262,    0,  286,    8,
   44,   -1,  268,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
  256,  257,  262,  259,   -1,   -1,  262,  263,  268,   -1,
   -1,   -1,  268,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  277,   -1,   -1,   -1,   -1,  286,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=290;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"IF","ELSE","PRINT","INT","UINT","DO","UNTIL","VECTOR","OF",
"THEN","CTE","ID","CAD","MAYORIGUAL","MENORIGUAL","ASIG","IGUAL","ABREPAR",
"CIERRAPAR","ABRELLAV","CIERRALLAV","ABRECOR","CIERRACOR","MENOS","MAS","POR",
"DIV","MENOR","MAYOR","PUNTOCOMA","COMA","IGUALRARO","DOSPUNTO",
"LOWER_THAN_ELSE",
};
final static String yyrule[] = {
"$accept : programa",
"programa :",
"programa : sentencias_declarativas",
"programa : sentencias_ejecutables",
"programa : sentencias_declarativas sentencias_ejecutables",
"sentencias_declarativas : sentencias_declarativas_simples PUNTOCOMA",
"sentencias_declarativas : sentencias_declarativas sentencias_declarativas_simples PUNTOCOMA",
"sentencias_declarativas_simples : INT variables",
"sentencias_declarativas_simples : ID ABRECOR INT DOSPUNTO INT CIERRACOR VECTOR OF tipo",
"sentencias_declarativas_simples : error",
"tipo : INT",
"tipo : UINT",
"variables : ID",
"variables : variables COMA ID",
"bloque_sentencias : sentencia",
"bloque_sentencias : ABRELLAV sentencias_ejecutables CIERRALLAV",
"sentencias_ejecutables : sentencia",
"sentencias_ejecutables : sentencias_ejecutables sentencia",
"sentencia : PRINT PUNTOCOMA",
"sentencia : seleccion",
"sentencia : asignacion PUNTOCOMA",
"sentencia : iteracion PUNTOCOMA",
"asignacion : variable ASIG expresion",
"iteracion : DO bloque_sentencias UNTIL condicion",
"seleccion : cabecera_seleccion THEN cuerpo_seleccion",
"seleccion : error",
"cuerpo_seleccion : bloque_then bloque_else",
"cuerpo_seleccion : bloque_final",
"bloque_then : bloque_sentencias ELSE",
"bloque_final : bloque_sentencias",
"bloque_else : bloque_sentencias",
"cabecera_seleccion : IF ABREPAR condicion CIERRAPAR",
"condicion : expresion comparador expresion",
"expresion : expresion MAS termino",
"expresion : expresion MENOS termino",
"expresion : termino",
"termino : termino POR factor",
"termino : termino DIV factor",
"termino : factor",
"variable : ID",
"variable : ID ABRECOR expresion CIERRACOR",
"factor : ID",
"factor : CTE",
"factor : ID ABRECOR expresion CIERRACOR",
"comparador : IGUAL",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : MENOR",
"comparador : MAYOR",
};

//#line 113 "parser.y"

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private int previousTokenLineNumber = 0;
boolean newline;


void yyerror(String s) {
	errors.add(s);
}

int yylex() {
	String s;
	int tok;
	
	if (!la.hasMoreTokens()) {
		 if (!newline) {
		 	newline=true;
		 	return '\n'; //So we look like classic YACC example
		 }
		 else {
		 	return 0;
		 }
	}
	
	Token t = la.nextToken();
	s = t.getLexem();
	
	lineNumber = t.getLine();
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
 newline=false;
 yyparse();
}
//#line 440 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 7:
//#line 31 "parser.y"
{ detections.add("Declaracion de variable comun en linea "+previousTokenLineNumber); }
break;
case 8:
//#line 32 "parser.y"
{ detections.add("Declaracion de variable vector en linea "+previousTokenLineNumber); }
break;
case 9:
//#line 33 "parser.y"
{ yyerror("Declaracion de variables mal hecha en " + previousTokenLineNumber); }
break;
case 18:
//#line 52 "parser.y"
{ detections.add("Declaracion imprimir en "+previousTokenLineNumber); }
break;
case 25:
//#line 66 "parser.y"
{ yyerror("if mal hecho en " + previousTokenLineNumber); }
break;
case 29:
//#line 76 "parser.y"
{ detections.add("Declaracion if en "+previousTokenLineNumber); }
break;
case 30:
//#line 78 "parser.y"
{ detections.add("Declaracion if else en "+previousTokenLineNumber); }
break;
case 42:
//#line 101 "parser.y"
{ System.out.println( val_peek(0).ival ); }
break;
//#line 621 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
