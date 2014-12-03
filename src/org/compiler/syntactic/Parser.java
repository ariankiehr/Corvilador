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
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    4,    4,    5,    5,    6,    6,    6,    6,    6,    2,
    2,    2,    2,    2,    2,    2,    7,    7,    7,    7,
    7,    7,   10,   10,   10,   11,   11,   11,   11,   11,
   11,   11,    8,    8,   15,   15,   16,   18,   17,   14,
   14,   14,   14,   14,   13,   13,   13,   13,   12,   12,
   12,   20,   20,   20,    9,    9,   22,   22,   23,   21,
   21,   21,   21,   19,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    1,    2,    3,    3,   10,    9,
    9,    9,    9,    9,    9,    9,    7,    3,    3,    9,
    1,    1,    1,    3,    2,    3,    2,    3,    3,    2,
    3,    1,    2,    3,    2,    1,    4,    1,    1,    4,
    4,    4,    3,    2,    2,    6,    5,    5,    5,    5,
    6,    6,    3,    3,    2,    1,    2,    1,    1,    4,
    4,    3,    4,    4,    3,    2,    2,    1,    3,    3,
    1,    3,    3,    1,    1,    1,    1,    2,    4,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
   36,    0,    0,   21,   22,    0,    0,    0,    0,    0,
    0,    0,    5,    0,    0,   32,    0,   38,   39,    0,
   75,   76,    0,   81,    0,   85,   86,   84,    0,    0,
   87,   88,   89,    0,    0,    0,    0,   74,   80,   82,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   78,    0,    6,    0,   33,    0,   23,    0,    0,   35,
   30,    0,    0,    0,    0,    0,   56,    0,    0,    0,
    0,   83,    0,    0,    0,   62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   27,   25,
   18,    0,    0,    0,    0,   31,   34,   19,    8,    7,
    0,    0,   53,   57,   54,   59,   55,   61,   64,   63,
   60,    0,    0,    0,   72,   73,   40,   42,   41,   37,
   28,    0,   29,   26,    0,    0,    0,    0,    0,    0,
    0,    0,   79,   24,   47,    0,    0,    0,   49,   48,
    0,    0,    0,    0,    0,   51,   52,   46,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   17,
    0,    0,    0,    0,    0,    0,    0,    0,   10,   11,
   16,    0,   13,   12,   15,   14,    9,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   59,   64,   15,   16,   17,   18,
   19,   34,   35,   20,   65,   66,  107,   67,   36,   37,
   38,   39,   40,
};
final static short yysindex[] = {                        46,
    0,  -54, -139,    0,    0,   53, -142, -103, -240,    0,
   46,  131,    0,  -89, -241,    0, -220,    0,    0,   68,
    0,    0,  158,    0, -195,    0,    0,    0,  -35,  -82,
    0,    0,    0,  -29, -187, -103,  -90,    0,    0,    0,
 -234, -177,   97, -149,   97, -141, -232, -132,   31,  -80,
    0,  131,    0, -130,    0, -138,    0, -128, -245,    0,
    0, -103,  104, -119,  -96,  104,    0, -100, -103,  -94,
 -129,    0, -103, -103, -103,    0,  -80, -103, -103,  -79,
  -77, -237,  124,  158,   90,  -63,  139,  158,    0,    0,
    0, -170,  -49,  -30, -152,    0,    0,    0,    0,    0,
  -50,  -80,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -90,  -90,  -80,    0,    0,    0,    0,    0,    0,
    0,  -32,    0,    0,  158,   16,  -28,  -27,  -33, -147,
  -11,  -13,    0,    0,    0,   -7,   -4,    2,    0,    0,
   14, -250,   15,  -36,   18,    0,    0,    0,    9,   30,
  -58,   32,   27,   52,  -36,  -36,  -38,  -36,  -36,    0,
  -36,   33,   36, -137,   37,   42,   45,   49,    0,    0,
    0,   51,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       275,
    0,    0,    0,    0,    0,    0,   60,    0,    0,    0,
  338,  339,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -172,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -243, -118,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -223,
    0,  341,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -200,    0,    8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -229,    0, -216,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   11,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -196,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -97,  -76, -135,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -190,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   -9,  331,  -14,    0,   -1,   19,   -8,   -5,    0,
    0,   12,  -20,    0,  280,    0,    0,    0,  311,  155,
  161,    6,   10,
};
final static int YYTABLESIZE=446;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   20,   52,   68,   55,   46,   21,   56,   58,   71,   22,
   99,   21,   68,  150,   60,   22,   21,   21,  119,   50,
   22,   22,   68,   89,   47,   21,   67,   51,  151,   22,
   54,   68,   44,   83,   80,   85,   67,  120,   47,   66,
  100,  101,   68,   55,   61,   67,   56,   77,   21,   66,
   21,   62,   22,   90,   22,   45,   67,   21,   66,   43,
   95,   22,   44,  122,  106,   50,  127,  128,   21,   66,
   54,   21,   22,  102,   55,   22,   55,   56,   81,   56,
   95,   47,   69,   77,   47,   45,  114,   76,   21,   43,
   21,   82,   22,   77,   22,   50,  129,   77,   77,   77,
   77,   54,   77,   54,  136,  138,   77,   77,   77,   77,
   77,   77,   77,   77,   86,   77,   41,   97,  130,  142,
   65,   87,    4,    5,   84,   48,  133,   73,   74,  153,
   65,  143,   88,   62,   42,   49,  110,   71,  104,   65,
  162,  163,  165,  166,  167,  111,  168,   71,  171,  172,
   65,   71,   71,   91,   71,   96,   71,   98,   70,  105,
   71,   71,   71,   24,   25,   71,   71,   71,   70,   71,
    4,    5,   70,   70,  108,   70,   30,   70,   57,   69,
  109,   70,   70,   70,   72,   51,   70,   70,   70,   69,
   70,   78,   79,   69,   69,  117,   69,  118,   69,   73,
   74,   23,   69,   69,   69,  157,  158,   69,   69,   69,
  125,   69,   24,   25,  131,   26,   27,  134,   28,   29,
   70,    4,    5,    4,    5,   30,  164,  112,  113,   31,
   32,   24,   25,   33,   26,   27,  132,   28,  115,  116,
   26,   27,  135,   28,   30,  141,  139,  140,   31,   32,
   73,   74,   33,  144,   31,   32,   20,   20,   33,   20,
   20,   20,   20,   58,   58,  145,   58,  146,   20,   58,
  147,  137,   20,  155,    1,   58,  148,  149,  152,   58,
   20,  154,   24,   25,   58,   26,   27,   58,   28,   81,
   81,   81,   81,   81,  156,   30,  159,   92,   25,   31,
   32,    1,    2,   33,    3,    4,    5,    6,   43,   93,
   30,    3,  160,    7,    6,   44,  161,    8,  169,   94,
   25,  170,  173,   43,    8,    9,    3,  174,   45,    6,
  175,   77,    9,   63,  176,   25,  177,    2,    3,    8,
    4,   53,  103,   45,   75,  123,    2,    9,    3,    0,
    0,    6,    1,    2,    0,    3,    0,   25,    6,   43,
    0,    8,    3,    0,   25,    6,  124,    0,    8,    9,
    0,   25,    0,    0,    0,    8,    9,    0,    0,   45,
    2,    0,    3,    9,    0,    6,    0,    2,    0,    3,
    0,   25,    6,    0,    0,    8,    0,    0,   25,    0,
  121,    0,    8,    9,    0,   24,   25,    0,   26,   27,
    9,   28,  126,    0,    0,    0,    0,    0,   30,    0,
    0,    0,   31,   32,   24,   25,   33,   26,   27,    0,
   28,    0,    0,    0,    0,    0,    0,   30,    0,    0,
    0,   31,   32,    0,    0,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         14,
    0,   11,   23,   12,    6,    0,   12,    0,   29,    0,
  256,    6,  256,  264,  256,    6,   11,   12,  256,    8,
   11,   12,  266,  256,    6,   20,  256,  268,  279,   20,
   12,  275,  256,   43,  269,   45,  266,  275,   20,  256,
  286,  287,  286,   52,  286,  275,   52,   36,   43,  266,
   45,  272,   43,  286,   45,  256,  286,   52,  275,  256,
   49,   52,  286,   84,   66,  256,   87,   88,   63,  286,
   52,   66,   63,   62,   83,   66,   85,   83,  256,   85,
   69,   63,  278,  256,   66,  286,   75,  275,   83,  286,
   85,  269,   83,  266,   85,  286,  267,  270,  271,  272,
  273,   83,  275,   85,  125,  126,  279,  280,  281,  282,
  283,  284,  285,  286,  256,  288,  256,  256,  289,  267,
  256,  263,  260,  261,  274,  268,  279,  280,  281,  144,
  266,  279,  274,  272,  274,  278,  266,  256,  258,  275,
  155,  156,  157,  158,  159,  275,  161,  266,  286,  164,
  286,  270,  271,  286,  273,  286,  275,  286,  256,  256,
  279,  280,  281,  267,  268,  284,  285,  286,  266,  288,
  260,  261,  270,  271,  275,  273,  280,  275,  268,  256,
  275,  279,  280,  281,  267,  268,  284,  285,  286,  266,
  288,  282,  283,  270,  271,  275,  273,  275,  275,  280,
  281,  256,  279,  280,  281,  264,  265,  284,  285,  286,
  274,  288,  267,  268,  264,  270,  271,  268,  273,  274,
  256,  260,  261,  260,  261,  280,  265,   73,   74,  284,
  285,  267,  268,  288,  270,  271,  267,  273,   78,   79,
  270,  271,  275,  273,  280,  279,  275,  275,  284,  285,
  280,  281,  288,  265,  284,  285,  256,  257,  288,  259,
  260,  261,  262,  256,  257,  279,  259,  275,  268,  262,
  275,  256,  272,  265,    0,  268,  275,  264,  264,  272,
  280,  264,  267,  268,  277,  270,  271,  280,  273,  279,
  280,  281,  282,  283,  265,  280,  265,  267,  268,  284,
  285,  256,  257,  288,  259,  260,  261,  262,  256,  279,
  280,  259,  286,  268,  262,  263,  265,  272,  286,  289,
  268,  286,  286,  256,  272,  280,  259,  286,  276,  262,
  286,  272,  280,  266,  286,  268,  286,    0,    0,  272,
    0,   11,   63,  276,   34,  256,  257,  280,  259,   -1,
   -1,  262,  256,  257,   -1,  259,   -1,  268,  262,  256,
   -1,  272,  259,   -1,  268,  262,  277,   -1,  272,  280,
   -1,  268,   -1,   -1,   -1,  272,  280,   -1,   -1,  276,
  257,   -1,  259,  280,   -1,  262,   -1,  257,   -1,  259,
   -1,  268,  262,   -1,   -1,  272,   -1,   -1,  268,   -1,
  277,   -1,  272,  280,   -1,  267,  268,   -1,  270,  271,
  280,  273,  274,   -1,   -1,   -1,   -1,   -1,  280,   -1,
   -1,   -1,  284,  285,  267,  268,  288,  270,  271,   -1,
  273,   -1,   -1,   -1,   -1,   -1,   -1,  280,   -1,   -1,
   -1,  284,  285,   -1,   -1,  288,
};
}
final static short YYFINAL=10;
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
"sentencias_declarativas_simples : tipo variables error",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ID PUNTOCOMA",
"sentencias_declarativas_simples : tipo tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo",
"tipo : INT",
"tipo : UINT",
"variables : ID",
"variables : variables COMA ID",
"bloque_sentencias : sentencia PUNTOCOMA",
"bloque_sentencias : ABRELLAV sentencias_ejecutables CIERRALLAV",
"bloque_sentencias : sentencia error",
"bloque_sentencias : error sentencias_ejecutables CIERRALLAV",
"bloque_sentencias : ABRELLAV sentencias_ejecutables error",
"sentencias_ejecutables : sentencia PUNTOCOMA",
"sentencias_ejecutables : sentencias_ejecutables sentencia PUNTOCOMA",
"sentencias_ejecutables : seleccion",
"sentencias_ejecutables : sentencias_ejecutables seleccion",
"sentencias_ejecutables : sentencias_ejecutables variable error",
"sentencias_ejecutables : sentencia error",
"sentencias_ejecutables : error",
"sentencia : PRINT ABREPAR CAD CIERRAPAR",
"sentencia : asignacion",
"sentencia : iteracion",
"sentencia : PRINT error CAD CIERRAPAR",
"sentencia : PRINT ABREPAR CAD error",
"sentencia : PRINT ABREPAR error CIERRAPAR",
"asignacion : variable ASIG expresion",
"asignacion : ASIG expresion",
"asignacion : variable ASIG",
"iteracion : DO bloque_sentencias UNTIL ABREPAR condicion CIERRAPAR",
"iteracion : DO UNTIL ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL ABREPAR condicion",
"iteracion : DO bloque_sentencias error ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL ABREPAR error CIERRAPAR",
"seleccion : cabecera_seleccion THEN cuerpo_seleccion",
"seleccion : cabecera_seleccion cuerpo_seleccion error",
"cuerpo_seleccion : bloque_then bloque_else",
"cuerpo_seleccion : bloque_final",
"bloque_then : bloque_sentencias ELSE",
"bloque_final : bloque_sentencias",
"bloque_else : bloque_sentencias",
"cabecera_seleccion : IF ABREPAR condicion CIERRAPAR",
"cabecera_seleccion : IF error condicion CIERRAPAR",
"cabecera_seleccion : IF condicion CIERRAPAR",
"cabecera_seleccion : IF ABREPAR condicion THEN",
"cabecera_seleccion : IF ABREPAR error CIERRAPAR",
"condicion : expresion comparador expresion",
"condicion : comparador expresion",
"condicion : expresion comparador",
"condicion : comparador",
"expresion : expresion MAS termino",
"expresion : expresion MENOS termino",
"expresion : termino",
"termino : termino POR factor",
"termino : termino DIV factor",
"termino : factor",
"variable : id",
"variable : usovector",
"id : ID",
"id : MENOS ID",
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

