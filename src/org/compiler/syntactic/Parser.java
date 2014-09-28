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
    0,    0,    0,    0,    1,    1,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    4,    4,    5,    5,    6,    6,    2,    2,    7,    7,
    7,    7,    7,    7,    7,    7,    9,   10,   10,   10,
   10,   10,    8,   15,   15,   16,   18,   17,   14,   14,
   14,   13,   12,   12,   12,   20,   20,   20,   11,   11,
   11,   11,   11,   21,   21,   21,   21,   19,   19,   19,
   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    1,    2,    3,   10,    2,    9,
   10,   10,   10,   10,   10,   10,   10,   10,   10,    1,
    1,    1,    1,    3,    1,    3,    1,    2,    5,    1,
    2,    2,    1,    1,    4,    4,    3,    6,    5,    5,
    5,    5,    3,    2,    1,    2,    1,    1,    4,    4,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    4,
    3,    3,    2,    1,    1,    4,    2,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   21,   22,    0,    0,    0,    0,    0,
    5,    0,   27,   30,    0,    0,    0,    0,    0,    0,
   65,    0,    0,    0,    0,    0,    0,   58,    0,    0,
    0,    0,    0,    0,   25,    0,    0,   63,    0,    6,
   28,   23,    0,   31,   32,    0,    0,    0,    0,    0,
    0,   67,   69,   70,   68,    0,    0,   71,   72,   73,
    0,   51,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   61,    0,    7,    0,    0,    0,
   43,    0,   45,    0,   50,    0,   49,    0,    0,    0,
   56,   57,   36,    0,    0,   26,    0,    0,    0,    0,
    0,    0,    0,   60,   24,   46,   48,   44,    0,   66,
   29,   39,    0,   41,   40,    0,    0,    0,    0,    0,
    0,   38,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   19,   11,   12,   13,   14,
   15,   16,   17,   18,    8,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   43,   34,   13,   14,   15,   16,
   17,   25,   26,   18,   81,   82,  108,   83,   61,   27,
   28,
};
final static short yysindex[] = {                      -158,
 -265, -250, -174,    0,    0, -193, -237,    0, -158, -151,
    0, -239,    0,    0, -263, -240, -219, -179, -176, -253,
    0, -218, -253, -160, -192, -166, -257,    0, -147, -139,
 -138, -194, -151, -235,    0, -118, -224,    0, -151,    0,
    0,    0, -145,    0,    0, -253, -186, -136, -121, -253,
 -120,    0,    0,    0,    0, -253, -253,    0,    0,    0,
 -253,    0, -253, -253, -130, -117, -253, -258, -205, -206,
 -253, -132, -128, -254,    0, -146,    0, -109, -137,  -98,
    0, -186,    0, -105,    0, -141,    0, -257, -257, -137,
    0,    0,    0, -122, -110,    0, -253, -108, -107, -101,
  -97,  -95, -208,    0,    0,    0,    0,    0, -106,    0,
    0,    0, -100,    0,    0, -103, -102,  -99,  -96, -245,
  -90,    0,  -86,  -85,  -83,  -82,  -79, -170,  -78,  -77,
  -76,  -75,  -74,  -72,  -71, -207, -115, -115, -115, -115,
 -115, -115, -115, -115, -129,  -91,  -89,  -88,  -87,  -84,
  -81,  -80,  -73,  -70,  -69,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       169,
  211,    0,    0,    0,    0,    0,  -68,    0,  186,  196,
    0,    0,    0,    0,  163,  171,    0,    0,    0,    0,
    0,    1,    0,    0,    0,    0,   33,    0,    0,    0,
    0,  -68,    0,    0,    0,    0,    0,    0,  200,    0,
    0,    0,  221,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -167,    0,  -65,    0,    0,  137,  192,
    0,    0,    0,    0,    0,    0,    0,   65,   97,  129,
    0,    0,    0,  184,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  150,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  231,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    3,  194,  -19,    0,  -42,   -2,    0,    0,    0,
    0,  -30,  -20,    0,    0,    0,    0,    0,    0,   91,
   88,
};
final static int YYTABLESIZE=499;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   64,  102,   51,   35,   80,   20,   76,   41,   21,   22,
  127,   39,   19,   21,   22,   79,   21,   22,   36,   86,
   75,   24,   44,   23,   63,   64,   24,   70,   42,   24,
   90,   73,   55,  128,  103,   69,   41,   76,   71,  107,
   37,   38,   74,   22,   35,   45,   95,  119,  144,   98,
   99,    2,   46,    3,   75,   24,    6,  145,  120,   50,
   21,   22,   32,    2,   54,    3,   41,   97,    6,   31,
    2,   96,    3,   24,   32,    6,  113,   53,   54,   35,
   55,   32,   33,   68,   38,  135,   47,   56,   57,   33,
   48,   58,   59,  136,   29,   60,   53,    1,    2,   30,
    3,    4,    5,    6,   65,    2,   52,    3,   62,    7,
    6,   65,   65,   65,   65,   65,   32,  146,  147,  148,
  149,  150,  151,  152,  153,  155,  154,   65,   52,   66,
    4,    5,  104,   56,   57,   67,   37,  110,   56,   57,
   77,   78,   56,   57,    4,    5,   88,   89,   72,   42,
   91,   92,   84,   85,   87,   93,  100,   94,  105,  106,
  101,  109,   33,  111,  112,  116,  114,  115,    1,  117,
   34,  118,  121,  129,  122,  123,  124,  130,  131,  125,
  132,  133,  126,   35,  134,    2,  137,  138,  139,  140,
  141,   47,  142,  143,  156,    3,  157,  158,  159,    4,
    0,  160,   40,   59,  161,  162,   62,    0,    0,    0,
   20,    0,  163,    0,    0,  164,  165,    0,    0,    0,
    9,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   10,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   64,   64,   64,
    0,    0,   64,   64,    0,    0,    0,    0,   64,    0,
   64,   64,   64,   64,   64,   64,    0,   64,    0,   64,
   64,   64,   64,   64,   64,   64,   64,    0,   64,   55,
   55,   55,    0,    0,   55,   55,    0,    0,    0,    0,
   55,    0,   55,   55,   55,   55,   55,   55,    0,   55,
    0,   55,   55,   55,    0,    0,   55,   55,   55,    0,
   55,   54,   54,   54,    0,    0,   54,   54,    0,    0,
    0,    0,   54,    0,   54,   54,   54,   54,   54,   54,
    0,   54,    0,   54,   54,   54,    0,    0,   54,   54,
   54,    0,   54,   53,   53,   53,    0,    0,   53,   53,
    0,    0,    0,    0,   53,    0,   53,   53,   53,   53,
   53,   53,    0,   53,    0,   53,   53,   53,    0,    0,
   53,   53,   53,    0,   53,   52,   52,   52,    0,    0,
   52,   52,    0,   37,   37,   37,   52,    0,   37,   37,
    0,    0,   52,   52,   37,   52,   42,   42,   42,    0,
   37,   42,   42,   37,   52,    0,    0,   42,    0,   33,
   33,   33,   37,   42,   33,   33,   42,   34,   34,   34,
   33,    0,   34,   34,    0,   42,   33,    0,   34,   33,
   35,   35,   35,    0,   34,   35,   35,   34,   47,    0,
   47,   35,    0,   47,   47,    0,    0,   35,    0,   47,
   35,    0,    0,    0,    0,   47,   20,   20,   47,   20,
   20,   20,   20,    0,    0,    0,    9,    9,   20,    9,
    9,    9,    9,    0,    0,    0,   10,   10,    9,   10,
   10,   10,   10,    0,    0,    0,    0,    0,   10,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         20,
    0,  256,   23,    6,   47,  256,   37,   10,  267,  268,
  256,    9,  278,  267,  268,   46,  267,  268,  256,   50,
  279,  280,  286,  274,  282,  283,  280,  263,  268,  280,
   61,  256,    0,  279,  289,   33,   39,   68,  274,   82,
  278,  279,  267,  268,   47,  286,   67,  256,  256,   70,
   71,  257,  272,  259,  279,  280,  262,  265,  267,  278,
  267,  268,  268,  257,    0,  259,   69,  274,  262,  263,
  257,  277,  259,  280,  268,  262,   97,  270,  271,   82,
  273,  268,  276,  278,  279,  256,  266,  280,  281,  276,
  267,  284,  285,  264,  269,  288,    0,  256,  257,  274,
  259,  260,  261,  262,  272,  257,  267,  259,  275,  268,
  262,  279,  280,  281,  282,  283,  268,  137,  138,  139,
  140,  141,  142,  143,  144,  145,  256,  275,    0,  269,
  260,  261,  279,  280,  281,  274,    0,  279,  280,  281,
  286,  287,  280,  281,  260,  261,   56,   57,  267,    0,
   63,   64,  289,  275,  275,  286,  289,  275,  268,  258,
  289,  267,    0,  286,  275,  267,  275,  275,    0,  267,
    0,  267,  279,  264,  275,  279,  279,  264,  264,  279,
  264,  264,  279,    0,  264,    0,  265,  265,  265,  265,
  265,    0,  265,  265,  286,    0,  286,  286,  286,    0,
   -1,  286,    9,  272,  286,  286,  272,   -1,   -1,   -1,
    0,   -1,  286,   -1,   -1,  286,  286,   -1,   -1,   -1,
    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
  274,  262,  263,  277,  286,   -1,   -1,  268,   -1,  257,
  258,  259,  286,  274,  262,  263,  277,  257,  258,  259,
  268,   -1,  262,  263,   -1,  286,  274,   -1,  268,  277,
  257,  258,  259,   -1,  274,  262,  263,  277,  257,   -1,
  259,  268,   -1,  262,  263,   -1,   -1,  274,   -1,  268,
  277,   -1,   -1,   -1,   -1,  274,  256,  257,  277,  259,
  260,  261,  262,   -1,   -1,   -1,  256,  257,  268,  259,
  260,  261,  262,   -1,   -1,   -1,  256,  257,  268,  259,
  260,  261,  262,   -1,   -1,   -1,   -1,   -1,  268,
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
"sentencias_declarativas_simples : tipo variables",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo",
"sentencias_declarativas_simples : ID error CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR error DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE error CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO error CIERRACOR VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE error VECTOR OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR error OF tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR error tipo PUNTOCOMA",
"sentencias_declarativas_simples : ID ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF error PUNTOCOMA",
"sentencias_declarativas_simples : error ABRECOR CTE DOSPUNTO CTE CIERRACOR VECTOR OF tipo PUNTOCOMA",
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

