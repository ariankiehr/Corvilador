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
public final static short DISTINTO=288;
public final static short DOSPUNTO=289;
public final static short LOWER_THAN_ELSE=290;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    4,    4,    5,    5,
    6,    6,    2,    2,    2,    2,    2,    2,    7,    7,
    7,    9,   10,   10,   10,   10,   10,    8,    8,   15,
   15,   16,   18,   17,   14,   14,   14,   13,   12,   12,
   12,   20,   20,   20,   11,   11,   11,   11,   11,   21,
   21,   21,   21,   19,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    2,    3,    2,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    1,    1,    1,    3,
    2,    3,    2,    3,    1,    2,    3,    2,    4,    1,
    1,    3,    6,    5,    5,    5,    5,    3,    2,    2,
    1,    2,    1,    1,    4,    4,    3,    3,    3,    3,
    1,    3,    3,    1,    1,    4,    3,    3,    2,    1,
    1,    4,    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   17,   18,    0,    0,    0,    0,    0,
    0,    0,    0,   25,   30,   31,    0,    0,    0,   28,
    0,   61,    0,    0,    0,    0,    0,    0,   54,    0,
    0,    0,    0,    0,    0,    0,   59,    0,    0,    0,
    0,   26,    5,   19,    0,   23,    0,    0,    0,   39,
    0,   41,    0,    0,    0,    0,   63,   65,   66,   64,
    0,    0,   67,   68,   69,    0,   47,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   21,    0,    0,   57,
    0,    6,   27,   24,    0,    0,   38,   42,   44,   40,
    0,   46,    0,   45,    0,    0,    0,   52,   53,   29,
    0,   22,    0,    0,    0,    0,    0,    0,   56,   20,
    0,   62,   34,    0,   36,   35,    0,    0,    0,    0,
    0,   33,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   16,    9,   10,   11,   12,   13,   14,   15,
    8,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   45,   49,   35,   14,   15,   16,
   17,   26,   27,   18,   50,   51,   90,   52,   66,   28,
   29,
};
final static short yysindex[] = {                       -67,
 -270, -247, -264,    0,    0, -203, -193,    0,  -67,  -60,
 -262, -246, -260,    0,    0,    0, -223,  -92, -197,    0,
 -239,    0, -214, -239, -153, -116, -174, -141,    0, -159,
 -131, -150,  -53, -240, -147, -249,    0,  -60, -130, -128,
 -115,    0,    0,    0, -121,    0, -239, -215, -125,    0,
 -215,    0, -114, -102, -239,  -98,    0,    0,    0,    0,
 -239, -239,    0,    0,    0, -239,    0, -239, -239,  -87,
 -239, -205,  -88, -211,  -89, -239,    0,  -84, -251,    0,
 -176,    0,    0,    0,  -68, -133,    0,    0,    0,    0,
  -57,    0, -157,    0, -141, -141, -133,    0,    0,    0,
  -64,    0, -239,  -63,  -62,  -43,  -42, -172,    0,    0,
  -72,    0,    0,  -61,    0,    0,  -52,  -51,  -50, -242,
  -38,    0,  -34,  -33,  -32,  -31, -187,  -29,  -28,  -27,
  -26,  -25,  -24, -213,  -80,  -80,  -80,  -80,  -80,  -80,
  -80, -189,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                       234,
    0,    0,    0,    0,    0,    0,  -37,    0,  242,  243,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -192,    0,    0,    0,    0, -173,    0,    0,
    0,  -37,    0,    0,    0,    0,    0,  244,    0,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,    1,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -120,    0,
  -23,    0,    0,    0,    0,  -40,    0,    0,    0,    0,
    0,    0,    0,    0, -154, -135, -166,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -39,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    6,  239,   81,    0,    7,    2,   -6,    0,    0,
    0,  -30,  -21,    0,  202,    0,    0,    0,    0,  121,
  118,
};
final static int YYTABLESIZE=278;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   43,   13,   56,   42,  107,   81,   78,   19,   21,   30,
   13,   41,   34,  126,   38,   20,   86,   79,   23,   22,
   23,   44,   75,   43,   93,   46,   24,   22,   23,   80,
   25,   42,   25,   76,   13,   97,  127,  108,   74,   41,
   25,   81,  141,    3,   40,    2,    6,    3,   47,  101,
    6,  142,   32,  104,  105,    3,   32,   89,    6,   31,
   33,   22,   23,   55,   32,  102,  150,   42,  133,   53,
    4,    5,   33,   80,   25,   41,  134,   60,   60,   60,
   60,  114,   60,  119,   36,   37,   60,   60,   60,   60,
   60,   60,   60,   60,  120,   60,   51,   51,   51,   51,
   67,   51,  109,   61,   62,   51,   51,   51,   48,   70,
   51,   51,   51,   57,   51,   50,   50,   50,   50,   48,
   50,  112,   61,   62,   50,   50,   50,   72,   37,   50,
   50,   50,   88,   50,   49,   49,   49,   49,   77,   49,
   68,   69,   71,   49,   49,   49,   61,   62,   49,   49,
   49,   61,   49,   58,   59,   82,   60,   83,   61,   61,
   61,   61,   61,   61,   62,   85,    3,   63,   64,    6,
   84,   65,   92,   48,   91,   32,   94,   22,   23,    4,
    5,   95,   96,   33,  103,   98,   99,  100,    1,    2,
   25,    3,    4,    5,    6,   40,    2,   20,    3,  110,
    7,    6,   73,    2,  106,    3,  121,   32,    6,  111,
  113,  115,  116,  122,   32,  143,  144,  145,  146,  147,
  148,  149,  151,  117,  118,  128,  123,  124,  125,  129,
  130,  131,  132,    1,   55,  135,  136,  137,  138,  139,
  140,    2,    3,    4,    7,   32,   37,   39,   58,   87,
    0,    0,    0,    0,    0,    0,   43,   43,    0,   43,
    0,    0,   43,    0,    0,    0,    0,    0,   43,    0,
    0,    0,    0,    0,    0,    0,    0,   43,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         21,
    0,    0,   24,   10,  256,   36,  256,  278,  256,  274,
    9,   10,    6,  256,    9,  286,   47,  267,  268,  267,
  268,  268,  263,  286,   55,  286,  274,  267,  268,  279,
  280,   38,  280,  274,   33,   66,  279,  289,   33,   38,
  280,   72,  256,  259,  256,  257,  262,  259,  272,   71,
  262,  265,  268,   75,   76,  259,  268,   51,  262,  263,
  276,  267,  268,  278,  268,  277,  256,   74,  256,  267,
  260,  261,  276,  279,  280,   74,  264,  270,  271,  272,
  273,  103,  275,  256,  278,  279,  279,  280,  281,  282,
  283,  284,  285,  286,  267,  288,  270,  271,  272,  273,
  275,  275,  279,  280,  281,  279,  280,  281,  275,  269,
  284,  285,  286,  267,  288,  270,  271,  272,  273,  286,
  275,  279,  280,  281,  279,  280,  281,  278,  279,  284,
  285,  286,  258,  288,  270,  271,  272,  273,  286,  275,
  282,  283,  274,  279,  280,  281,  280,  281,  284,  285,
  286,  272,  288,  270,  271,  286,  273,  286,  279,  280,
  281,  282,  283,  280,  281,  287,  259,  284,  285,  262,
  286,  288,  275,  266,  289,  268,  275,  267,  268,  260,
  261,   61,   62,  276,  274,   68,   69,  275,  256,  257,
  280,  259,  260,  261,  262,  256,  257,  286,  259,  268,
  268,  262,  256,  257,  289,  259,  279,  268,  262,  267,
  275,  275,  275,  275,  268,  135,  136,  137,  138,  139,
  140,  141,  142,  267,  267,  264,  279,  279,  279,  264,
  264,  264,  264,    0,  272,  265,  265,  265,  265,  265,
  265,    0,    0,    0,  286,  286,  286,    9,  272,   48,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,  259,
   -1,   -1,  262,   -1,   -1,   -1,   -1,   -1,  268,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  277,
};
}
final static short YYFINAL=8;
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
"DIV","MENOR","MAYOR","PUNTOCOMA","COMA","DISTINTO","DOSPUNTO",
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
"sentencias_declarativas_simples : tipo variables",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo",
"sentencias_declarativas_simples : ID ABRECOR error DOSPUNTO CTE CIERRACOR VECTOR OF tipo",
"sentencias_declarativas_simples : ID ABRECOR CTE error CTE CIERRACOR VECTOR OF tipo",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO error CIERRACOR VECTOR OF tipo",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE error VECTOR OF tipo",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR error OF tipo",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR error tipo",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF error",
"sentencias_declarativas_simples : error ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo",
"tipo : INT",
"tipo : UINT",
"variables : ID",
"variables : variables COMA ID",
"bloque_sentencias : sentencia PUNTOCOMA",
"bloque_sentencias : ABRELLAV sentencias_ejecutables CIERRALLAV",
"sentencias_ejecutables : sentencia PUNTOCOMA",
"sentencias_ejecutables : sentencias_ejecutables sentencia PUNTOCOMA",
"sentencias_ejecutables : seleccion",
"sentencias_ejecutables : sentencias_ejecutables seleccion",
"sentencias_ejecutables : sentencias_ejecutables error PUNTOCOMA",
"sentencias_ejecutables : error PUNTOCOMA",
"sentencia : PRINT ABREPAR CAD CIERRAPAR",
"sentencia : asignacion",
"sentencia : iteracion",
"asignacion : variable ASIG expresion",
"iteracion : DO bloque_sentencias UNTIL ABREPAR condicion CIERRAPAR",
"iteracion : DO UNTIL ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL ABREPAR condicion",
"seleccion : cabecera_seleccion THEN cuerpo_seleccion",
"seleccion : cabecera_seleccion cuerpo_seleccion",
"cuerpo_seleccion : bloque_then bloque_else",
"cuerpo_seleccion : bloque_final",
"bloque_then : bloque_sentencias ELSE",
"bloque_final : bloque_sentencias",
"bloque_else : bloque_sentencias",
"cabecera_seleccion : IF ABREPAR condicion CIERRAPAR",
"cabecera_seleccion : IF error condicion CIERRAPAR",
"cabecera_seleccion : IF condicion CIERRAPAR",
"condicion : expresion comparador expresion",
"expresion : expresion MAS termino",
"expresion : expresion MENOS termino",
"expresion : termino",
"termino : termino POR factor",
"termino : termino DIV factor",
"termino : factor",
"variable : ID",
"variable : ID ABRECOR expresion CIERRACOR",
"variable : ID ABRECOR CIERRACOR",
"variable : ID ABRECOR expresion",
"variable : ID CIERRACOR",
"factor : ID",
"factor : CTE",
"factor : ID ABRECOR expresion CIERRACOR",
"factor : MENOS CTE",
"comparador : IGUAL",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : MENOR",
"comparador : MAYOR",
"comparador : DISTINTO",
};

