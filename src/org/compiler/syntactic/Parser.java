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



//#line 2 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
    5,    5,    5,    6,    6,    6,    6,    2,    2,    2,
    2,    2,    2,    7,    7,    7,    7,    7,    7,   10,
   11,   11,   11,   11,   11,   11,   11,    8,    8,   15,
   15,   16,   18,   17,   14,   14,   14,   14,   14,   13,
   12,   12,   12,   20,   20,   20,    9,    9,    9,    9,
    9,   22,   23,   21,   21,   21,   21,   19,   19,   19,
   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    1,    2,    3,   10,    9,    9,
    9,    9,    9,    9,    9,    9,    2,    2,    1,    1,
    1,    3,    2,    2,    3,    3,    3,    2,    3,    1,
    2,    3,    2,    4,    1,    1,    4,    4,    4,    3,
    6,    5,    5,    5,    5,    6,    6,    3,    2,    2,
    1,    2,    1,    1,    4,    4,    3,    4,    4,    3,
    3,    3,    1,    3,    3,    1,    1,    1,    4,    3,
    2,    1,    4,    1,    1,    1,    2,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   19,   20,    0,    0,    0,    0,    0,
    5,    0,    0,   30,    0,   35,   36,    0,   67,   68,
    0,   33,    0,   75,    0,    0,    0,    0,    0,    0,
   66,   74,   76,    0,    0,    0,    0,    0,    0,    0,
    0,   17,    0,   71,    0,    6,    0,   31,    0,   21,
   18,    0,   28,    0,    0,    0,   49,    0,   51,    0,
    0,    0,    0,    0,   77,   79,   80,   78,    0,    0,
   81,   82,   83,    0,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   24,    0,
    0,    0,   29,   32,   23,    7,    0,    0,   48,   52,
   54,   50,    0,   56,    0,   59,   58,   55,    0,    0,
    0,   64,   65,   37,   39,   38,   34,   26,    0,    0,
   27,   25,    0,    0,    0,    0,   69,    0,    0,    0,
   73,   22,    0,   42,    0,    0,    0,   44,   43,    0,
    0,    0,    0,    0,   46,   47,   41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   16,    9,   10,
   11,   12,   13,   14,   15,    0,    8,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   52,   56,   13,   14,   15,   16,
   17,   28,   29,   18,   57,   58,  102,   59,   74,   30,
   31,   32,   33,
};
final static short yysindex[] = {                        34,
 -148,  -67, -242,    0,    0,  -26, -243,    0,   34,   48,
    0, -102, -241,    0, -199,    0,    0,  -28,    0,    0,
 -190,    0, -196,    0, -179,  -63, -188,    4, -164, -103,
    0,    0,    0, -156, -180,   41, -150,  -91,   41, -234,
 -143,    0,  -58,    0,   48,    0, -139,    0, -225,    0,
    0, -230,    0, -196,   -3, -101,    0,   -3,    0, -121,
  -90, -196,  -81, -166,    0,    0,    0,    0, -196, -196,
    0,    0,    0, -196,    0, -196, -196,  -73,  -59, -222,
 -113,  -16, -196,  -53,  -33,  -68, -135, -196,    0, -185,
 -252, -183,    0,    0,    0,    0,  -50,  -69,    0,    0,
    0,    0,  -46,    0, -183,    0,    0,    0, -103, -103,
  -69,    0,    0,    0,    0,    0,    0,    0,  -36,  -54,
    0,    0, -196,  -48,  -30,  -24,    0,  -20,  -18, -174,
    0,    0,  -15,    0,   -7,    7,    8,    0,    0,    2,
   20,   22, -249,   12,    0,    0,    0,   23,   42,   44,
   53,  -92,   21,   54,   55,   56,   57,   58, -131,   -6,
   -6,   -6,   -6,   -6,   -6,   -6,    6,    0,    0,    0,
    0,    0,    0,    0,    0,  -14,    0,
};
final static short yyrindex[] = {                       318,
    0,    0,    0,    0,    0,    0,   52,    0,  325,  326,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -165,    0,    0,    0,    0, -144,
    0,    0,    0,    0,    0,    0,    0, -197,    0,    0,
    0,    0,    0,    0,  327,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   32, -186,    0,    0,    0,    0,    0,   43,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -110,  -89,
 -248,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   45,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   15,  319,  -12,    0,   11,    5,   -4,   -2,    0,
    0,  -10,  -21,    0,  275,    0,    0,    0,    0,  201,
  203,    3,   10,
};
final static int YYTABLESIZE=331;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
   53,   61,   19,  129,   64,   48,  151,   49,   19,   20,
   41,   19,   19,   34,   47,   20,   40,   60,   20,   20,
   19,   86,   41,   45,   42,   95,   60,   20,   87,  152,
   94,   35,   92,  116,   43,   44,  130,   60,   19,   88,
   48,   19,   49,   98,   53,   20,   54,   19,   20,   47,
   82,  105,  117,   85,   20,   96,   97,   19,   72,   41,
   19,  119,   41,  111,   20,  125,  126,   20,  101,   70,
   24,   25,   54,   92,   72,   79,   60,   48,   65,   49,
   48,  142,   49,   27,   19,   70,   47,   19,   80,   47,
   72,   20,  143,  127,   20,  131,   69,   70,   62,  107,
   72,  135,  137,  128,   72,   72,   72,   72,  108,   72,
   75,   63,   78,   72,   72,   72,   72,   72,   72,   72,
   72,   63,   72,   83,  166,   63,   63,   63,   63,   21,
   63,   24,   25,  167,   63,   63,   63,   22,  124,   63,
   63,   63,   89,   63,   27,   62,   93,  168,  169,  170,
  171,  172,  173,  174,  176,   62,  100,    4,    5,   62,
   62,   62,   62,  158,   62,   50,   61,  103,   62,   62,
   62,  159,   22,   62,   62,   62,   61,   62,   76,   77,
   61,   61,   61,   61,  104,   61,   84,   44,   23,   61,
   61,   61,   63,  106,   61,   61,   61,   90,   61,   24,
   25,  114,  120,   24,   25,  123,   26,  136,   91,   25,
   69,   70,   27,   24,   25,  115,   27,  132,   24,   25,
  133,   27,  121,    2,  127,    3,   27,   36,    6,   36,
    3,   27,    3,    6,   38,    6,   37,   55,  134,   38,
    2,   38,    3,  122,  138,    6,  140,   39,  141,   39,
  139,   38,   36,    4,    5,    3,   53,   53,    6,   53,
  118,  175,   53,  144,   38,    4,    5,  145,   53,  109,
  110,  177,   39,   66,   67,  153,   68,   53,  112,  113,
  148,  146,  147,   69,   70,  160,  154,   71,   72,    1,
    2,   73,    3,    4,    5,    6,   81,    2,  149,    3,
  150,    7,    6,   75,    2,  155,    3,  156,   38,    6,
   75,   75,   75,   75,   75,   38,  157,    1,  161,  162,
  163,  164,  165,   72,    2,    3,    4,   46,   40,   99,
   45,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         12,
    0,   23,    0,  256,   26,   10,  256,   10,    6,    0,
    6,    9,   10,  256,   10,    6,    6,  266,    9,   10,
   18,  256,   18,    9,  268,  256,  275,   18,  263,  279,
  256,  274,   43,  256,  278,  279,  289,  286,   36,  274,
   45,   39,   45,   54,  286,   36,  272,   45,   39,   45,
   36,   62,  275,   39,   45,  286,  287,   55,  256,   55,
   58,   83,   58,   74,   55,   87,   88,   58,   58,  256,
  267,  268,  272,   84,  272,  256,  267,   82,  267,   82,
   85,  256,   85,  280,   82,  272,   82,   85,  269,   85,
  256,   82,  267,  279,   85,  279,  280,  281,  278,  266,
  266,  123,  124,  289,  270,  271,  272,  273,  275,  275,
  275,  256,  269,  279,  280,  281,  282,  283,  284,  285,
  286,  266,  288,  274,  256,  270,  271,  272,  273,  278,
  275,  267,  268,  265,  279,  280,  281,  286,  274,  284,
  285,  286,  286,  288,  280,  256,  286,  160,  161,  162,
  163,  164,  165,  166,  167,  266,  258,  260,  261,  270,
  271,  272,  273,  256,  275,  268,  256,  289,  279,  280,
  281,  264,  286,  284,  285,  286,  266,  288,  282,  283,
  270,  271,  272,  273,  275,  275,  278,  279,  256,  279,
  280,  281,  256,  275,  284,  285,  286,  256,  288,  267,
  268,  275,  256,  267,  268,  274,  274,  256,  267,  268,
  280,  281,  280,  267,  268,  275,  280,  268,  267,  268,
  267,  280,  256,  257,  279,  259,  280,  256,  262,  256,
  259,  280,  259,  262,  268,  262,  263,  266,  275,  268,
  257,  268,  259,  277,  275,  262,  267,  276,  267,  276,
  275,  268,  256,  260,  261,  259,  256,  257,  262,  259,
  277,  256,  262,  279,  268,  260,  261,  275,  268,   69,
   70,  286,  276,  270,  271,  264,  273,  277,   76,   77,
  279,  275,  275,  280,  281,  265,  264,  284,  285,  256,
  257,  288,  259,  260,  261,  262,  256,  257,  279,  259,
  279,  268,  262,  272,  257,  264,  259,  264,  268,  262,
  279,  280,  281,  282,  283,  268,  264,    0,  265,  265,
  265,  265,  265,  272,    0,    0,    0,    9,  286,   55,
  286,
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
"variables : variables error",
"bloque_sentencias : sentencia PUNTOCOMA",
"bloque_sentencias : ABRELLAV sentencias_ejecutables CIERRALLAV",
"bloque_sentencias : error sentencias_ejecutables CIERRALLAV",
"bloque_sentencias : ABRELLAV sentencias_ejecutables error",
"sentencias_ejecutables : sentencia PUNTOCOMA",
"sentencias_ejecutables : sentencias_ejecutables sentencia PUNTOCOMA",
"sentencias_ejecutables : seleccion",
"sentencias_ejecutables : sentencias_ejecutables seleccion",
"sentencias_ejecutables : sentencias_ejecutables variable error",
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
"iteracion : DO bloque_sentencias error ABREPAR condicion CIERRAPAR",
"iteracion : DO bloque_sentencias UNTIL ABREPAR error CIERRAPAR",
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
"cabecera_seleccion : IF ABREPAR condicion THEN",
"cabecera_seleccion : IF ABREPAR error CIERRAPAR",
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