//#line 145 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"

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
//#line 526 "Parser.java"
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
{ detections.add("Declaracion de variable comun en linea " + 										lineNumber); }
break;
case 8:
//#line 30 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable vector en linea " + lineNumber); }
break;
case 9:
//#line 31 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la declaracion en linea " + lineNumber);}
break;
case 10:
//#line 32 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la declaracion del vector en linea " + lineNumber);}
break;
case 11:
//#line 33 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: se esperaba un '[' eb linea "+ lineNumber);}
break;
case 12:
//#line 34 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
break;
case 13:
//#line 35 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba una '..' en linea " + lineNumber);}
break;
case 14:
//#line 36 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba una constante en linea " + lineNumber);}
break;
case 15:
//#line 37 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
break;
case 16:
//#line 38 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta la palabra reservada 'VECTOR' en linea " + lineNumber);}
break;
case 17:
//#line 39 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta la palabra reservada 'DE' en linea " + lineNumber);}
break;
case 18:
//#line 40 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta tipo del vector en linea " + lineNumber);}
break;
case 19:
//#line 41 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Nombre variable en linea " + lineNumber);}
break;
case 20:
//#line 42 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Variable mal declarada en linea " + lineNumber);}
break;
case 29:
//#line 62 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion imprimir en linea  "+lineNumber+" cadena "+ val_peek(2).sval); }
break;
case 33:
//#line 66 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la asignacion en linea " + lineNumber);}
break;
case 34:
//#line 67 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en la iteracion en linea " + lineNumber);}
break;
case 35:
//#line 68 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta ';' en imprimir en linea "+ lineNumber);}
break;
case 36:
//#line 69 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 37:
//#line 72 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Asignacion en linea  "+lineNumber); }
break;
case 38:
//#line 75 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Iterar en linea  "+lineNumber); }
break;
case 39:
//#line 76 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un bloque de sentencias en linea "+ lineNumber);}
break;
case 40:
//#line 77 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Hasta' en linea "+ lineNumber);}
break;
case 41:
//#line 78 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en linea "+ lineNumber);}
break;
case 42:
//#line 79 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en linea "+ lineNumber);}
break;
case 47:
//#line 93 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if en linea "+lineNumber); }
break;
case 48:
//#line 96 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if else en linea "+lineNumber); }
break;
case 50:
//#line 100 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en linea "+ lineNumber);}
break;
case 51:
//#line 101 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo falta parentesis "+ lineNumber);}
break;
case 61:
//#line 119 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera una exprecion entre los corchetes en linea "+ lineNumber);}
break;
case 62:
//#line 120 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera que se cierre corchetes en linea "+ lineNumber);}
break;
case 63:
//#line 121 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Cierre de corchetes inesperado en linea "+ lineNumber);}
break;
case 67:
//#line 127 "c:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{
   	   		if (val_peek(0).ival > 32768 ) {
   	   			yyerror("Numero negativo debajo del rango en linea "+lineNumber); 
   	   		}
   	   		/* Aca hay que agregar a la tabla de simbolos el - y sacar el otro, pero hay que usar semantico */
   	   	}
break;
//#line 812 "Parser.java"
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
