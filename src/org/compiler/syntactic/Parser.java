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



//#line 2 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
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
    0,    0,    0,    0,    1,    1,    1,    1,    3,    3,
    4,    4,    5,    5,    6,    6,    2,    2,    7,    7,
    7,    7,    7,    7,    7,    7,    9,   10,   10,   10,
   10,   10,    8,   15,   15,   16,   18,   17,   14,   14,
   14,   13,   12,   12,   12,   20,   20,   20,   11,   11,
   11,   11,   11,   21,   21,   21,   21,   19,   19,   19,
   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    2,    3,    1,    2,    2,    9,
    1,    1,    1,    3,    1,    3,    1,    2,    5,    1,
    2,    2,    1,    1,    4,    4,    3,    6,    5,    5,
    5,    5,    3,    2,    1,    2,    1,    1,    4,    4,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    4,
    3,    3,    2,    1,    1,    4,    2,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   11,   12,    0,    0,    0,    0,    0,    0,
    0,   17,   20,    0,    0,    0,    0,    0,   55,    0,
    0,    0,    0,    0,    0,   48,    0,    0,    0,    0,
    0,    0,   15,    0,   53,    0,    0,   18,    5,   13,
    0,   21,   22,    0,    0,    0,    0,    0,   57,   59,
   60,   58,    0,    0,   61,   62,   63,    0,   41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   51,
    0,    6,    0,    0,    0,   33,    0,   35,   40,    0,
   39,    0,    0,    0,   46,   47,   26,    0,    0,   16,
    0,    0,    0,    0,   50,   14,   36,   38,   34,   56,
   19,   29,    0,   31,   30,    0,   28,    0,    0,    0,
   10,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   41,   32,   12,   13,   14,   15,
   16,   23,   24,   17,   76,   77,   99,   78,   58,   25,
   26,
};
final static short yysindex[] = {                      -156,
 -248, -256,    0,    0, -220, -238,    0, -156, -149, -277,
 -246,    0,    0, -261, -259, -241, -213, -244,    0, -223,
 -244, -203, -204, -197, -168,    0, -190, -199, -202, -234,
 -149, -258,    0, -193,    0, -149, -198,    0,    0,    0,
 -180,    0,    0, -244, -205, -166, -244, -157,    0,    0,
    0,    0, -244, -244,    0,    0,    0, -244,    0, -244,
 -244, -175, -148, -244, -185, -247, -178, -244, -163,    0,
 -188,    0, -138, -164, -130,    0, -205,    0,    0, -181,
    0, -168, -168, -164,    0,    0,    0, -155, -143,    0,
 -244, -142, -141, -132,    0,    0,    0,    0,    0,    0,
    0,    0, -139,    0,    0, -137,    0, -126, -125, -140,
    0,
};
final static short yyrindex[] = {                       139,
    0,    0,    0,    0,    0, -131,    0,  143,  144,  229,
    0,    0,    0,  175,  182,    0,    0,    0,    0,    1,
    0,    0,    0,    0,   33,    0,    0,    0,    0, -131,
    0,    0,    0,    0,    0,  145,  239,    0,    0,    0,
  160,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -221,    0,
 -124,    0,    0,  137,  210,    0,    0,    0,    0,    0,
    0,   65,   97,  129,    0,    0,    0,  203,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  150,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,   -2,  138,   37,    0,  -43,    2,    0,    0,    0,
    0,  -30,  -18,    0,    0,    0,    0,    0,    0,   69,
   64,
};
final static int YYTABLESIZE=507;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   54,   75,   48,   71,   67,   36,   33,   18,   39,    1,
   38,    2,   27,   74,    5,   68,   80,   28,   19,   20,
   30,   40,   19,   20,   42,   21,   43,   84,   66,   90,
   44,   22,   45,   98,   71,   22,    1,   38,    2,   34,
   35,    5,   29,   65,   35,   89,   33,   30,   92,   93,
   55,    1,   45,    2,   47,   31,    5,   55,   55,   55,
   55,   55,   30,   49,   44,   50,   51,   38,   52,   63,
   31,   64,  103,   69,   20,   53,   54,   59,   33,   55,
   56,   19,   20,   57,   62,   70,   22,   72,   19,   20,
   95,   53,   54,   70,   22,   91,   43,  100,   53,   54,
    1,   22,    2,    3,    4,    5,   73,    1,   79,    2,
   87,    6,    5,   60,   61,   53,   54,   81,   30,    3,
    4,   82,   83,   85,   86,   94,   88,   97,   42,   96,
  101,  102,  104,  105,  106,  107,   27,  109,    1,  110,
   49,  108,    2,    3,    4,   37,  111,   52,    0,   32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   23,    0,    0,    0,    0,    0,
    0,   24,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   25,    0,    0,    0,    0,    0,    0,   37,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    7,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    8,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   54,   54,   54,
    0,    0,   54,   54,    0,    0,    0,    0,   54,    0,
   54,   54,   54,   54,   54,   54,    0,   54,    0,   54,
   54,   54,   54,   54,   54,   54,   54,    0,   54,   45,
   45,   45,    0,    0,   45,   45,    0,    0,    0,    0,
   45,    0,   45,   45,   45,   45,   45,   45,    0,   45,
    0,   45,   45,   45,    0,    0,   45,   45,   45,    0,
   45,   44,   44,   44,    0,    0,   44,   44,    0,    0,
    0,    0,   44,    0,   44,   44,   44,   44,   44,   44,
    0,   44,    0,   44,   44,   44,    0,    0,   44,   44,
   44,    0,   44,   43,   43,   43,    0,    0,   43,   43,
    0,    0,    0,    0,   43,    0,   43,   43,   43,   43,
   43,   43,    0,   43,    0,   43,   43,   43,    0,    0,
   43,   43,   43,    0,   43,   42,   42,   42,    0,    0,
   42,   42,    0,   27,   27,   27,   42,    0,   27,   27,
    0,    0,   42,   42,   27,   42,   32,   32,   32,    0,
   27,   32,   32,   27,   42,    0,    9,   32,    9,    9,
    9,    9,   27,   32,    0,    0,   32,    9,    0,    0,
    0,   23,   23,   23,    0,   32,   23,   23,   24,   24,
   24,    0,   23,   24,   24,    9,    0,    0,   23,   24,
    0,   23,    0,    0,    0,   24,    0,    0,   24,   25,
   25,   25,    0,    0,   25,   25,   37,    0,   37,    0,
   25,   37,   37,    0,    0,    0,   25,   37,    0,   25,
    0,    0,    0,   37,    0,    7,   37,    7,    7,    7,
    7,    0,    0,    0,    0,    8,    7,    8,    8,    8,
    8,    0,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         18,
    0,   45,   21,   34,  263,    8,    5,  256,  286,  257,
    9,  259,  269,   44,  262,  274,   47,  274,  267,  268,
  268,  268,  267,  268,  286,  274,  286,   58,   31,  277,
  272,  280,    0,   77,   65,  280,  257,   36,  259,  278,
  279,  262,  263,  278,  279,   64,   45,  268,   67,   68,
  272,  257,  266,  259,  278,  276,  262,  279,  280,  281,
  282,  283,  268,  267,    0,  270,  271,   66,  273,  269,
  276,  274,   91,  267,  268,  280,  281,  275,   77,  284,
  285,  267,  268,  288,  275,  279,  280,  286,  267,  268,
  279,  280,  281,  279,  280,  274,    0,  279,  280,  281,
  257,  280,  259,  260,  261,  262,  287,  257,  275,  259,
  286,  268,  262,  282,  283,  280,  281,  275,  268,  260,
  261,   53,   54,   60,   61,  289,  275,  258,    0,  268,
  286,  275,  275,  275,  267,  275,    0,  264,    0,  265,
  272,  279,    0,    0,    0,    8,  110,  272,   -1,    0,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,    0,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
   -1,   -1,  262,  263,   -1,   -1,   -1,   -1,  268,   -1,
  270,  271,  272,  273,  274,  275,   -1,  277,   -1,  279,
  280,  281,  282,  283,  284,  285,  286,   -1,  288,  257,
  258,  259,   -1,   -1,  262,  263,   -1,   -1,   -1,   -1,
  268,   -1,  270,  271,  272,  273,  274,  275,   -1,  277,
   -1,  279,  280,  281,   -1,   -1,  284,  285,  286,   -1,
  288,  257,  258,  259,   -1,   -1,  262,  263,   -1,   -1,
   -1,   -1,  268,   -1,  270,  271,  272,  273,  274,  275,
   -1,  277,   -1,  279,  280,  281,   -1,   -1,  284,  285,
  286,   -1,  288,  257,  258,  259,   -1,   -1,  262,  263,
   -1,   -1,   -1,   -1,  268,   -1,  270,  271,  272,  273,
  274,  275,   -1,  277,   -1,  279,  280,  281,   -1,   -1,
  284,  285,  286,   -1,  288,  257,  258,  259,   -1,   -1,
  262,  263,   -1,  257,  258,  259,  268,   -1,  262,  263,
   -1,   -1,  274,  275,  268,  277,  257,  258,  259,   -1,
  274,  262,  263,  277,  286,   -1,  257,  268,  259,  260,
  261,  262,  286,  274,   -1,   -1,  277,  268,   -1,   -1,
   -1,  257,  258,  259,   -1,  286,  262,  263,  257,  258,
  259,   -1,  268,  262,  263,  286,   -1,   -1,  274,  268,
   -1,  277,   -1,   -1,   -1,  274,   -1,   -1,  277,  257,
  258,  259,   -1,   -1,  262,  263,  257,   -1,  259,   -1,
  268,  262,  263,   -1,   -1,   -1,  274,  268,   -1,  277,
   -1,   -1,   -1,  274,   -1,  257,  277,  259,  260,  261,
  262,   -1,   -1,   -1,   -1,  257,  268,  259,  260,  261,
  262,   -1,   -1,   -1,   -1,   -1,  268,
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
"sentencias_declarativas : sentencias_declarativas_simples",
"sentencias_declarativas : sentencias_declarativas sentencias_declarativas_simples",
"sentencias_declarativas_simples : tipo variables",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo",
"tipo : INT",
"tipo : UINT",
"variables : ID",
"variables : variables COMA ID",
"bloque_sentencias : sentencia",
"bloque_sentencias : ABRELLAV sentencias_ejecutables CIERRALLAV",
"sentencias_ejecutables : sentencia",
"sentencias_ejecutables : sentencias_ejecutables sentencia",
"sentencia : PRINT ABREPAR CAD CIERRAPAR PUNTOCOMA",
"sentencia : seleccion",
"sentencia : asignacion PUNTOCOMA",
"sentencia : iteracion PUNTOCOMA",
"sentencia : asignacion",
"sentencia : iteracion",
"sentencia : PRINT ABREPAR CAD CIERRAPAR",
"sentencia : PRINT CAD CIERRAPAR PUNTOCOMA",
"asignacion : variable ASIG expresion",
"iteracion : DO bloque_sentencias UNTIL ABREPAR condicion CIERRAPAR",
"iteracion : DO UNTIL ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL ABREPAR condicion",
"seleccion : cabecera_seleccion THEN cuerpo_seleccion",
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