//#line 514 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"

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
//#line 552 "Parser.java"
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
//#line 21 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
		tree = (Arbol)val_peek(0).obj;
	}
break;
case 4:
//#line 24 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
		tree = (Arbol)val_peek(0).obj;
	}
break;
case 7:
//#line 34 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
//#line 50 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
								

								if ( !estaDeclarada(val_peek(9).sval) ) {
									declaradas.add(val_peek(9).sval);
								}
								else {
									yyerror("Error: La variable '" + ((Arbol)val_peek(9).obj).getElem() + "' ya esta declarada en la linea: " + lineNumber);
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
case 9:
//#line 82 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba una constante en la linea " + lineNumber);}
break;
case 10:
//#line 83 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba '..' en la linea " + lineNumber);}
break;
case 11:
//#line 84 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba una constante en la linea " + lineNumber);}
break;
case 12:
//#line 85 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: se esperaba un ']' eb linea "+ lineNumber);}
break;
case 13:
//#line 86 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta la palabra reservada 'VECTOR' en la linea " + lineNumber);}
break;
case 14:
//#line 87 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta la palabra reservada 'DE' en la linea " + lineNumber);}
break;
case 15:
//#line 88 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta tipo del vector en la linea " + lineNumber);}
break;
case 16:
//#line 89 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Nombre variable en la linea " + lineNumber);}
break;
case 17:
//#line 90 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Palabra reservada mal escrita en la linea " + lineNumber);}
break;
case 18:
//#line 91 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Nombre de variable igual al tipo en la linea " + lineNumber);}
break;
case 19:
//#line 94 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
			yyval = new ParserVal("entero");  
	}
