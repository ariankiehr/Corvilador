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
import org.compiler.arbolito.*;
//#line 23 "Parser.java"




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
   20,   11,   11,   11,   11,   11,   22,   23,   21,   21,
   21,   21,   19,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    1,    2,    3,   10,    9,    9,
    9,    9,    9,    9,    9,    9,    2,    2,    1,    1,
    1,    3,    2,    3,    3,    3,    2,    3,    1,    2,
    3,    2,    4,    1,    1,    4,    4,    4,    3,    6,
    5,    5,    5,    5,    3,    2,    2,    1,    2,    1,
    1,    4,    4,    3,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    4,    3,    2,    1,    4,    1,    1,
    1,    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   19,   20,    0,    0,    0,    0,    0,
    5,    0,    0,   29,   34,   35,    0,    0,   62,   63,
    0,   32,    0,   70,    0,    0,    0,    0,    0,    0,
   61,   69,   71,    0,    0,    0,    0,    0,    0,    0,
    0,   17,    0,   66,    0,    6,    0,    0,   30,   21,
   18,    0,   27,    0,    0,    0,   46,    0,   48,    0,
    0,    0,    0,   72,   74,   75,   73,    0,    0,   76,
   77,   78,    0,   54,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   23,    0,    0,    0,
   31,   28,    7,    0,    0,   45,   49,   51,   47,    0,
   53,    0,   52,    0,    0,    0,   59,   60,   36,   38,
   37,   33,   25,    0,    0,    0,   24,    0,    0,    0,
   64,    0,    0,    0,   68,   22,    0,   41,    0,   43,
   42,    0,    0,    0,    0,    0,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   16,    9,   10,
   11,   12,   13,   14,   15,    0,    8,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   52,   56,   13,   14,   15,   16,
   17,   28,   29,   18,   57,   58,   99,   59,   73,   30,
   31,   32,   33,
};
final static short yysindex[] = {                        -7,
 -152, -184, -230,    0,    0, -147, -245,    0,   -7,   17,
    0,   28, -261,    0,    0,    0, -219, -110,    0,    0,
 -239,    0, -232,    0, -220, -232, -194, -153, -168, -252,
    0,    0,    0, -178, -237,   25, -161, -208,   25, -195,
 -156,    0, -193,    0,   17,    0, -138, -135,    0,    0,
    0, -150,    0, -232, -109, -133,    0, -109,    0, -134,
 -115, -232, -103,    0,    0,    0,    0, -232, -232,    0,
    0,    0, -232,    0, -232, -232,  -84,  -81, -238, -129,
 -158, -232, -174, -154,  -36, -232,    0, -203, -244, -105,
    0,    0,    0,  -58,  -68,    0,    0,    0,    0,  -63,
    0, -105,    0, -252, -252,  -68,    0,    0,    0,    0,
    0,    0,    0,  -52,  -65, -138,    0, -232,  -46,  -38,
    0,  -34,  -25, -167,    0,    0,  -28,    0,  -19,    0,
    0,  -17,   -9,   12, -240,    3,    0,    8,   16,   19,
   26,  -71,   27,   29,   30,   32,   33,   34, -132,  -42,
  -42,  -42,  -42,  -42,  -42,  -42,  -61,    0,    0,    0,
    0,    0,    0,    0,    0,   14,    0,
};
final static short yyrindex[] = {                       301,
    0,    0,    0,    0,    0,    0,   31,    0,  302,  304,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -102,    0,    0,    0,    0,  -83,
    0,    0,    0,    0,    0,    0,    0,   31,    0,    0,
    0,    0,    0,    0,  305,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    9,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -118,   35,
    0,    0,    0,    0,   20,    0,    0,    0,    0,    0,
    0,    0,    0,  -64,  -45, -164,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   22,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   15,  300,  -12,    0,    2,   -3,   -4,    0,    0,
    0,  -16,  -21,    0,  255,    0,    0,    0,    0,  177,
  172,    4,   11,
};
final static int YYTABLESIZE=310;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
   26,   61,   41,   19,   63,   49,   48,   40,   50,   19,
   20,  123,   19,   19,   41,  141,   20,  111,   78,   20,
   20,   19,   42,   45,   53,   34,   90,   60,   20,   75,
   76,   79,   43,   44,   24,   25,  112,   95,  142,   19,
   49,   48,   19,   35,  124,  102,   20,   27,   19,   20,
   81,   41,   54,   84,   41,   20,  106,   62,   19,   98,
  114,   19,   88,  119,  120,   20,   90,   85,   20,   83,
   44,   23,   64,   89,   25,  121,   49,   48,   86,   49,
   48,  115,   24,   25,   19,  122,   27,   19,  134,   26,
   77,   20,   24,   25,   20,   27,  129,   47,    2,  135,
    3,  116,    2,    6,    3,   27,   74,    6,   36,   38,
   55,    3,   82,   38,    6,   37,   65,   66,  113,   67,
   38,   55,  117,  156,   97,   21,   68,   69,   39,   87,
   70,   71,  157,   22,   72,   93,   94,  158,  159,  160,
  161,  162,  163,  164,  166,   36,   36,   91,    3,    3,
   92,    6,    6,   70,  100,   55,   22,   38,   38,  101,
   70,   70,   70,   70,   70,   39,   39,   67,   67,   67,
   67,  103,   67,  125,   68,   69,   67,   67,   67,   67,
   67,   67,   67,   67,  148,   67,   58,   58,   58,   58,
  109,   58,  149,  110,  165,   58,   58,   58,    4,    5,
   58,   58,   58,  127,   58,   57,   57,   57,   57,  126,
   57,   68,   69,  121,   57,   57,   57,    4,    5,   57,
   57,   57,  128,   57,   56,   56,   56,   56,  130,   56,
   24,   25,  132,   56,   56,   56,  131,  118,   56,   56,
   56,  133,   56,   27,  104,  105,  107,  108,    1,    2,
  136,    3,    4,    5,    6,  137,   26,   26,   26,   26,
    7,  138,   26,   26,   50,   50,  143,   50,   26,  139,
   50,  144,   47,    2,   26,    3,   50,   26,    6,  145,
   80,    2,  146,    3,   38,   50,    6,    4,    5,  147,
  140,  150,   38,  151,  152,   50,  153,  154,  155,  167,
    1,    2,   67,    3,    4,   39,   65,   44,   46,   96,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
    0,   23,    6,    0,   26,   10,   10,    6,    0,    6,
    0,  256,    9,   10,   18,  256,    6,  256,  256,    9,
   10,   18,  268,    9,  286,  256,   43,  267,   18,  282,
  283,  269,  278,  279,  267,  268,  275,   54,  279,   36,
   45,   45,   39,  274,  289,   62,   36,  280,   45,   39,
   36,   55,  272,   39,   58,   45,   73,  278,   55,   58,
   82,   58,  256,   85,   86,   55,   83,  263,   58,  278,
  279,  256,  267,  267,  268,  279,   81,   81,  274,   84,
   84,  256,  267,  268,   81,  289,  280,   84,  256,  274,
  269,   81,  267,  268,   84,  280,  118,  256,  257,  267,
  259,  256,  257,  262,  259,  280,  275,  262,  256,  268,
  275,  259,  274,  268,  262,  263,  270,  271,  277,  273,
  268,  286,  277,  256,  258,  278,  280,  281,  276,  286,
  284,  285,  265,  286,  288,  286,  287,  150,  151,  152,
  153,  154,  155,  156,  157,  256,  256,  286,  259,  259,
  286,  262,  262,  272,  289,  266,  286,  268,  268,  275,
  279,  280,  281,  282,  283,  276,  276,  270,  271,  272,
  273,  275,  275,  279,  280,  281,  279,  280,  281,  282,
  283,  284,  285,  286,  256,  288,  270,  271,  272,  273,
  275,  275,  264,  275,  256,  279,  280,  281,  260,  261,
  284,  285,  286,  267,  288,  270,  271,  272,  273,  268,
  275,  280,  281,  279,  279,  280,  281,  260,  261,  284,
  285,  286,  275,  288,  270,  271,  272,  273,  275,  275,
  267,  268,  267,  279,  280,  281,  275,  274,  284,  285,
  286,  267,  288,  280,   68,   69,   75,   76,  256,  257,
  279,  259,  260,  261,  262,  275,  256,  257,  258,  259,
  268,  279,  262,  263,  256,  257,  264,  259,  268,  279,
  262,  264,  256,  257,  274,  259,  268,  277,  262,  264,
  256,  257,  264,  259,  268,  277,  262,  260,  261,  264,
  279,  265,  268,  265,  265,  268,  265,  265,  265,  286,
    0,    0,  272,    0,    0,  286,  272,  286,    9,   55,
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
"sentencias_declarativas : sentencias_declarativas_simples",
"sentencias_declarativas : sentencias_declarativas sentencias_declarativas_simples",
"sentencias_declarativas_simples : tipo variables PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
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
"variable : id",
"variable : usovector",
"variable : ID ABRECOR error CIERRACOR",
"variable : ID ABRECOR expresion",
"variable : ID CIERRACOR",
"id : ID",
"usovector : ID ABRECOR expresion CIERRACOR",
"factor : id",
"factor : CTE",
"factor : usovector",
"factor : MENOS CTE",
"comparador : IGUAL",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : MENOR",
"comparador : MAYOR",
"comparador : DISTINTO",
};