//#line 144 "parser.y"

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
//#line 478 "Parser.java"
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
//#line 28 "parser.y"
{ detections.add("Declaracion de variable comun en linea " + 										lineNumber); }
break;
case 8:
//#line 29 "parser.y"
{ detections.add("Declaracion de variable vector en linea " + lineNumber); }
break;
case 9:
//#line 33 "parser.y"
{yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
break;
case 10:
//#line 34 "parser.y"
{yyerror("Error: Se esperaba '..' en linea " + lineNumber);}
break;
case 11:
//#line 35 "parser.y"
{yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
break;
case 12:
//#line 36 "parser.y"
{yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
break;
case 13:
//#line 37 "parser.y"
{yyerror("Error: Falta la palabra reservada 'VECTOR' en linea " + lineNumber);}
break;
case 14:
//#line 38 "parser.y"
{yyerror("Error: Falta la palabra reservada 'DE' en linea " + lineNumber);}
break;
case 15:
//#line 39 "parser.y"
{yyerror("Error: Falta tipo del vector en linea " + lineNumber);}
break;
case 16:
//#line 40 "parser.y"
{yyerror("Error: Nombre variable en linea " + lineNumber);}
break;
case 27:
//#line 60 "parser.y"
{yyerror("Codigo erroneo" + lineNumber);}
break;
case 28:
//#line 61 "parser.y"
{yyerror("Codigo erroneo" + lineNumber);}
break;
case 29:
//#line 65 "parser.y"
{ detections.add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ val_peek(1).sval); }
break;
case 32:
//#line 70 "parser.y"
{ detections.add("Asignacion en linea  "+lineNumber); }
break;
case 33:
//#line 73 "parser.y"
{ detections.add("Iterar en linea  "+lineNumber); }
break;
case 34:
//#line 74 "parser.y"
{yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);}
break;
case 35:
//#line 75 "parser.y"
{yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
break;
case 36:
//#line 76 "parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 37:
//#line 77 "parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);}
break;
case 39:
//#line 81 "parser.y"
{yyerror("vazco gato"+ lineNumber); }
break;
case 43:
//#line 92 "parser.y"
{ detections.add("Declaracion if en linea "+lineNumber); }
break;
case 44:
//#line 95 "parser.y"
{ detections.add("Declaracion if else en linea "+lineNumber); }
break;
case 46:
//#line 99 "parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
break;
case 47:
//#line 100 "parser.y"
{yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
break;
case 57:
//#line 118 "parser.y"
{yyerror("Error: Se espera una exprecion entre los corchetes en linea "+ lineNumber);}
break;
case 58:
//#line 119 "parser.y"
{yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
break;
case 59:
//#line 120 "parser.y"
{yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}
break;
case 63:
//#line 126 "parser.y"
{
   	   		if (val_peek(0).ival > 32768 ) {
   	   			yyerror("Numero negativo debajo del rango en linea "+lineNumber); 
   	   		}
   	   		/* Aca hay que agregar a la tabla de simbolos el - y sacar el otro, pero hay que usar semantico */
   	   	}
break;
//#line 744 "Parser.java"
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