//#line 591 "parser.y"

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
//#line 580 "Parser.java"
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
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			tree = (Arbol)val_peek(0).obj;
		}
	}
break;
case 4:
//#line 26 "parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			tree = (Arbol)val_peek(0).obj;
		}
	}
break;
case 7:
//#line 38 "parser.y"
{
							List<String> vars = (List<String>)val_peek(1).obj;
							for( String s : vars ) {
								SymbolTable.getInstance().addSymbol( s , new AttributeVariableID( 
									SymbolTable.getInstance().get(s).getTypeOfToken(),  val_peek(2).sval, "variable" ) );
								if (!estaDeclarada(s)) {
									declaradas.add(s);
								}
								else {
									
										yyerror("Error: La variable ya esta declarada en la linea: " + lineNumber);
								}
							}
		 
							add("Declaracion de variable comun en la linea " + lineNumber);
						}
break;
case 8:
//#line 56 "parser.y"
{yyerror("Error: Falta ';' en la declaracion de variables en la linea " + lineNumber);}
break;
case 9:
//#line 58 "parser.y"
{
								

								if ( !estaDeclarada(val_peek(9).sval) ) {
									declaradas.add(val_peek(9).sval);
								}
								else {
									yyerror("Error: La variable '" + val_peek(9).sval + "' ya esta declarada en la linea: " + lineNumber);
								}
		
		
								if ( !esEntero((Long)val_peek(7).obj) || !esEntero((Long)val_peek(5).obj)) {
									yyerror("Error: El/Los limites del vector no es/son entero/s en la linea: " + lineNumber);
								}
								else if ( (Long)val_peek(7).obj < 0 ) {
									yyerror("Error: La constante es menor que 0 en la linea: " + lineNumber);
								}
								else if ( (Long)val_peek(7).obj > (Long)val_peek(5).obj ){
									yyerror("Error: El limite inferior es mayor al limite superior en la linea: " + lineNumber);
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
case 10:
//#line 90 "parser.y"
{yyerror("Error: Se esperaba '..' en la linea " + lineNumber);}
break;
case 11:
//#line 91 "parser.y"
{yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
break;
case 12:
//#line 92 "parser.y"
{yyerror("Error: Falta la palabra reservada 'VECTOR' en la linea " + lineNumber);}
break;
case 13:
//#line 93 "parser.y"
{yyerror("Error: Falta la palabra reservada 'DE' en la linea " + lineNumber);}
break;
case 14:
//#line 94 "parser.y"
{yyerror("Error: Se esperaba una constante en la linea " + lineNumber);}
break;
case 15:
//#line 95 "parser.y"
{yyerror("Error: Se esperaba una constante en la linea " + lineNumber);}
break;
case 16:
//#line 97 "parser.y"
{yyerror("Error: Falta el tipo del vector en la linea " + lineNumber);}
break;
case 17:
//#line 99 "parser.y"
{yyerror("Error: Falta el tipo del vector en la linea " + lineNumber);}
break;
case 18:
//#line 101 "parser.y"
{yyerror("Error: Palabra reservada mal escrita en la linea " + lineNumber);}
break;
case 19:
//#line 102 "parser.y"
{yyerror("Error: Nombre de variable igual al tipo en la linea " + lineNumber);}
break;
case 20:
//#line 103 "parser.y"
{yyerror("Error: Se detecto declaracion de vector erronea, falta ';' en la linea " + lineNumber);  }
break;
case 21:
//#line 106 "parser.y"
{ 
			yyval = new ParserVal("entero");  
	}
break;
case 22:
//#line 109 "parser.y"
{ 
			yyval = new ParserVal("entero_ss"); 
	}
break;
case 23:
//#line 114 "parser.y"
{ 
				List<String> vars = new LinkedList<String>(); 
				vars.add( val_peek(0).sval );
				yyval = new ParserVal(vars); 


		}
break;
case 24:
//#line 121 "parser.y"
{ List<String> vars = new LinkedList<String>(); 
								vars.add( val_peek(0).sval );
								vars.addAll( (LinkedList<String>)val_peek(2).obj );
								yyval = new ParserVal(vars);
		}
break;
case 25:
//#line 130 "parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = val_peek(1); 
						} else {
							yyval = new ParserVal( new Hoja( "error", "syntax error" ));
						}		
					}
break;
case 26:
//#line 138 "parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(1); 
						} else {
							yyval = new ParserVal( new Hoja( "error", "syntax error" ));
						}
					}
