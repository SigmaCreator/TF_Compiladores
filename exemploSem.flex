%%

%byaccj

%{
  private Parser yyparser;

  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
    yyline = 1;
  }


  public int getLine() {
      return yyline;
  }

%}

NUMINT = [0-9]+
NUMDOUBLE = [0-9]+\.[0-9]+
NL  = \n|\r|\r\n

%%


"$TRACE_ON"  { yyparser.setDebug(true);  }
"$TRACE_OFF" { yyparser.setDebug(false); }
"$MOSTRA_TS" { yyparser.listarTS(); }


/* operators */
"+" |
"-" |
"*" |
"/" |
"<" | 
"=" |
">" |
";" |
":" |
"(" |
")" |
"," |
"\{" |
"\}" |
"\[" | 
"\]"    { return (int) yycharat(0); }

"&&" { return Parser.AND; }

{NUMINT}  { yyparser.yylval = new ParserVal(Integer.parseInt(yytext())); 
         return Parser.NUMINT; }
{NUMDOUBLE}  { yyparser.yylval = new ParserVal(Double.parseDouble(yytext())); 
         return Parser.NUMDOUBLE; }

int       {return Parser.INT; }
double    {return Parser.DOUBLE; }
bool      {return Parser.BOOL; }
string    {return Parser.STRING; }
void      {return Parser.VOID; }
main      {return Parser.MAIN; }
if        {return Parser.IF; }
new       {return Parser.NEW; }
override  {return Parser.OVERRIDE; }
break     {return Parser.BREAK;}
while     {return Parser.WHILE;}
elihw     {return Parser.WHILE;}
fi        {return Parser.ENDIF;}
for       {return Parser.FOR;}
rof       {return Parser.ENDFOR;}
print     {return Parser.PRINT;} 
scan      {return Parser.SCAN;}
class     {return Parser.CLASS;}
private   {return Parser.PRIVATE;}
public    {return Parser.PUBLIC;}
return    {return Parser.RETURN;}
[a-zA-Z][a-zA-Z_0-9]* { yyparser.yylval = new ParserVal(yytext());
                     return Parser.IDENT; }

\"[^\"]*\" { yyparser.yylval = new ParserVal(yytext());
             return Parser.LITERAL; }



{NL}   {yyline++;}
[ \t]+ { }

.    { System.err.println("Error: unexpected character '"+yytext()+"' na linha "+yyline); return YYEOF; }