break;
case 20:
//#line 97 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
			yyval = new ParserVal("entero_ss"); 
	}
break;
case 21:
//#line 102 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
				List<String> vars = new LinkedList<String>(); 
				vars.add( val_peek(0).sval );
				yyval = new ParserVal(vars); 


		}
break;
case 22:
//#line 109 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ List<String> vars = new LinkedList<String>(); 
								vars.add( val_peek(0).sval );
								vars.addAll( (LinkedList<String>)val_peek(2).obj );
								yyval = new ParserVal(vars);
		}
break;
case 23:
//#line 115 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta la coma en la declaracion en la linea: " + lineNumber);}
break;
case 24:
//#line 118 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = val_peek(1); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 25:
//#line 125 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(1); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 26:
//#line 133 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba '{' en la linea " + lineNumber);}
break;
case 27:
//#line 134 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba '}' en la linea " + lineNumber);}
break;
case 28:
//#line 140 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(1); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 29:
//#line 147 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal(new NodoSinTipo("sentencia",(Arbol)(val_peek(2).obj),(Arbol)(val_peek(1).obj))); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 30:
//#line 154 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = val_peek(0); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 31:
//#line 161 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal(new NodoSinTipo("sentencia",(Arbol)(val_peek(1).obj),(Arbol)(val_peek(0).obj))); 
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 32:
//#line 168 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto sentencia erronea, falta ';' en la linea " + lineNumber);}
break;
case 33:
//#line 169 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Codigo erroneo en la linea " + lineNumber);}
break;
case 34:
//#line 173 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
				add("Declaracion imprimir en la linea  "+lineNumber+" cadena "+ val_peek(1).sval); 
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new Hoja("imprimir", val_peek(1).sval));
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 35:
//#line 181 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 36:
//#line 188 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			}  else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 37:
//#line 195 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto un PRINT erroneo, se esperaba un '(' en la linea " + lineNumber);}
break;
case 38:
//#line 196 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto un PRINT erroneo, se esperaba un ')' en la linea " + lineNumber);}
break;
case 39:
//#line 197 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto un PRINT erroneo, se esperaba una 'cadena' en la linea " + lineNumber);}
break;
case 40:
//#line 200 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
							}
							if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
								yyval = new ParserVal( new Nodo(":=", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
							} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
						}