break;
case 27:
//#line 147 "parser.y"
{yyerror("Error: falta ';' en una sentencia dentro del IF, en la linea " + lineNumber);}
break;
case 28:
//#line 149 "parser.y"
{yyerror("Error: Se esperaba '{' en la linea " + lineNumber);}
break;
case 29:
//#line 151 "parser.y"
{yyerror("Error: Se esperaba '}' en la linea " + lineNumber);}
break;
case 30:
//#line 160 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(1); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 31:
//#line 167 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal(new NodoSinTipo("sentencia",(Arbol)(val_peek(2).obj),(Arbol)(val_peek(1).obj))); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 32:
//#line 174 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(0); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 33:
//#line 181 "parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal(new NodoSinTipo("sentencia",(Arbol)(val_peek(1).obj),(Arbol)(val_peek(0).obj))); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 34:
//#line 189 "parser.y"
{yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}
break;
case 35:
//#line 191 "parser.y"
{yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}
break;
case 36:
//#line 193 "parser.y"
{yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}
break;
case 37:
//#line 197 "parser.y"
{ 
				add("Declaracion imprimir en la linea  "+lineNumber+" cadena "+ val_peek(1).sval); 
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new Hoja("imprimir", val_peek(1).sval));
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 38:
//#line 206 "parser.y"
{ 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 39:
//#line 214 "parser.y"
{ 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			}  else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 40:
//#line 222 "parser.y"
{yyerror("Error: Se detecto un PRINT erroneo, se esperaba un '(' en la linea " + lineNumber);}
break;
case 41:
//#line 223 "parser.y"
{yyerror("Error: Se detecto un PRINT erroneo, se esperaba un ')' en la linea " + lineNumber);}
break;
case 42:
//#line 224 "parser.y"
{yyerror("Error: Se detecto un PRINT erroneo, se esperaba una 'cadena' en la linea " + lineNumber);}
break;
case 43:
//#line 227 "parser.y"
{
						if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
								if ( !estaDeclarada(   ((Arbol)val_peek(2).obj).getElem()) ) {
									yyerror("Error: La variable '" + ((Arbol)val_peek(2).obj).getElem() + "' no ha sido declarada en la linea " + lineNumber);
								}
							

								if( ! (((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() )) ){
									yyerror ("Error: A la variable '" + ((Arbol)val_peek(2).obj).getElem() + "' se le esta asignando algo de otro tipo en la linea " + lineNumber);
									
								} else {
									add("Asignacion en linea  "+lineNumber); 
								}
						
								if(((Arbol)val_peek(2).obj).getElem().contains("-") ){
									yyerror("Error: La variable '" + ((Arbol)val_peek(2).obj).getElem() + "' no puede estar del lado izquierdo, en la linea " + lineNumber);
								}
								
								if( ((Arbol)val_peek(0).obj).getTipo().equals("entero_ss") && ((Arbol)val_peek(0).obj).getElem().charAt(0) == '-' ) {
									yyerror("Error: mal uso de la negacion en la linea " + lineNumber);
								}
						
							}
							
							
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal( new Nodo(":=", (Arbol)val_peek(0).obj , (Arbol)val_peek(2).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
						} else {
						yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 44:
//#line 258 "parser.y"
{yyerror("Error: Se detecto una asignacion erronea, falta parte izquierda, en la linea " + lineNumber);}
break;
case 45:
//#line 260 "parser.y"
{yyerror("Error: Se detecto una asignacion erronea, falta parte derecha, en la linea " + lineNumber);}
break;
case 46:
//#line 264 "parser.y"
{ 
					add("Iterar en linea  "+lineNumber);
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new NodoSinTipo("iterar", new NodoUnario("condicion",(Arbol)(val_peek(1).obj)), (Arbol)(val_peek(4).obj)));
					} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 47:
//#line 272 "parser.y"
{yyerror("Error: Se espera un bloque de sentencias en la linea "+ lineNumber);}
break;
case 48:
//#line 273 "parser.y"
{yyerror("Error: Se esperaba la palabra reservada 'HASTA' en la linea "+ lineNumber);}
break;
case 49:
//#line 274 "parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en la linea "+ lineNumber);}
break;
case 50:
//#line 275 "parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en la linea "+ lineNumber);}
break;
case 51:
//#line 277 "parser.y"
{yyerror("Error: Se esperaba la palabra reservada 'Hasta' en la linea "+ lineNumber); }
break;
case 52:
//#line 278 "parser.y"
{yyerror("Error: Falta la condicion el la iteracion en la linea "+ lineNumber); }
break;
case 53:
//#line 282 "parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			yyval = new ParserVal( new NodoSinTipo("si", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj ) );
		} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
	}
break;
case 54:
//#line 289 "parser.y"
{yyerror("Error: Se detecto IF erroneo, falta la palabra reservada 'ENTONCES' en la linea "+ lineNumber); }
break;
case 55:
//#line 296 "parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal( new NodoSinTipo("cuerpo", (Arbol)val_peek(1).obj , (Arbol)val_peek(0).obj ) ); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 56:
//#line 303 "parser.y"
{
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal(new NodoUnario("cuerpo",(Arbol)(val_peek(0).obj))); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
					}
				}
