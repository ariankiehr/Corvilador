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



//#line 2 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
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
    0,    0,    0,    0,    1,    1,    3,    3,    4,    4,
    5,    5,    6,    6,    2,    2,    7,    7,    7,    7,
    9,   10,    8,   15,   15,   16,   18,   17,   14,   13,
   12,   12,   12,   20,   20,   20,   11,   11,   21,   21,
   21,   21,   19,   19,   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    1,    1,    2,    2,    3,    2,    9,    1,    1,
    1,    3,    1,    3,    1,    2,    2,    1,    2,    2,
    3,    4,    3,    2,    1,    2,    1,    1,    4,    3,
    3,    3,    1,    3,    3,    1,    1,    4,    1,    1,
    4,    2,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    9,   10,    0,    0,    0,    0,    0,    0,
    0,   15,   18,    0,    0,    0,    0,    0,   17,    0,
    0,    0,   13,    0,    0,    0,   16,    5,   11,    0,
   19,   20,    0,    0,   40,    0,    0,    0,    0,    0,
   36,    0,    0,    0,    0,    0,    6,    0,    0,    0,
   23,    0,   25,    0,   42,   44,   45,   43,    0,    0,
   46,   47,    0,   29,    0,    0,   14,   22,    0,   38,
   12,   26,   28,   24,    0,    0,    0,    0,   34,   35,
    0,   41,    0,    0,    0,    8,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   30,   22,   12,   13,   14,   15,
   16,   38,   39,   17,   51,   52,   74,   53,   63,   40,
   41,
};
final static short yysindex[] = {                      -147,
 -266, -275,    0,    0, -240, -260,    0, -147, -198, -263,
 -238,    0,    0, -249, -247, -226, -232, -236,    0, -215,
 -198, -187,    0, -254, -198, -221,    0,    0,    0, -239,
    0,    0, -236, -240,    0, -209, -189, -162, -180, -166,
    0, -236, -252, -236, -169, -199,    0, -175, -194, -130,
    0, -240,    0, -236,    0,    0,    0,    0, -236, -236,
    0,    0, -236,    0, -236, -236,    0,    0, -131,    0,
    0,    0,    0,    0, -182, -166, -166, -194,    0,    0,
 -149,    0, -133, -132, -157,    0,
};
final static short yyrindex[] = {                       132,
    0,    0,    0,    0,    0, -138,    0,  135,  136,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -138,
    0,    0,    0,    0,  137,    0,    0,    0,    0, -148,
    0,    0,    0,    0,    0, -230,    0,    0,    0, -213,
    0,    0,    0,    0,    0,    0,    0,    0, -146,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -196, -179, -251,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   -6,  131,   56,    0,  -25,   -5,    0,    0,    0,
    0,  -21,   98,    0,    0,    0,    0,    0,    0,   65,
   61,
};
final static int YYTABLESIZE=278;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         23,
   27,   25,   46,   27,    1,   45,    2,   18,   50,    5,
   19,   49,   35,   36,   43,   20,    1,   24,    2,   27,
   46,    5,   28,   30,   67,   37,   73,   20,   23,   29,
   35,   36,   75,   34,   30,   21,   31,   27,   32,   39,
   39,   78,   39,   37,   39,   33,   23,   48,   39,   39,
   39,   39,   39,   39,   39,   39,   33,   33,    1,   33,
    2,   33,   42,    5,   47,   33,   33,   33,   54,   20,
   33,   33,   33,   32,   32,   44,   32,   55,   32,   70,
   59,   60,   32,   32,   32,   59,   60,   32,   32,   32,
   31,   31,   71,   31,   64,   31,   82,   59,   60,   31,
   31,   31,    3,    4,   31,   31,   31,   56,   57,    1,
   58,    2,    3,    4,    5,   65,   66,   59,   60,   69,
    6,   61,   62,   76,   77,   79,   80,   72,   81,   83,
   84,    1,   85,   37,    2,    3,    4,    7,   26,   21,
   86,   68,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,    0,    0,    0,    0,    0,    0,   27,    0,   27,
    0,    0,   27,   27,    0,    0,    0,    0,   27,    0,
    0,    0,    0,    0,    0,    0,    0,   27,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,    8,   24,    9,  257,  260,  259,  274,   34,  262,
  286,   33,  267,  268,   21,  268,  257,  278,  259,   25,
   42,  262,  286,  275,  277,  280,   52,  268,   34,  268,
  267,  268,   54,  266,  286,  276,  286,   43,  286,  270,
  271,   63,  273,  280,  275,  272,   52,  287,  279,  280,
  281,  282,  283,  284,  285,  286,  270,  271,  257,  273,
  259,  275,  278,  262,  286,  279,  280,  281,  278,  268,
  284,  285,  286,  270,  271,  263,  273,  267,  275,  279,
  280,  281,  279,  280,  281,  280,  281,  284,  285,  286,
  270,  271,  268,  273,  275,  275,  279,  280,  281,  279,
  280,  281,  260,  261,  284,  285,  286,  270,  271,  257,
  273,  259,  260,  261,  262,  282,  283,  280,  281,  289,
  268,  284,  285,   59,   60,   65,   66,  258,  260,  279,
  264,    0,  265,  272,    0,    0,    0,  286,    8,  286,
   85,   44,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,  259,
   -1,   -1,  262,  263,   -1,   -1,   -1,   -1,  268,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  277,
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
"sentencias_declarativas_simples : tipo variables",
"sentencias_declarativas_simples : ID ABRECOR INT DOSPUNTO INT CIERRACOR VECTOR OF tipo",
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
"factor : MENOS CTE",
"comparador : IGUAL",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : MENOR",
"comparador : MAYOR",
};

//#line 113 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"

String ins;
LexicalAnalyzer la;
public static List<String> errors;
public static List<String> detections;
private Map<String, Integer> hm = generateHash() ;
private int lineNumber = 0;
private int previousTokenLineNumber = 0;


void yyerror(String s) {
	errors.add(s);
}

int yylex() {
	String s;
	int tok;
	
	if (!la.hasMoreTokens()) {
		 	return 0;
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
 yyparse();
}
//#line 429 "Parser.java"
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
//#line 31 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable comun en linea "+previousTokenLineNumber); }
break;
case 8:
//#line 32 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion de variable vector en linea "+previousTokenLineNumber); }
break;
case 9:
//#line 35 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Variable de tipo entera " + previousTokenLineNumber ); }
break;
case 10:
//#line 36 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Variable de tipo entera sin signo " + previousTokenLineNumber ); }
break;
case 17:
//#line 51 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion imprimir en "+previousTokenLineNumber); }
break;
case 21:
//#line 57 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("se asigno un valor "+previousTokenLineNumber); }
break;
case 27:
//#line 74 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if en "+previousTokenLineNumber); }
break;
case 28:
//#line 77 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Declaracion if else en "+previousTokenLineNumber); }
break;
case 32:
//#line 86 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ detections.add("Se hizo una resta bien "+previousTokenLineNumber); }
break;
case 42:
//#line 102 "C:\Users\Mariel\Desktop\Corvilador\Compilador\src\org\compiler\syntactic\parser.y"
{ System.out.println( "-"+val_peek(0).ival ); }
break;
//#line 618 "Parser.java"
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