break;
case 41:
//#line 221 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
					add("Iterar en linea  "+lineNumber);
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new NodoSinTipo("iterar", new NodoUnario("condicion",(Arbol)(val_peek(1).obj)), (Arbol)(val_peek(4).obj)));
					} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 42:
//#line 229 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un bloque de sentencias en la linea "+ lineNumber);}
break;
case 43:
//#line 230 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba la palabra reservada 'Hasta' en la linea "+ lineNumber);}
break;
case 44:
//#line 231 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis abierto' en la linea "+ lineNumber);}
break;
case 45:
//#line 232 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera un 'Parentesis cerrado' en la linea "+ lineNumber);}
break;
case 46:
//#line 234 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se esperaba la palabra reservada 'Hasta' en la linea "+ lineNumber); }
break;
case 47:
//#line 235 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Falta la condicion el la iteracion en la linea "+ lineNumber); }
break;
case 48:
//#line 239 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			yyval = new ParserVal( new NodoSinTipo("si", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj ) );
		} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
	}
break;
case 49:
//#line 246 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: falta entonces"+ lineNumber); }
break;
case 50:
//#line 250 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal( new NodoSinTipo("cuerpo", (Arbol)val_peek(1).obj , (Arbol)val_peek(0).obj ) ); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
					}
break;
case 51:
//#line 257 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal(new NodoUnario("cuerpo",(Arbol)(val_peek(0).obj))); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 52:
//#line 266 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
	if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
		yyval = new ParserVal(new NodoUnario("entonces",(Arbol)(val_peek(1).obj))); 
	} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
}
break;
case 53:
//#line 275 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
						add("Declaracion if en linea " + lineNumber); 
						if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
							yyval = new ParserVal(new NodoUnario("entonces",(Arbol)(val_peek(0).obj))); 
						} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}

			}