//#line 486 "parser.y"

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

// VER SI HICIMOS DE SI UNA DECLARACION DE UNA VARIABLE SE USA COMO TAL Y NO SE PUEDA USAR COMO VECTOR Y VICEVERSA 
//#line 536 "Parser.java"
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
case 3:
//#line 21 "parser.y"
{
		tree = (Arbol)val_peek(0).obj;
	}
break;
case 4:
//#line 24 "parser.y"
{
		tree = (Arbol)val_peek(0).obj;
	}
break;
case 7:
//#line 34 "parser.y"
{
						List<String> vars = (List<String>)val_peek(1).obj;
						for( String s : vars ) {
							SymbolTable.getInstance().addSymbol( s , new AttributeVariableID( 
								SymbolTable.getInstance().get(s).getTypeOfToken(),  val_peek(2).sval, "variable" ) );
							if (!estaDeclarada(s)) {
								declaradas.add(s);
							}
							else {
								yyerror("La variable ya esta declarada" + lineNumber);
							}
						}
	
						add("Declaracion de variable comun en linea " + lineNumber);
					}
break;
case 8:
//#line 49 "parser.y"
{
								if ( !estaDeclarada(val_peek(9).sval) ) {
									declaradas.add(val_peek(9).sval);
								}
								else {
									yyerror("La variable ya esta declarada" + lineNumber);
								}
		
		
								if ( !esEntero((Long)val_peek(7).obj) || !esEntero((Long)val_peek(5).obj)) {
									yyerror("no son enteros" + lineNumber);
								}
								else if ( (Long)val_peek(7).obj < 0 ) {
									yyerror("la constante es menor que 0" + lineNumber);
								}
								else if ( (Long)val_peek(7).obj > (Long)val_peek(5).obj ){
									yyerror("el limite inferior es mayor al limite superior" + lineNumber);
								}
								else {
									SymbolTable.getInstance().addSymbol( String.valueOf((Long)val_peek(7).obj), new AttributeConTipo( 
										SymbolTable.getInstance().get(String.valueOf((Long)val_peek(7).obj)).getTypeOfToken(), "entero") );
									SymbolTable.getInstance().addSymbol( String.valueOf((Long)val_peek(5).obj), new AttributeConTipo( 
										SymbolTable.getInstance().get(String.valueOf((Long)val_peek(5).obj)).getTypeOfToken(), "entero") );
									String typeOfToken = SymbolTable.getInstance().get(val_peek(9).sval).getTypeOfToken();
									SymbolTable.getInstance().removeSymbol(val_peek(9).sval);
									SymbolTable.getInstance().addSymbol( val_peek(9).sval, new AttributeVector( 
										typeOfToken, val_peek(1).sval, "vector", (Long)val_peek(7).obj, (Long)val_peek(5).obj ) ); 
								}
										
						add("Declaracion de variable vector en linea " + lineNumber); }
