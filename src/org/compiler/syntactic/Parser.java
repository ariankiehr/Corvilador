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
import org.compiler.symboltable.*;
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
    3,    3,    3,    3,    3,    3,    3,    3,    4,    4,
    5,    5,    6,    6,    6,    6,    2,    2,    2,    2,
    2,    2,    7,    7,    7,    7,    7,    7,    9,   10,
   10,   10,   10,   10,    8,    8,   15,   15,   16,   18,
   17,   14,   14,   14,   13,   12,   12,   12,   20,   20,
   20,   11,   11,   11,   11,   11,   21,   21,   21,   21,
   19,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    2,    3,    2,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    2,    2,    1,    1,
    1,    3,    2,    3,    3,    3,    2,    3,    1,    2,
    3,    2,    4,    1,    1,    4,    4,    4,    3,    6,
    5,    5,    5,    5,    3,    2,    2,    1,    2,    1,
    1,    4,    4,    3,    3,    3,    3,    1,    3,    3,
    1,    1,    4,    4,    3,    2,    1,    1,    4,    2,
    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   19,   20,    0,    0,    0,    0,    0,
    0,    0,    0,   29,   34,   35,    0,    0,    0,   32,
    0,   68,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,   17,    0,   66,
    0,    0,    0,    0,   30,    5,   21,   18,    0,   27,
    0,    0,    0,   46,    0,   48,    0,    0,    0,    0,
   70,   72,   73,   71,    0,    0,   74,   75,   76,    0,
   54,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   23,    0,    0,    0,    6,   31,   28,
    0,    0,   45,   49,   51,   47,    0,   53,    0,   52,
    0,    0,    0,   59,   60,   36,   38,   37,   33,   25,
    0,    0,    0,   24,    0,    0,    0,   64,    0,    0,
    0,   63,   22,    0,   69,   41,    0,   43,   42,    0,
    0,    0,    0,    0,   40,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   16,    9,   10,   11,   12,
   13,   14,   15,    8,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   49,   53,   13,   14,   15,   16,
   17,   26,   27,   18,   54,   55,   96,   56,   70,   28,
   29,
};
final static short yysindex[] = {                       -39,
 -130, -235, -239,    0,    0, -175, -206,    0,  -39,  -32,
 -268,  -22, -261,    0,    0,    0, -222, -164, -196,    0,
 -221,    0, -188, -221, -147,  -69, -162, -217,    0, -145,
 -199,  -25, -152, -210,  -25, -220, -119,    0, -244,    0,
  -32, -116, -106, -100,    0,    0,    0,    0,  -84,    0,
 -221, -153, -125,    0, -153,    0, -132, -140, -221,  -70,
    0,    0,    0,    0, -221, -221,    0,    0,    0, -221,
    0, -221, -221,  -49,  -47, -227,  -53, -201, -221, -240,
 -177, -254, -221,    0, -190, -245, -129,    0,    0,    0,
  -33, -170,    0,    0,    0,    0,  -27,    0,  -91,    0,
 -217, -217, -170,    0,    0,    0,    0,    0,    0,    0,
  -34,  -37, -106,    0, -221,  -31,  -30,    0,  -20,  -18,
 -173,    0,    0,  -28,    0,    0,  -23,    0,    0,  -26,
  -24,  -17, -237,  -16,    0,  -14,  -10,   -8,    3,  -95,
    5,    7,    8,   11,   14,   15, -151,  -66,  -66,  -66,
  -66,  -66,  -66,  -66,  -85,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                       261,
    0,    0,    0,    0,    0,    0,    2,    0,  281,  282,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -154,    0,    0,    0,    0, -126,    0,    0,
    0,    0,    0,    2,    0,    0,    0,    0,    0,    0,
  283,    0,    0,    0,    0,    0,    0,    0,    4,    0,
    0,    0,    9,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -73,   12,    0,    0,    0,
    0,    6,    0,    0,    0,    0,    0,    0,    0,    0,
 -107,  -88, -189,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   10,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   -1,  276,  -12,    0,   -2,   -3,   -4,    0,    0,
    0,  -29,  -19,    0,  235,    0,    0,    0,    0,   42,
  141,
};
final static int YYTABLESIZE=296;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   26,   58,   37,   36,   60,   45,   44,   41,   50,   87,
  120,   85,   22,   23,   37,  112,   30,   46,  139,  115,
   21,   92,   86,   23,   50,   25,   22,   23,  108,   99,
   78,   22,   23,   81,   31,   25,   45,   44,   24,   25,
  103,  140,   82,  121,   25,   22,   23,  109,   37,   51,
   87,   37,   95,   83,   43,    2,   75,    3,   25,  111,
    6,   38,  116,  117,   72,   73,   34,   80,   40,   76,
   57,   39,   40,   45,   44,  110,   45,   44,  113,    2,
   32,    3,  132,    3,    6,   55,    6,   33,  118,   59,
   34,   32,   34,  133,    3,  127,   55,    6,  119,  114,
   35,   52,   32,   34,  154,    3,  101,  102,    6,   65,
   66,   35,   71,  155,   34,   67,   67,   67,   67,   61,
   67,   79,   35,   74,   67,   67,   67,   67,   67,   67,
   67,   67,   94,   67,   98,  156,  157,  158,  159,  160,
  161,  162,  164,   58,   58,   58,   58,   19,   58,  122,
   65,   66,   58,   58,   58,   20,   97,   58,   58,   58,
  146,   58,   57,   57,   57,   57,   84,   57,  147,   88,
  163,   57,   57,   57,    4,    5,   57,   57,   57,   89,
   57,   56,   56,   56,   56,   90,   56,  125,   65,   66,
   56,   56,   56,    4,    5,   56,   56,   56,   68,   56,
   62,   63,   91,   64,  100,   68,   68,   68,   68,   68,
   65,   66,  104,  105,   67,   68,    1,    2,   69,    3,
    4,    5,    6,   43,    2,  106,    3,  107,    7,    6,
   77,    2,   20,    3,  123,   34,    6,    4,    5,  124,
  126,  118,   34,  128,  129,   47,  130,  141,  131,  142,
  134,  135,  136,  143,  137,  144,   26,   26,   26,   26,
    1,  138,   26,   26,   50,   50,  145,   50,   26,  148,
   50,  149,  150,   62,   26,  151,   50,   26,  152,  153,
    2,    3,    4,   65,   42,   50,   93,    0,    0,    7,
    0,   39,    0,    0,    0,   44,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
    0,   21,    6,    6,   24,   10,   10,    9,    0,   39,
  256,  256,  267,  268,   18,  256,  256,  286,  256,  274,
  256,   51,  267,  268,  286,  280,  267,  268,  256,   59,
   32,  267,  268,   35,  274,  280,   41,   41,  274,  280,
   70,  279,  263,  289,  280,  267,  268,  275,   52,  272,
   80,   55,   55,  274,  256,  257,  256,  259,  280,   79,
  262,  268,   82,   83,  282,  283,  268,  278,  279,  269,
  267,  278,  279,   78,   78,  277,   81,   81,  256,  257,
  256,  259,  256,  259,  262,  275,  262,  263,  279,  278,
  268,  256,  268,  267,  259,  115,  286,  262,  289,  277,
  276,  266,  256,  268,  256,  259,   65,   66,  262,  280,
  281,  276,  275,  265,  268,  270,  271,  272,  273,  267,
  275,  274,  276,  269,  279,  280,  281,  282,  283,  284,
  285,  286,  258,  288,  275,  148,  149,  150,  151,  152,
  153,  154,  155,  270,  271,  272,  273,  278,  275,  279,
  280,  281,  279,  280,  281,  286,  289,  284,  285,  286,
  256,  288,  270,  271,  272,  273,  286,  275,  264,  286,
  256,  279,  280,  281,  260,  261,  284,  285,  286,  286,
  288,  270,  271,  272,  273,  286,  275,  279,  280,  281,
  279,  280,  281,  260,  261,  284,  285,  286,  272,  288,
  270,  271,  287,  273,  275,  279,  280,  281,  282,  283,
  280,  281,   72,   73,  284,  285,  256,  257,  288,  259,
  260,  261,  262,  256,  257,  275,  259,  275,  268,  262,
  256,  257,  286,  259,  268,  268,  262,  260,  261,  267,
  275,  279,  268,  275,  275,  268,  267,  264,  267,  264,
  279,  275,  279,  264,  279,  264,  256,  257,  258,  259,
    0,  279,  262,  263,  256,  257,  264,  259,  268,  265,
  262,  265,  265,  272,  274,  265,  268,  277,  265,  265,
    0,    0,    0,  272,    9,  277,   52,   -1,   -1,  286,
   -1,  286,   -1,   -1,   -1,  286,
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
"sentencias_declarativas_simples : ID ID",
"sentencias_declarativas_simples : tipo tipo",
"tipo : INT",
"tipo : UINT",
"variables : ID",
"variables : variables COMA ID",
"bloque_sentencias : sentencia PUNTOCOMA",
"bloque_sentencias : ABRELLAV sentencias_ejecutables CIERRALLAV",
"bloque_sentencias : error sentencias_ejecutables CIERRALLAV",
"bloque_sentencias : ABRELLAV sentencias_ejecutables error",
"sentencias_ejecutables : sentencia PUNTOCOMA",
"sentencias_ejecutables : sentencias_ejecutables sentencia PUNTOCOMA",
"sentencias_ejecutables : seleccion",
"sentencias_ejecutables : sentencias_ejecutables seleccion",
"sentencias_ejecutables : sentencias_ejecutables error PUNTOCOMA",
"sentencias_ejecutables : error PUNTOCOMA",
"sentencia : PRINT ABREPAR CAD CIERRAPAR",
"sentencia : asignacion",
"sentencia : iteracion",
"sentencia : PRINT error CAD CIERRAPAR",
"sentencia : PRINT ABREPAR CAD error",
"sentencia : PRINT ABREPAR error CIERRAPAR",
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
"variable : ID ABRECOR error CIERRACOR",
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