break;
case 54:
//#line 286 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ 
					add("Declaracion if else en linea " + lineNumber); 
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal(new NodoUnario("sino",(Arbol)(val_peek(0).obj)));
					} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				}
break;
case 55:
//#line 296 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
									if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
										yyval = new ParserVal(new NodoUnario("condicion",(Arbol)(val_peek(1).obj)));
									} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
								}
break;
case 56:
//#line 304 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo despues del token if en la linea "+ lineNumber);}
break;
case 57:
//#line 305 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo, se esperaba '(' en la linea "+ lineNumber);}
break;
case 58:
//#line 306 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo, se esperaba ')' en la linea "+ lineNumber);}
break;
case 59:
//#line 307 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se detecto IF erroneo, falta la condicion en la linea "+ lineNumber);}
break;
case 60:
//#line 311 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
case 61:
//#line 328 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
				
				if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
					if( !((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
						yyerror("Error: Diefieren los tipos en la linea " + lineNumber);
					} 
				}
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "+", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			}
break;
case 62:
//#line 341 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
				if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
					if( !((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
						yyerror("Error: Diefieren los tipos en la linea " + lineNumber);
					} 
				}
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "-", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
				
			}
break;
case 63:
//#line 354 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		  }
break;
case 64:
//#line 363 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
				if( ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
					if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
						yyval = new ParserVal( new Nodo( "*", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
					} else {
						yyval = new ParserVal( new Hoja( "error", "syntax error" ));
					}
				} else {
					if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
						yyerror("Error: Diefieren los tipos en la linea " + lineNumber);
					}
				}
				
			}
break;
case 65:
//#line 377 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
			if( ((Arbol)val_peek(2).obj).getTipo().equals( ((Arbol)val_peek(0).obj).getTipo() ) ) {
				if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
					yyval = new ParserVal( new Nodo( "/", (Arbol)val_peek(2).obj , (Arbol)val_peek(0).obj , ((Arbol)val_peek(0).obj).getTipo() ) );
				} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
			} else {
					if(  !"error".equals(((Arbol)val_peek(2).obj).getElem()) &&  !"error".equals(((Arbol)val_peek(0).obj).getElem()) ){
						yyerror("Error: Diefieren los tipos en la linea " + lineNumber);
				}			}
		}
break;
case 66:
//#line 389 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
break;
case 67:
//#line 398 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
				yyval = new ParserVal( new Hoja( "error", "syntax error" ));
			}
		}
break;
case 68:
//#line 405 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
		if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
			yyval = val_peek(0);
		} else {
			yyval = new ParserVal( new Hoja( "error", "syntax error" ));
		}
	}
break;
case 69:
//#line 412 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera una expresion entre los corchetes en la linea "+ lineNumber);}
break;
case 70:
//#line 413 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Se espera que se cierre corchetes en la linea "+ lineNumber);}
break;
case 71:
//#line 414 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{yyerror("Error: Cierre de corchetes inesperado en la linea "+ lineNumber);}
break;
case 72:
//#line 419 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{

	
			if( !estaDeclarada(val_peek(0).sval) ) {
				yyerror("Error: La variable '" + val_peek(0).sval + "' no esta declarada la variable en la linea " + lineNumber);
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
case 73:
//#line 439 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
case 74:
//#line 462 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0); 
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 75:
//#line 469 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
case 76:
//#line 479 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{
			if( LexicalAnalyzer.errors.isEmpty() && errors.isEmpty() ) {
				yyval = val_peek(0);
			} else {
					yyval = new ParserVal( new Hoja( "error", "syntax error" ));
				}
		}
break;
case 77:
//#line 486 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
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
case 78:
//#line 504 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ yyval = new ParserVal("=");}
break;
case 79:
//#line 505 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ yyval = new ParserVal(">=");}
break;
case 80:
//#line 506 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ yyval = new ParserVal("<=");}
break;
case 81:
//#line 507 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ yyval = new ParserVal("<");}
break;
case 82:
//#line 508 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ yyval = new ParserVal(">");}
break;
case 83:
//#line 509 "C:\Users\Vaio\workspace\Compilador\src\org\compiler\syntactic\parser.y"
{ yyval = new ParserVal("^=");}
break;
//#line 1365 "Parser.java"
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