break;
case 9:
//#line 79 "parser.y"
{yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
break;
case 10:
//#line 80 "parser.y"
{yyerror("Error: Se esperaba '..' en linea " + lineNumber);}
break;
case 11:
//#line 81 "parser.y"
{yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
break;
case 12:
//#line 82 "parser.y"
{yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
break;
case 13:
//#line 83 "parser.y"
{yyerror("Error: Falta la palabra reservada 'VECTOR' en linea " + lineNumber);}
break;
case 14:
//#line 84 "parser.y"
{yyerror("Error: Falta la palabra reservada 'DE' en linea " + lineNumber);}
break;
case 15:
//#line 85 "parser.y"
{yyerror("Error: Falta tipo del vector en linea " + lineNumber);}
break;
case 16:
//#line 86 "parser.y"
{yyerror("Error: Nombre variable en linea " + lineNumber);}
break;
case 17:
//#line 87 "parser.y"
{yyerror("Error: Palabra reservada mal escrita en linea " + lineNumber);}
break;
case 18:
//#line 88 "parser.y"
{yyerror("Error: Nombre de variable igual al tipo en linea " + lineNumber);}
break;
case 19:
//#line 91 "parser.y"
{ 
			yyval = new ParserVal("entero");  
	}
break;
case 20:
//#line 94 "parser.y"
{ 
			yyval = new ParserVal("entero_ss"); 
	}
break;
case 21:
//#line 99 "parser.y"
{ 
				List<String> vars = new LinkedList<String>(); 
				vars.add( val_peek(0).sval );
				yyval = new ParserVal(vars); 
		}
break;
case 22:
//#line 104 "parser.y"
{ List<String> vars = new LinkedList<String>(); 
								vars.add( val_peek(0).sval );
								vars.addAll( (LinkedList<String>)val_peek(2).obj );
								yyval = new ParserVal(vars);
		}
break;
case 23:
//#line 111 "parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = val_peek(1); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 24:
//#line 118 "parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(1); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 25:
//#line 125 "parser.y"
{yyerror("Error: Se esperaba '{' " + lineNumber);}
break;
case 26:
//#line 126 "parser.y"
{yyerror("Error: Se esperaba '}'" + lineNumber);}
break;
case 27:
//#line 132 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(1); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 28:
//#line 139 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal(new NodoSinTipo("sentencia",(Arbol)(val_peek(2).obj),(Arbol)(val_peek(1).obj))); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 29:
//#line 146 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(0); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 30:
//#line 153 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal(new NodoSinTipo("sentencia",(Arbol)(val_peek(1).obj),(Arbol)(val_peek(0).obj))); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 31:
//#line 160 "parser.y"
{yyerror("Codigo erroneo en linea " + lineNumber);}
break;
case 32:
//#line 161 "parser.y"
{yyerror("Codigo erroneo en linea " + lineNumber);}
break;
case 33:
//#line 165 "parser.y"
{ 
				add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ val_peek(1).sval); 
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new Hoja("imprimir", val_peek(1).sval));
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 34:
//#line 173 "parser.y"
{ 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 35:
//#line 180 "parser.y"
{ 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			}  else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 36:
//#line 187 "parser.y"
{yyerror("Error: Se espera un '(' " + lineNumber);}
break;
case 37:
//#line 188 "parser.y"
{yyerror("Error: Se espera un ')' " + lineNumber);}
break;
case 38:
//#line 189 "parser.y"
{yyerror("Error: Se espera una 'cadena' " + lineNumber);}
break;
case 39:
//#line 192 "parser.y"
{

							if ( !estaDeclarada(   ((Arbol)val_peek(2).obj).getElem()) ) {
								yyerror("La variable que intenta utilizar no ha sido declarada en la linea " + lineNumber);
							}

							if( ! (((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() )) ){
								yyerror ("Error: a la variable " + ((Arbol)val_peek(2).obj).getElem() + " se le esta asignando algo de otro tipo");
								
							} else {
								add("Asignacion en linea  "+lineNumber); 
							}

							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal( new Nodo(":=", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 40:
//#line 213 "parser.y"
{ 
					add("Iterar en linea  "+lineNumber);
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new NodoSinTipo("iterar", new NodoUnario("condicion",(Arbol)(val_peek(1).obj)), (Arbol)(val_peek(4).obj)));
					} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 41:
//#line 221 "parser.y"
{yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);}
break;
case 42:
//#line 222 "parser.y"
{yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
break;
case 43:
//#line 223 "parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 44:
//#line 224 "parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);}
break;
case 45:
//#line 227 "parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			yyval = new ParserVal( new NodoSinTipo("si", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj ) );
		} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
	}
break;
case 46:
//#line 234 "parser.y"
{yyerror("Error: falta entonces"+ lineNumber); }
break;
case 47:
//#line 238 "parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal( new NodoSinTipo("cuerpo", (Arbol)val_peek(1).obj , (Arbol)val_peek(0).obj ) ); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 48:
//#line 245 "parser.y"
{
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal(new NodoUnario("cuerpo",(Arbol)(val_peek(0).obj))); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 49:
//#line 254 "parser.y"
{ 
	if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
		yyval = new ParserVal(new NodoUnario("entonces",(Arbol)(val_peek(1).obj))); 
	} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
}
break;
case 50:
//#line 263 "parser.y"
{
						add("Declaracion if en linea " + lineNumber); 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal(new NodoUnario("entonces",(Arbol)(val_peek(0).obj))); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}

			}
break;
case 51:
//#line 274 "parser.y"
{ 
					add("Declaracion if else en linea " + lineNumber); 
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new NodoUnario("sino",(Arbol)(val_peek(0).obj)));
					} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 52:
//#line 284 "parser.y"
{
									if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
										yyval = new ParserVal(new NodoUnario("condicion",(Arbol)(val_peek(1).obj)));
									} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
								}
break;
case 53:
//#line 292 "parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
break;
case 54:
//#line 293 "parser.y"
{yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
break;
case 55:
//#line 296 "parser.y"
{
				if(! ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
		
					yyerror("difieren los tipos wuachin!");
				}

				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( val_peek(1).sval, (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 56:
//#line 310 "parser.y"
{
				if( !((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
					yyerror("difieren los tipos wuachin!");
				} 
				
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "+", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 57:
//#line 321 "parser.y"
{
				if( !((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
					yyerror("difieren los tipos wuachin!");
				} 
				
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "-", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				
			}
break;
case 58:
//#line 333 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		  }
break;
case 59:
//#line 342 "parser.y"
{
				if( ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal( new Nodo( "*", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
					} else {
						yyval = new ParserVal( new Hoja( "error", "syntax error" ));
					}
				} else {
					yyerror("difieren los tipos wuachin!");
				}
				
			}
break;
case 60:
//#line 354 "parser.y"
{
			if( ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "/", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			} else {
				yyerror("difieren los tipos wuachin!");
			}
		}
break;
case 61:
//#line 365 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
break;
case 62:
//#line 374 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
break;
case 63:
//#line 381 "parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			yyval = val_peek(0);
		} else {
			yyval = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}
break;
case 64:
//#line 388 "parser.y"
{yyerror("Error: Se espera una expresion entre los corchetes en linea "+ lineNumber);}
break;
case 65:
//#line 389 "parser.y"
{yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
break;
case 66:
//#line 390 "parser.y"
{yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}
break;
case 67:
//#line 395 "parser.y"
{
			if( !estaDeclarada(val_peek(0).sval) ) {
				yyerror("no esta declarada la variable");
			}

			if (!("variable".equals(((AttributeVariableID)SymbolTable.getInstance().get(val_peek(0).sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo variable simple e la linea " + lineNumber);
			}
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = new ParserVal(new Hoja(  val_peek(0).sval, ((AttributeVariableID)SymbolTable.getInstance().get(val_peek(0).sval)).getTypeOfElement() )); 
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
			
		}
break;
case 68:
//#line 412 "parser.y"
{

		if (!((Arbol)val_peek(1).obj).getTipo().equals("entero")) {
			yyerror("Error: El tipo del indice del vector es incorrecto en la linea "+ lineNumber);
		}

		if( !estaDeclarada(val_peek(3).sval) ) {
				yyerror("no esta declarada la variable");
		}
		if (!("vector".equals(((AttributeVariableID)SymbolTable.getInstance().get(val_peek(3).sval)).getTypeOfId()) ) ){
			yyerror("Error: La variable no es de tipo vector e la linea " + lineNumber);
		}

		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				Arbol idv = new Hoja(val_peek(3).sval, ((AttributeVector)SymbolTable.getInstance().get(val_peek(3).sval)).getTypeOfElement() );
				yyval = new ParserVal(new Nodo(val_peek(3).sval , idv, (Arbol)val_peek(1).obj, idv.getTipo()  ));
		} else {
			yyval = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}
break;
case 69:
//#line 434 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 70:
//#line 441 "parser.y"
{

	   		positivosPendientes.add( (Long)val_peek(0).obj );
	   		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = new ParserVal(new Hoja( val_peek(0).obj.toString(), 
				((AttributeConTipo)SymbolTable.getInstance().get(String.valueOf((Long)val_peek(0).obj))).getTypeOfElement() )); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
	   	}
break;
case 71:
//#line 451 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
	  	}
break;
case 72:
//#line 458 "parser.y"
{
   	   		if (((Long)val_peek(0).obj) > 32768 ) {
   	   			yyerror("Numero negativo debajo del rango en linea " + lineNumber); 
   	   			err = true;
   	   		} else {
   	   			SymbolTable.getInstance().addSymbol("-"+val_peek(0).obj, new AttributeConTipo("const","entero"));
   	   			negativosPendientes.add((Long)val_peek(0).obj);
   	   		}
			
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = new ParserVal(new Hoja( "-" + val_peek(0).obj.toString(), 
				((AttributeConTipo)SymbolTable.getInstance().get("-" + String.valueOf((Long)val_peek(0).obj))).getTypeOfElement() ));
			}  else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
   	   	}
break;
case 73:
//#line 476 "parser.y"
{ yyval = new ParserVal("=");}
break;
case 74:
//#line 477 "parser.y"
{ yyval = new ParserVal(">=");}
break;
case 75:
//#line 478 "parser.y"
{ yyval = new ParserVal("<=");}
break;
case 76:
//#line 479 "parser.y"
{ yyval = new ParserVal("<");}
break;
case 77:
//#line 480 "parser.y"
{ yyval = new ParserVal(">");}
break;
case 78:
//#line 481 "parser.y"
{ yyval = new ParserVal("^=");}
break;
//#line 1311 "Parser.java"
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