//#line 154 "parser.y"

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private String s;
private boolean err = false;

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
//#line 502 "Parser.java"
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
{ add("Declaracion de variable comun en linea " + 										lineNumber); }
break;
case 8:
//#line 29 "parser.y"
{ add("Declaracion de variable vector en linea " + lineNumber); }
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
case 17:
//#line 41 "parser.y"
{yyerror("Error: Palabra reservada mal escrita en linea " + lineNumber);}
break;
case 18:
//#line 42 "parser.y"
{yyerror("Error: Nombre de variable igual al tipo en linea " + lineNumber);}
break;
case 25:
//#line 55 "parser.y"
{yyerror("Error: Se esperaba '{' " + lineNumber);}
break;
case 26:
//#line 56 "parser.y"
{yyerror("Error: Se esperaba '}'" + lineNumber);}
break;
case 31:
//#line 64 "parser.y"
{yyerror("Codigo erroneo en linea " + lineNumber);}
break;
case 32:
//#line 65 "parser.y"
{yyerror("Codigo erroneo en linea " + lineNumber);}
break;
case 33:
//#line 69 "parser.y"
{ add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ val_peek(1).sval); }
break;
case 36:
//#line 72 "parser.y"
{yyerror("Error: Se espera un '(' " + lineNumber);}
break;
case 37:
//#line 73 "parser.y"
{yyerror("Error: Se espera un ')' " + lineNumber);}
break;
case 38:
//#line 74 "parser.y"
{yyerror("Error: Se espera una 'cadena' " + lineNumber);}
break;
case 39:
//#line 77 "parser.y"
{ add("Asignacion en linea  "+lineNumber); }
break;
case 40:
//#line 80 "parser.y"
{ add("Iterar en linea  "+lineNumber); }
break;
case 41:
//#line 81 "parser.y"
{yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);}
break;
case 42:
//#line 82 "parser.y"
{yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
break;
case 43:
//#line 83 "parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 44:
//#line 84 "parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);}
break;
case 46:
//#line 88 "parser.y"
{yyerror("Error: falta entonces"+ lineNumber); }
break;
case 50:
//#line 99 "parser.y"
{ add("Declaracion if en linea "+lineNumber); }
break;
case 51:
//#line 102 "parser.y"
{ add("Declaracion if else en linea "+lineNumber); }
break;
case 53:
//#line 106 "parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
break;
case 54:
//#line 107 "parser.y"
{yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
break;
case 64:
//#line 126 "parser.y"
{yyerror("Error: Se espera una expresion entre los corchetes en linea "+ lineNumber);}
break;
case 65:
//#line 127 "parser.y"
{yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
break;
case 66:
//#line 128 "parser.y"
{yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}
break;
case 70:
//#line 134 "parser.y"
{
   	   		if (((Long)val_peek(0).obj) > 32768 ) {
   	   			yyerror("Numero negativo debajo del rango en linea "+lineNumber); 
   	   			err = true;
   	   		} else {
   	   			SymbolTable.getInstance().addSymbol("-"+val_peek(0).obj, new Attribute("const"));
   	   		}
   	   	}
break;
//#line 798 "Parser.java"
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
