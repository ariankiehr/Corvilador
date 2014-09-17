%{
import java.lang.Math;
import org.compiler.lex.LexicalAnalyzer;
%}

/* YACC Declarations */
%token NUM
%left '-' '+'
%left '*' '/'
%left NEG /* negation--unary minus */
%right '^' /* exponentiation */

/* Grammar follows */
%%
input: /* empty string */
 | input line
 ;

line: '\n'
 | exp '\n' { System.out.println($1.dval); }
 ;

exp: NUM { $$ = $1; }
 | exp '+' exp { $$ = new ParserVal($1.dval + $3.dval); }
 | exp '-' exp { $$ = new ParserVal($1.dval - $3.dval); }
 | exp '*' exp { $$ = new ParserVal($1.dval * $3.dval); }
 | exp '/' exp { $$ = new ParserVal($1.dval / $3.dval); }
 | '-' exp %prec NEG { $$ = new ParserVal(-$2.dval); }
 | exp '^' exp { $$ = new ParserVal(Math.pow($1.dval, $3.dval)); }
 | '(' exp ')' { $$ = $2; }
 ;
%%

String ins;
LexicalAnalyzer la;

void yyerror(String s)
{
 System.out.println("par:"+s);
}

boolean newline;
int yylex()
{
String s;
int tok;
Double d;
 if (!la.hasMoreTokens())
 if (!newline)
 {
 newline=true;
 return '\n'; //So we look like classic YACC example
 }
 else
 return 0;
 s = la.nextToken().getLexem();
 try
 {
 d = Double.valueOf(s);/*this may fail*/
 yylval = new ParserVal(d.doubleValue()); //SEE BELOW
 tok = NUM;
 }
 catch (Exception e)
 {
 tok = s.charAt(0);/*if not float, return char*/
 }
 return tok;
}

public void dotest(LexicalAnalyzer lex)
{
 la = lex;
 newline=false;
 yyparse();
}
