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
    0,    0,    0,    0,    1,    1,    3,    3,    3,    4,
    4,    5,    5,    6,    6,    2,    2,    7,    7,    7,
    7,    7,    7,    7,    7,    9,   10,   10,   10,   10,
   10,    8,    8,   15,   15,   16,   18,   17,   14,   14,
   14,   14,   14,   14,   14,   13,   12,   12,   12,   20,
   20,   20,   11,   11,   11,   11,   11,   21,   21,   21,
   21,   19,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    2,    3,    2,    9,    1,    1,
    1,    1,    3,    1,    3,    1,    2,    5,    1,    2,
    2,    1,    1,    4,    4,    3,    6,    5,    5,    5,
    5,    3,    1,    2,    1,    2,    1,    1,    4,    3,
    2,    2,    3,    2,    1,    3,    3,    3,    1,    3,
    3,    1,    1,    4,    3,    3,    2,    1,    1,    4,
    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   10,   11,    0,    0,    0,    0,    0,
    0,    0,   16,   19,    0,    0,    0,    0,   59,    0,
    0,   41,    0,    0,   44,    0,   52,    0,    0,    0,
    0,    0,    0,    0,   14,    0,   57,    0,    0,   17,
    5,   12,    0,   20,   21,    0,    0,    0,   40,    0,
   61,   63,   64,   62,    0,    0,   65,   66,   67,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   55,    0,    6,    0,    0,    0,   32,    0,   35,    0,
   39,    0,    0,    0,   50,   51,   25,    0,    0,   15,
    0,    0,    0,    0,   54,   13,   36,   38,   34,   60,
   18,   28,    0,   30,   29,    0,   27,    0,    0,    0,
    8,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   43,   34,   13,   14,   15,   16,
   17,   24,   25,   18,   77,   78,   99,   79,   60,   26,
   27,
};
final static short yysindex[] = {                      -151,
    0, -222, -253,    0,    0, -220, -191,    0, -151, -144,
 -272, -250,    0,    0, -264, -259, -241, -226,    0, -234,
 -198,    0, -218, -195,    0, -199,    0, -216, -204,    0,
 -206, -176, -144, -254,    0, -256,    0, -144, -215,    0,
    0,    0, -171,    0,    0, -242, -202, -242,    0, -194,
    0,    0,    0,    0, -242, -242,    0,    0,    0, -242,
 -242, -242, -187, -161, -242, -188, -249, -173, -242, -185,
    0, -217,    0, -143, -160, -122,    0, -202,    0, -183,
    0, -199, -199, -160,    0,    0,    0, -149, -137,    0,
 -242, -136, -135, -126,    0,    0,    0,    0,    0,    0,
    0,    0, -133,    0,    0, -134,    0, -121, -119, -138,
    0,
};
final static short yyrindex[] = {                       144,
  178,    0,    0,    0,    0,    0, -125,    0,  148,  149,
    0,    0,    0,    0,  215,  223,    0,    0,    0,    1,
 -116,    0,    0,    0,    0,   34,    0,    0,    0,  193,
    0, -125,    0,    0,    0,    0,    0,  151,    0,    0,
    0,    0, -132,    0,    0,    0,    0,    0,    0, -114,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -153,
    0, -115,    0,    0,  155,  253,    0,    0,    0,    0,
    0,   67,  100,  133,    0,    0,    0,  245,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  168,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,   -3,  147,   43,    0,  -45,   -6,    0,    0,    0,
    0,  -31,  -18,    0,    0,    0,    0,    0,    0,   76,
   73,
};
final static int YYTABLESIZE=530;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         35,
   58,   76,   50,   40,   72,   38,   30,    2,   68,    3,
   70,   20,    6,   41,   75,   28,   80,   42,   32,   69,
   29,   44,   71,   23,   19,   20,   45,   90,   84,   67,
   46,   40,   98,   49,   72,   30,    2,   23,    3,   47,
   35,    6,   31,   48,   19,   20,   89,   32,   51,   92,
   93,   21,   22,   30,    2,   33,    3,   23,   63,    6,
   40,   95,   55,   56,   64,   32,   48,   65,   19,   20,
   73,   35,  103,   33,   52,   53,   49,   54,   19,   20,
   81,   23,   61,   62,   55,   56,   36,   37,   57,   58,
   71,   23,   59,   19,   20,  100,   55,   56,   87,   47,
   91,   66,   37,   94,    1,    2,   23,    3,    4,    5,
    6,   30,    2,   88,    3,   74,    7,    6,   59,   55,
   56,    4,    5,   32,   96,   59,   59,   59,   59,   59,
   82,   83,   46,   85,   86,   97,  101,  102,  104,  105,
  106,  107,  109,    1,  108,  110,   53,    2,    3,   42,
    4,   43,  111,    7,   26,   39,   56,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   31,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   22,    0,    0,    0,    0,    0,
    0,    0,   23,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   24,    0,    0,    0,    0,    0,
    0,    0,   37,    0,    0,    0,   58,   58,   58,   58,
    0,    0,   58,   58,    0,    0,   58,    0,   58,    0,
   58,   58,   58,   58,   58,   58,    0,   58,    0,   58,
   58,   58,   58,   58,   58,   58,   58,    0,   58,   49,
   49,   49,   49,    0,    0,   49,   49,    0,    0,   49,
    0,   49,    0,   49,   49,   49,   49,   49,   49,    0,
   49,    0,   49,   49,   49,    0,    0,   49,   49,   49,
    0,   49,   48,   48,   48,   48,    0,    0,   48,   48,
    0,    0,   48,    0,   48,    0,   48,   48,   48,   48,
   48,   48,    0,   48,    0,   48,   48,   48,    0,    0,
   48,   48,   48,    0,   48,   47,   47,   47,   47,    0,
    0,   47,   47,    0,    0,   47,    0,   47,    0,   47,
   47,   47,   47,   47,   47,    0,   47,    0,   47,   47,
   47,    0,    0,   47,   47,   47,    0,   47,   46,   46,
   46,   46,    0,    0,   46,   46,    0,    0,   46,    0,
   46,    0,    0,    0,    0,    0,   46,   46,    0,   46,
   26,   26,   26,   26,    0,    0,   26,   26,   46,    0,
    0,    0,   26,   31,   31,   31,   31,    0,   26,   31,
   31,   26,    0,   33,   33,   31,   33,    0,    0,   33,
   26,   31,    0,   45,   31,   33,    0,    0,   33,   33,
   33,   33,    0,   31,   33,   33,    0,    0,   45,    0,
   33,    0,    0,    9,    0,    0,   33,    0,    0,   33,
   22,   22,   22,   22,    0,    0,   22,   22,   23,   23,
   23,   23,   22,    0,   23,   23,    0,    0,   22,    0,
   23,   22,    0,    0,    0,    0,   23,    0,    0,   23,
   24,   24,   24,   24,    0,    0,   24,   24,   37,   37,
    0,   37,   24,    0,   37,   37,    0,    0,   24,    0,
   37,   24,    0,    0,    0,    0,   37,    0,    0,   37,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          6,
    0,   47,   21,   10,   36,    9,  256,  257,  263,  259,
  267,  268,  262,  286,   46,  269,   48,  268,  268,  274,
  274,  286,  279,  280,  267,  268,  286,  277,   60,   33,
  272,   38,   78,    0,   66,  256,  257,  280,  259,  266,
   47,  262,  263,  278,  267,  268,   65,  268,  267,   68,
   69,  274,  275,  256,  257,  276,  259,  280,  275,  262,
   67,  279,  280,  281,  269,  268,    0,  274,  267,  268,
  286,   78,   91,  276,  270,  271,  275,  273,  267,  268,
  275,  280,  282,  283,  280,  281,  278,  279,  284,  285,
  279,  280,  288,  267,  268,  279,  280,  281,  286,    0,
  274,  278,  279,  289,  256,  257,  280,  259,  260,  261,
  262,  256,  257,  275,  259,  287,  268,  262,  272,  280,
  281,  260,  261,  268,  268,  279,  280,  281,  282,  283,
   55,   56,    0,   61,   62,  258,  286,  275,  275,  275,
  267,  275,  264,    0,  279,  265,  272,    0,    0,  266,
    0,  266,  110,  286,    0,    9,  272,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,    0,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,    0,   -1,   -1,   -1,  256,  257,  258,  259,
   -1,   -1,  262,  263,   -1,   -1,  266,   -1,  268,   -1,
  270,  271,  272,  273,  274,  275,   -1,  277,   -1,  279,
  280,  281,  282,  283,  284,  285,  286,   -1,  288,  256,
  257,  258,  259,   -1,   -1,  262,  263,   -1,   -1,  266,
   -1,  268,   -1,  270,  271,  272,  273,  274,  275,   -1,
  277,   -1,  279,  280,  281,   -1,   -1,  284,  285,  286,
   -1,  288,  256,  257,  258,  259,   -1,   -1,  262,  263,
   -1,   -1,  266,   -1,  268,   -1,  270,  271,  272,  273,
  274,  275,   -1,  277,   -1,  279,  280,  281,   -1,   -1,
  284,  285,  286,   -1,  288,  256,  257,  258,  259,   -1,
   -1,  262,  263,   -1,   -1,  266,   -1,  268,   -1,  270,
  271,  272,  273,  274,  275,   -1,  277,   -1,  279,  280,
  281,   -1,   -1,  284,  285,  286,   -1,  288,  256,  257,
  258,  259,   -1,   -1,  262,  263,   -1,   -1,  266,   -1,
  268,   -1,   -1,   -1,   -1,   -1,  274,  275,   -1,  277,
  256,  257,  258,  259,   -1,   -1,  262,  263,  286,   -1,
   -1,   -1,  268,  256,  257,  258,  259,   -1,  274,  262,
  263,  277,   -1,  256,  257,  268,  259,   -1,   -1,  262,
  286,  274,   -1,  266,  277,  268,   -1,   -1,  256,  257,
  258,  259,   -1,  286,  262,  263,   -1,   -1,  266,   -1,
  268,   -1,   -1,  286,   -1,   -1,  274,   -1,   -1,  277,
  256,  257,  258,  259,   -1,   -1,  262,  263,  256,  257,
  258,  259,  268,   -1,  262,  263,   -1,   -1,  274,   -1,
  268,  277,   -1,   -1,   -1,   -1,  274,   -1,   -1,  277,
  256,  257,  258,  259,   -1,   -1,  262,  263,  256,  257,
   -1,  259,  268,   -1,  262,  263,   -1,   -1,  274,   -1,
  268,  277,   -1,   -1,   -1,   -1,  274,   -1,   -1,  277,
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
"sentencias_declarativas_simples : error",
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
"seleccion : error",
"cuerpo_seleccion : bloque_then bloque_else",
"cuerpo_seleccion : bloque_final",
"bloque_then : bloque_sentencias ELSE",
"bloque_final : bloque_sentencias",
"bloque_else : bloque_sentencias",
"cabecera_seleccion : IF ABREPAR condicion CIERRAPAR",
"cabecera_seleccion : IF ABREPAR CIERRAPAR",
"cabecera_seleccion : IF CIERRAPAR",
"cabecera_seleccion : IF ABREPAR",
"cabecera_seleccion : IF ABREPAR condicion",
"cabecera_seleccion : IF condicion",
"cabecera_seleccion : error",
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