//#line 130 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private String s;


void yyerror(String s) {
	errors.add(s);
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
//#line 501 "Parser.java"
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
//#line 26 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la asignacion en linea " + lineNumber);}
break;
case 8:
//#line 27 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la asignacion en linea " + lineNumber);}
break;
case 9:
//#line 30 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable comun en linea " + lineNumber); }
break;
case 10:
//#line 31 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable vector en linea " + lineNumber); }
break;
case 19:
//#line 52 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ val_peek(2).sval); }
break;
case 23:
//#line 56 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la asignacion en linea " + lineNumber);}
break;
case 24:
//#line 57 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la iteracion en linea " + lineNumber);}
break;
case 25:
//#line 58 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en imprimir en linea "+ lineNumber);}
break;
case 26:
//#line 59 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 29:
//#line 66 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);}
break;
case 30:
//#line 67 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
break;
case 31:
//#line 68 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 32:
//#line 69 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);}
break;
case 37:
//#line 83 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if en linea "+lineNumber); }
break;
case 38:
//#line 86 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if else en linea "+lineNumber); }
break;
case 40:
//#line 90 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
break;
case 41:
//#line 91 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
break;
case 51:
//#line 109 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera una exprecion entre los corchetes en linea "+ lineNumber);}
break;
case 52:
//#line 110 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
break;
case 53:
//#line 111 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}
break;
case 57:
//#line 117 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{if (val_peek(0).ival > 32768 ) {yyerror("Numero negativo debajo del rango en linea "+lineNumber); } }
break;
//#line 734 "Parser.java"
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