break;
case 57:
//#line 314 "parser.y"
{ 
	if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
		yyval = new ParserVal(new NodoUnario("entonces",(Arbol)(val_peek(1).obj))); 
	} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
}
break;
case 58:
//#line 323 "parser.y"
{
						add("Declaracion if en linea " + lineNumber); 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal(new NodoUnario("entonces",(Arbol)(val_peek(0).obj))); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}

			}
break;
case 59:
//#line 334 "parser.y"
{ 
					add("Declaracion if else en linea " + lineNumber); 
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new NodoUnario("sino",(Arbol)(val_peek(0).obj)));
					} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 60:
//#line 344 "parser.y"
{
									if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
										yyval = new ParserVal(new NodoUnario("condicion",(Arbol)(val_peek(1).obj)));
									} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
								}
break;
case 61:
//#line 352 "parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en la linea "+ lineNumber);}
break;
case 62:
//#line 353 "parser.y"
{yyerror("Error: Se detecto IF erroneo, se esperaba '(' en la linea "+ lineNumber);}
break;
case 63:
//#line 354 "parser.y"
{yyerror("Error: Se detecto IF erroneo, se esperaba ')' en la linea "+ lineNumber);}
break;
case 64:
//#line 355 "parser.y"
{yyerror("Error: Se detecto IF erroneo, falta la condicion en la linea "+ lineNumber);}
break;
case 65:
//#line 359 "parser.y"
{
				
				if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
						if(! ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
				
							yyerror("Error: Difieren los tipos en la linea " + lineNumber);
						}
				}

				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( val_peek(1).sval, (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 66:
//#line 375 "parser.y"
{yyerror("Error: Se detecto una comparacion erronea, falta parte izquierda, en la linea " + lineNumber);}
break;
case 67:
//#line 376 "parser.y"
{yyerror("Error: Se detecto una comparacion erronea, falta parte derecha, en la linea " + lineNumber);}
break;
case 68:
//#line 377 "parser.y"
{yyerror("Error: Se detecto una comparacion erronea, no hay nada para comparar, en la linea " + lineNumber);}
break;
case 69:
//#line 381 "parser.y"
{
				
				if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
					if( !((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
						yyerror("Error: Diefieren los tipos de la suma en la linea " + lineNumber);
					} 
				}
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "+", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 70:
//#line 394 "parser.y"
{
				if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
					if( !((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
						yyerror("Error: Diefieren los tipos de la resta en la linea " + lineNumber);
					} 
				}
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "-", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				
			}
break;
case 71:
//#line 407 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		  }
break;
case 72:
//#line 417 "parser.y"
{
				if( ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal( new Nodo( "*", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
					} else {
						yyval = new ParserVal( new Hoja( "error", "syntax error" ));
					}
				} else {
					if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
						yyerror("Error: Diefieren los tipos de la multiplicacion en la linea " + lineNumber);
					}
				}
				
			}
break;
case 73:
//#line 431 "parser.y"
{
			if( ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "/", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			} else {
					if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
						yyerror("Error: Diefieren los tipos de la division en la linea " + lineNumber);
				}			}
		}
break;
case 74:
//#line 443 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
break;
case 75:
//#line 453 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
break;
case 76:
//#line 460 "parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			yyval = val_peek(0);
		} else {
			yyval = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}
break;
case 77:
//#line 471 "parser.y"
{

	
			if( !estaDeclarada(val_peek(0).sval) ) {
				yyerror("Error: La variable '" + val_peek(0).sval + "' no esta declarada en la linea " + lineNumber);
			}

			if (!("variable".equals(((AttributeVariableID)SymbolTable.getInstance().get(val_peek(0).sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo variable simple en la linea " + lineNumber);
			}

			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = new ParserVal(new Hoja(  val_peek(0).sval, ((AttributeVariableID)SymbolTable.getInstance().get(val_peek(0).sval)).getTypeOfElement() )); 
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
			
		}
break;
case 78:
//#line 490 "parser.y"
{

			if( !estaDeclarada(val_peek(0).sval) ) {
				yyerror("Error: La variable '" + val_peek(0).sval + "' no esta declarada en la linea " + lineNumber);
			}

			if (!("variable".equals(((AttributeVariableID)SymbolTable.getInstance().get(val_peek(0).sval)).getTypeOfId()) ) ){
				yyerror("Error: La variable no es de tipo variable simple en la linea " + lineNumber);
			}

			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = new ParserVal(new Hoja("-"+val_peek(0).sval, ((AttributeVariableID)SymbolTable.getInstance().get(val_peek(0).sval)).getTypeOfElement() )); 
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
			
		}
break;
case 79:
//#line 509 "parser.y"
{
	if( !"error".equals(((Arbol)val_peek(1).obj).getElem()) ){
		
		if (!((Arbol)val_peek(1).obj).getTipo().equals("entero")) {
			yyerror("Error: El tipo del indice del vector es incorrecto en la linea "+ lineNumber);
		}

		if( !estaDeclarada(val_peek(3).sval) ) {
				yyerror("Error: La variable no esta declarada en la linea " + lineNumber);
		}
		if (!("vector".equals(((AttributeVariableID)SymbolTable.getInstance().get(val_peek(3).sval)).getTypeOfId()) ) ){
			yyerror("Error: La variable no es de tipo vector e la linea " + lineNumber);
		}
	}
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				Arbol idv = new Hoja(val_peek(3).sval, ((AttributeVector)SymbolTable.getInstance().get(val_peek(3).sval)).getTypeOfElement() );
				yyval = new ParserVal(new Nodo(val_peek(3).sval , idv, (Arbol)val_peek(1).obj, idv.getTipo()  ));
		} else {
			yyval = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}
break;
case 80:
//#line 535 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 81:
//#line 542 "parser.y"
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
case 82:
//#line 555 "parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 83:
//#line 562 "parser.y"
{
	
			if (((Long)val_peek(0).obj) > 32768 ) {
				yyerror("Error: Numero negativo debajo del rango en la linea " + lineNumber); 
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
case 84:
//#line 581 "parser.y"
{ yyval = new ParserVal("=");}
break;
case 85:
//#line 582 "parser.y"
{ yyval = new ParserVal(">=");}
break;
case 86:
//#line 583 "parser.y"
{ yyval = new ParserVal("<=");}
break;
case 87:
//#line 584 "parser.y"
{ yyval = new ParserVal("<");}
break;
case 88:
//#line 585 "parser.y"
{ yyval = new ParserVal(">");}
break;
case 89:
//#line 586 "parser.y"
{ yyval = new ParserVal("^=");}
break;
//#line 1453 "Parser.java"
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