//#line 134 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"

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
//#line 509 "Parser.java"
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
//#line 29 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable comun en linea "+lineNumber); }
break;
case 8:
//#line 30 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable vector en linea "+lineNumber); }
break;
case 9:
//#line 31 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ yyerror("Error: Declaracion de variables mal hecha en " + lineNumber); }
break;
case 18:
//#line 51 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion imprimir en "+lineNumber+" cadena "+ val_peek(2).sval); }
break;
case 22:
//#line 55 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la asignacion");}
break;
case 23:
//#line 56 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la iteracion");}
break;
case 24:
//#line 57 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en imprimir");}
break;
case 25:
//#line 58 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Fasida");}
break;
case 28:
//#line 65 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un bloque de sentencias");}
break;
case 29:
//#line 66 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Hasta'");}
break;
case 30:
//#line 67 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto'");}
break;
case 31:
//#line 68 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado'");}
break;
case 33:
//#line 72 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ yyerror("Error: if mal hecho en " + lineNumber); }
break;
case 37:
//#line 83 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if en "+lineNumber); }
break;
case 38:
//#line 86 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if else en "+lineNumber); }
break;
case 40:
//#line 90 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF sin ninguna condicion");}
break;
case 41:
//#line 91 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF  con un cierre de parentesis inesperado");}
break;
case 42:
//#line 92 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF sin condicion");}
break;
case 43:
//#line 93 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF  sin cierre de parentesis");}
break;
case 44:
//#line 94 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF sin parentesis");}
break;
case 45:
//#line 95 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo");}
break;
case 55:
//#line 113 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera una exprecion entre los corchetes");}
break;
case 56:
//#line 114 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera que se cierre corchetes");}
break;
case 57:
//#line 115 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Cierre de corchetes inesperado");}
break;
case 61:
//#line 121 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{if (val_peek(0).ival > 32768 ) {yyerror("Numero negativo debajo del rango "+lineNumber); } }
break;
//#line 758 "Parser.java"
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
