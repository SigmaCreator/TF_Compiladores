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






//#line 3 "exemploSem.y"
  import java.io.*;
  import java.util.ArrayList;
//#line 20 "Parser.java"




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
public final static short CLASS=257;
public final static short EXTENDS=258;
public final static short PRIVATE=259;
public final static short PUBLIC=260;
public final static short IDENT=261;
public final static short NEW=262;
public final static short OVERRIDE=263;
public final static short IF=264;
public final static short ENDIF=265;
public final static short ELSE=266;
public final static short WHILE=267;
public final static short ENDWHILE=268;
public final static short FOR=269;
public final static short ENDFOR=270;
public final static short PRINT=271;
public final static short SCAN=272;
public final static short BREAK=273;
public final static short RETURN=274;
public final static short INT=275;
public final static short DOUBLE=276;
public final static short BOOL=277;
public final static short NUMINT=278;
public final static short NUMDOUBLE=279;
public final static short STRING=280;
public final static short LITERAL=281;
public final static short AND=282;
public final static short VOID=283;
public final static short MAIN=284;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    3,    3,    5,    4,    8,    4,   10,    6,    9,
    9,   11,    1,    1,    1,    1,    1,   12,   12,    7,
   13,   13,   15,   17,   14,   19,   20,   14,   21,   22,
   14,   16,   16,   23,   23,   18,   24,   24,   25,   25,
   25,   26,   25,   27,   25,   25,   25,   25,   25,   29,
   25,   30,   30,   28,   28,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,   31,    2,    2,
};
final static short yylen[] = {                            2,
    1,    2,    1,    0,    7,    0,    9,    0,    4,    2,
    0,    3,    1,    1,    1,    1,    1,    3,    1,    3,
    2,    1,    0,    0,    9,    0,    0,    8,    0,    0,
   10,    1,    0,    4,    2,    3,    2,    0,    2,    8,
    5,    0,    6,    0,    9,    5,    5,    1,    1,    0,
    4,    1,    0,    3,    1,    3,    3,    3,    3,    3,
    3,    3,    1,    1,    3,    3,    4,    0,    6,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,    6,    0,
    0,    0,    0,    0,    8,    0,    0,    0,    0,    0,
    5,    0,   16,   13,   14,   15,   17,    0,    9,    0,
    0,    0,    0,    0,   22,    7,   19,    0,   10,    0,
    0,   23,   21,   12,    0,    0,   29,    0,   18,    0,
    0,    0,    0,    0,   35,   27,    0,    0,    0,    0,
    0,    0,   24,    0,   34,   30,    0,   38,   28,    0,
    0,    0,    0,   25,    0,    0,    0,   42,   44,    0,
    0,   49,    0,   63,   64,   36,    0,    0,   48,   37,
   31,    0,   68,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   65,
   62,    0,    0,    0,    0,    0,    0,    0,    0,   67,
    0,    0,    0,    0,    0,    0,   51,    0,    0,   41,
    0,    0,    0,   46,   47,   69,    0,   43,    0,    0,
    0,   40,    0,   45,
};
final static short yydgoto[] = {                          2,
   28,   88,    3,    4,    8,   13,   17,   11,   29,   19,
   30,   38,   34,   35,   48,   51,   67,   89,   40,   60,
   53,   70,   52,   72,   90,   95,   96,  111,  119,  112,
  113,
};
final static short yysindex[] = {                      -234,
 -230,    0, -234,    0, -217,    0, -218,  -71,    0, -199,
  -59,    8, -190, -199,    0,   33,  -32, -190, -106, -151,
    0,  -28,    0,    0,    0,    0,    0, -177,    0, -106,
    0, -106, -162, -151,    0,    0,    0,  -20,    0,   63,
 -150,    0,    0,    0, -145, -106,    0,   88,    0, -132,
   96,   90,   98, -106,    0,    0, -106, -106,  102, -106,
  -99,  135,    0,   61,    0,    0, -106,    0,    0, -106,
   61,   10,   61,    0,  155,  -60,  -35,    0,    0,  162,
  173,    0,  -35,    0,    0,    0,  -35,  -13,    0,    0,
    0,  -35,    0,   -7,  -35,  -35,  -35,  -51,   53,  -34,
  -35,  -35,  -35,  -35,  -35,  -35,  -35,  -35,    0,   53,
  146,  174,  176,   43,   14,   20,    1,  177,  158,    0,
    0,   53,   59,   75,   75,  -33,  -30,  -63,  -35,    0,
  -35, -188,   43,  -35,  161,  163,    0,   53,  180,    0,
  165,  -44,   26,    0,    0,    0,   43,    0,  -35,  -40,
   47,    0,  -37,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  234,    0,  112,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -22,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -101,
  -24,    0,    0,  115,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  205,    0,    0,    0,    0,
    0,  210,    0,  205,    0,    0,    0,  205,    0,  143,
    0,    0,    0,    0,    0,    0,  143,    0,    0,  143,
    0,    0,    0,    0,  -41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  217,    0,    0,    0,    0,    0,    0,  202,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  113,
  227,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  150,  206,  201,  195,  170,  145,  138,    0,    0,
  217,    0,    0,    0,    0,    0,    0,  117,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  110,   44,    0,  267,    0,  259,  258,    0,   93,    0,
    0,    0,    0,  244,    0,  -21,    0,  101,    0,    0,
    0,    0,    0,    0,  -89,    0,    0,  183,    0,  154,
    0,
};
final static int YYTABLESIZE=357;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         70,
   70,   70,   70,   70,   87,   70,  120,  105,  107,  107,
  108,  108,  106,  106,  108,   26,   70,   70,   70,   70,
   70,   11,    1,   45,  132,  104,  102,  103,  105,  107,
    5,  108,   59,  106,  105,  107,   62,  108,   44,  106,
    7,  135,    9,  142,  129,  109,  104,  102,  103,   87,
  114,   10,  104,  102,  103,  105,  107,  150,  108,   12,
  106,  105,  107,   14,  108,   15,  106,  105,  107,   16,
  108,  133,  106,  104,  102,  103,  140,  141,  134,  104,
  102,  103,   87,   37,  149,  104,  102,  103,  105,  107,
   20,  108,   21,  106,  105,  107,   36,  108,   42,  106,
  105,  107,   46,  108,  153,  106,  104,  102,  103,   31,
   47,   32,  104,  102,  103,   49,  105,  107,  104,  108,
   94,  106,   39,   24,   25,   26,   99,   54,   55,   33,
  100,   27,   68,   57,   86,  110,   56,   58,  115,  116,
  110,   41,   63,   33,  121,  122,  123,  124,  125,  126,
  127,  128,   64,   55,   23,   50,   55,   54,   11,   71,
   54,   65,   73,   50,   69,   68,   61,   50,   24,   25,
   26,   74,  138,   91,  110,   66,   27,  143,   58,   58,
   58,   58,   58,   68,   58,   56,   56,   56,   56,  129,
   66,   56,  151,   66,   92,   58,   58,   58,   58,   58,
   93,   97,   56,   56,   56,   56,   56,   66,   66,  118,
   59,   59,   98,   59,  130,  131,  137,  136,  101,  144,
  146,  145,  147,  148,  152,   75,   76,   59,   59,   59,
   59,   59,  154,    1,    4,   57,   16,   11,   57,   20,
   70,   60,   84,   85,   60,   33,   61,  101,  101,   61,
   32,  101,   57,   57,   57,   57,   57,   53,   60,   60,
   50,   60,   60,   61,   61,   11,   61,   52,  101,    6,
   75,   76,   18,   77,  101,   22,   78,   43,   79,  117,
   80,   81,   82,   83,  139,    0,    0,   84,   85,    0,
    0,    0,    0,    0,    0,  101,    0,    0,    0,    0,
    0,  101,    0,   75,   76,    0,   77,  101,    0,   78,
    0,   79,    0,   80,   81,   82,   83,    0,    0,    0,
   84,   85,    0,    0,    0,    0,    0,    0,  101,    0,
    0,    0,    0,    0,  101,    0,    0,    0,    0,    0,
  101,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  101,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   40,   47,   41,   42,   43,   43,
   45,   45,   47,   47,   45,   40,   58,   59,   60,   61,
   62,  123,  257,   44,  114,   60,   61,   62,   42,   43,
  261,   45,   54,   47,   42,   43,   58,   45,   59,   47,
  258,   41,  261,  133,   44,   59,   60,   61,   62,   40,
   58,  123,   60,   61,   62,   42,   43,  147,   45,  259,
   47,   42,   43,  123,   45,   58,   47,   42,   43,  260,
   45,   58,   47,   60,   61,   62,  265,  266,   59,   60,
   61,   62,   40,  261,   59,   60,   61,   62,   42,   43,
   58,   45,  125,   47,   42,   43,  125,   45,  261,   47,
   42,   43,   40,   45,   58,   47,   60,   61,   62,  261,
  261,  263,   60,   61,   62,  261,   42,   43,   60,   45,
   77,   47,   30,  275,  276,  277,   83,   40,  261,   20,
   87,  283,  123,   44,  125,   92,   41,   40,   95,   96,
   97,   32,   41,   34,  101,  102,  103,  104,  105,  106,
  107,  108,   60,   41,  261,   46,   44,   41,  260,   67,
   44,  261,   70,   54,   64,  123,   57,   58,  275,  276,
  277,   71,  129,   73,  131,   41,  283,  134,   41,   42,
   43,   44,   45,  123,   47,   41,   42,   43,   44,   44,
   41,   47,  149,   44,   40,   58,   59,   60,   61,   62,
  261,   40,   58,   59,   60,   61,   62,   58,   59,  261,
   41,   42,   40,   44,   41,   40,   59,   41,  282,   59,
   41,   59,   58,  268,  265,  261,  262,   58,   59,   60,
   61,   62,  270,    0,  123,   41,  261,  260,   44,  125,
  282,   41,  278,  279,   44,   41,   41,  282,  282,   44,
   41,  282,   58,   59,   60,   61,   62,   41,   58,   59,
   59,   61,   62,   58,   59,  123,   61,   41,  282,    3,
  261,  262,   14,  264,  282,   18,  267,   34,  269,   97,
  271,  272,  273,  274,  131,   -1,   -1,  278,  279,   -1,
   -1,   -1,   -1,   -1,   -1,  282,   -1,   -1,   -1,   -1,
   -1,  282,   -1,  261,  262,   -1,  264,  282,   -1,  267,
   -1,  269,   -1,  271,  272,  273,  274,   -1,   -1,   -1,
  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,  282,   -1,
   -1,   -1,   -1,   -1,  282,   -1,   -1,   -1,   -1,   -1,
  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  282,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=284;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"CLASS","EXTENDS","PRIVATE","PUBLIC","IDENT",
"NEW","OVERRIDE","IF","ENDIF","ELSE","WHILE","ENDWHILE","FOR","ENDFOR","PRINT",
"SCAN","BREAK","RETURN","INT","DOUBLE","BOOL","NUMINT","NUMDOUBLE","STRING",
"LITERAL","AND","VOID","MAIN",
};
final static String yyrule[] = {
"$accept : program",
"program : classList",
"classList : classList class",
"classList : class",
"$$1 :",
"class : CLASS IDENT $$1 '{' atribs methods '}'",
"$$2 :",
"class : CLASS IDENT EXTENDS IDENT $$2 '{' atribs methods '}'",
"$$3 :",
"atribs : PRIVATE ':' $$3 declareList",
"declareList : declare declareList",
"declareList :",
"declare : type idList ';'",
"type : INT",
"type : DOUBLE",
"type : BOOL",
"type : IDENT",
"type : VOID",
"idList : idList ',' IDENT",
"idList : IDENT",
"methods : PUBLIC ':' methodList",
"methodList : methodList method",
"methodList : method",
"$$4 :",
"$$5 :",
"method : type IDENT $$4 '(' auxParamList ')' $$5 declareList block",
"$$6 :",
"$$7 :",
"method : IDENT $$6 '(' auxParamList ')' $$7 declareList block",
"$$8 :",
"$$9 :",
"method : OVERRIDE type IDENT $$8 '(' auxParamList ')' $$9 declareList block",
"auxParamList : paramList",
"auxParamList :",
"paramList : paramList ',' type IDENT",
"paramList : type IDENT",
"block : '{' cmdList '}'",
"cmdList : cmdList cmd",
"cmdList :",
"cmd : exp ';'",
"cmd : IF exp ':' cmd ELSE ':' cmd ENDIF",
"cmd : IF exp ':' cmd ENDIF",
"$$10 :",
"cmd : WHILE $$10 exp ':' cmd ENDWHILE",
"$$11 :",
"cmd : FOR $$11 exp ';' exp ';' exp ':' ENDFOR",
"cmd : PRINT '(' argList ')' ';'",
"cmd : SCAN '(' IDENT ')' ';'",
"cmd : block",
"cmd : BREAK",
"$$12 :",
"cmd : RETURN exp $$12 ';'",
"auxArgList : argList",
"auxArgList :",
"argList : argList ',' exp",
"argList : exp",
"exp : exp '+' exp",
"exp : exp '*' exp",
"exp : exp '-' exp",
"exp : exp '/' exp",
"exp : exp '<' exp",
"exp : exp '>' exp",
"exp : exp AND exp",
"exp : NUMINT",
"exp : NUMDOUBLE",
"exp : '(' exp ')'",
"exp : exp '=' exp",
"exp : IDENT '(' auxArgList ')'",
"$$13 :",
"exp : NEW IDENT $$13 '(' auxArgList ')'",
"exp : IDENT",
};

//#line 256 "exemploSem.y"

  private Yylex lexer;

  private TS_entry global;
  private TabSimb ts;

  public static TS_entry Tp_INT =  new TS_entry("int", null, SimbID.TipoBase, null);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  SimbID.TipoBase, null);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  SimbID.TipoBase, null);

  public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  SimbID.TipoBase, null);

  public static TS_entry Tp_VOID = new TS_entry("void", null,  SimbID.TipoBase, null);


  public static final int ATRIB = 1600;

  private TS_entry currScope;
  private SimbID currSymb;
  private TS_entry currentType;
  private ArrayList<TS_entry> args;
  private int forWhileCounter = 0;

  private int yylex () {
    int yyl_return = -1;
    try { yylval = new ParserVal(0);  yyl_return = lexer.yylex(); }
    catch (IOException e) { System.err.println("IO error :" + e); }
    return yyl_return;
  }


  public void yyerror (String error) { System.err.printf("Erro (linha: %2d) \tMensagem: %s\n", lexer.getLine(), error); }


  public Parser(Reader r) {

    lexer = new Yylex(r, this);
    global = new TS_entry (null,null,null,null);
    ts = global.locais;

    args = new ArrayList<>();

  }  

  public void setDebug(boolean debug) { yydebug = debug; }

  public void listarTS() { ts.listar();}

  public static void main(String args[]) throws IOException {

    System.out.println("\n\nVerificador semantico simples\n");
    
    Parser yyparser;
    if ( args.length > 0 ) { /* parse a file */ yyparser = new Parser(new FileReader(args[0])); }
    else {
      // interactive mode
      System.out.println("[Quit with CTRL-D]");
      System.out.print("Programa de entrada:\n");
      yyparser = new Parser(new InputStreamReader(System.in));
    }

    yyparser.yyparse();

    yyparser.listarTS();

    System.out.print("\n\nFeito!\n");
    
  }


  TS_entry validaTipo(int operador, TS_entry A, TS_entry B) {

        switch ( operador ) {
            case ATRIB:
                    if ( (A == Tp_INT && B == Tp_INT)                        ||
                         ((A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE))) ||
                         (A == B) )
                         return A;
                     else yyerror("(sem) tipos incomp. para atribuicao: "+ A.getTipoStr() + " = "+B.getTipoStr());
                    break;

            case '+' :
                    if ( A == Tp_INT && B == Tp_INT) return Tp_INT;
                    else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                              (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) ) 
                         return Tp_DOUBLE;     
                    else yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                    break;
            case '*' :
                    if ( A == Tp_INT && B == Tp_INT) return Tp_INT;
                    else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                              (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) ) 
                         return Tp_DOUBLE;     
                    else yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                    break;
            case '-' :
                    if ( A == Tp_INT && B == Tp_INT) return Tp_INT;
                    else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                              (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) ) 
                         return Tp_DOUBLE;     
                    else yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                    break;
            case '/' :
                    if ( A == Tp_INT && B == Tp_INT) return Tp_INT;
                    else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                              (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) ) 
                         return Tp_DOUBLE;     
                    else yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                    break;
            case '>' :
                      if ((A == Tp_INT || A == Tp_DOUBLE) && (B == Tp_INT || B == Tp_DOUBLE)) return Tp_BOOL;
                      else yyerror("(sem) tipos incomp. para op relacional: "+ A.getTipoStr() + " > "+B.getTipoStr());
                      break;
            case '<' :
                      if ((A == Tp_INT || A == Tp_DOUBLE) && (B == Tp_INT || B == Tp_DOUBLE)) return Tp_BOOL;
                      else yyerror("(sem) tipos incomp. para op relacional: "+ A.getTipoStr() + " > "+B.getTipoStr());
                      break; 
            case AND:
                      if (A == Tp_BOOL && B == Tp_BOOL) return Tp_BOOL;
                      else yyerror("(sem) tipos incomp. para op lógica: "+ A.getTipoStr() + " && "+B.getTipoStr());
                      break;
            }

            return Tp_ERRO;
           
     }

boolean compare(ArrayList<TS_entry> args, ArrayList<TS_entry> methodLocals) {

  if (args.size() == 0) { 
    if (methodLocals.size() == 0) return true; 
    else return false; 
  } else if (methodLocals.size() == 0) return false;

  for (int i = 0; i < methodLocals.size(); i++) {

    if (i > args.size()) { return methodLocals.get(i).getSymbType() != SimbID.NomeParam; }
    if (args.get(i) != methodLocals.get(i).getTipo()) return false;

  }

  return true;


}
//#line 531 "Parser.java"
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
case 4:
//#line 37 "exemploSem.y"
{

          if (ts.pesquisa(val_peek(0).sval) != null) { yyerror("Class " + val_peek(0).sval +  " already declared\n"); }
          else { 

            TS_entry newEntry = new TS_entry (val_peek(0).sval, null, SimbID.Classe, null); 
            newEntry.parent = global;
            currScope = newEntry;
            ts.insert(newEntry);

          } /* OK*/

        }
break;
case 6:
//#line 53 "exemploSem.y"
{

          if (ts.pesquisa(val_peek(2).sval) != null) { yyerror("Class " + val_peek(2).sval +  " already declared\n"); }
          else {

            TS_entry superClass = ts.pesquisa(val_peek(0).sval);
            if (superClass == null) { yyerror("Super Class " + val_peek(0).sval +  " does not exist\n"); }
            else { 

              TS_entry newEntry = new TS_entry (val_peek(2).sval, null, SimbID.Classe, superClass);
              newEntry.parent = superClass;
              currScope = newEntry;
              ts.insert(newEntry);

            }
          } /* OK*/

        }
break;
case 8:
//#line 76 "exemploSem.y"
{ currSymb = SimbID.VarLocal; }
break;
case 13:
//#line 86 "exemploSem.y"
{ currentType = Tp_INT; }
break;
case 14:
//#line 87 "exemploSem.y"
{ currentType = Tp_DOUBLE; }
break;
case 15:
//#line 88 "exemploSem.y"
{ currentType = Tp_BOOL; }
break;
case 16:
//#line 89 "exemploSem.y"
{ 
                 TS_entry nodo = currScope.pesquisa(val_peek(0).sval);
                 if (nodo == null) { yyerror("Type <" + val_peek(0).sval + "> not Defined"); currentType = Tp_ERRO;}
                 else { currentType = nodo.getTipo(); }
               }
break;
case 17:
//#line 94 "exemploSem.y"
{ currentType = Tp_VOID; }
break;
case 18:
//#line 97 "exemploSem.y"
{ currScope.locais.insert(new TS_entry(val_peek(0).sval, currentType, currSymb, null)); }
break;
case 19:
//#line 98 "exemploSem.y"
{ currScope.locais.insert(new TS_entry(val_peek(0).sval, currentType, currSymb, null)); }
break;
case 23:
//#line 108 "exemploSem.y"
{ 
            TS_entry newMethod = new TS_entry(val_peek(0).sval, currentType, SimbID.NomeFuncao, null); 
            newMethod.parent = currScope; 
            currScope = newMethod; 
          }
break;
case 24:
//#line 114 "exemploSem.y"
{ if (!currScope.parent.pesquisaPoli(currScope)) yyerror(" Method overload already defined "); }
break;
case 25:
//#line 118 "exemploSem.y"
{ currScope.parent.locais.insert(currScope); currScope = currScope.parent; }
break;
case 26:
//#line 120 "exemploSem.y"
{
            if (!val_peek(0).sval.equals(currScope.getId())) yyerror("それはちがうよ");
            TS_entry newMethod = new TS_entry(val_peek(0).sval, currScope, SimbID.NomeFuncao, null); 
            newMethod.parent = currScope; 
            currScope = newMethod;
          }
break;
case 27:
//#line 127 "exemploSem.y"
{  if (!currScope.parent.pesquisaPoli(currScope)) yyerror(" Method overload already defined "); }
break;
case 28:
//#line 131 "exemploSem.y"
{ currScope.parent.locais.insert(currScope); currScope = currScope.parent; }
break;
case 29:
//#line 132 "exemploSem.y"
{ 
                                TS_entry newMethod = new TS_entry(val_peek(0).sval, currentType, SimbID.NomeFuncao, null); 
                                newMethod.parent = currScope; 
                                currScope = newMethod; 
                             }
break;
case 30:
//#line 140 "exemploSem.y"
{ if (currScope.parent.pesquisaPoli(currScope)) yyerror("No method to override on base class with given parameters."); }
break;
case 31:
//#line 144 "exemploSem.y"
{ currScope.parent.locais.insert(currScope); currScope = currScope.parent; }
break;
case 34:
//#line 152 "exemploSem.y"
{ currScope.locais.insert(new TS_entry(val_peek(0).sval, currentType, SimbID.NomeParam, null)); }
break;
case 35:
//#line 153 "exemploSem.y"
{ currScope.locais.insert(new TS_entry(val_peek(0).sval, currentType, SimbID.NomeParam, null)); }
break;
case 42:
//#line 166 "exemploSem.y"
{ forWhileCounter++; }
break;
case 43:
//#line 166 "exemploSem.y"
{ forWhileCounter--;  }
break;
case 44:
//#line 167 "exemploSem.y"
{ forWhileCounter++; }
break;
case 45:
//#line 167 "exemploSem.y"
{ forWhileCounter--; }
break;
case 46:
//#line 168 "exemploSem.y"
{ args.clear(); }
break;
case 49:
//#line 171 "exemploSem.y"
{ if(forWhileCounter == 0 ) yyerror("break out of for or while loop. Break in to break out.");  }
break;
case 50:
//#line 172 "exemploSem.y"
{ if(((TS_entry)val_peek(0).obj).getTipo() != currScope.getTipo()) yyerror("retun value is wrong type. Expected " + currScope.getTipo()); 
                   if(currScope.getSymbType() != SimbID.NomeFuncao) yyerror("Return command in non-method scope."); }
break;
case 54:
//#line 180 "exemploSem.y"
{ args.add((TS_entry)val_peek(0).obj); }
break;
case 56:
//#line 184 "exemploSem.y"
{ yyval.obj = validaTipo('+', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 57:
//#line 185 "exemploSem.y"
{ yyval.obj = validaTipo('*', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 58:
//#line 186 "exemploSem.y"
{ yyval.obj = validaTipo('-', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 59:
//#line 187 "exemploSem.y"
{ yyval.obj = validaTipo('/', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 60:
//#line 188 "exemploSem.y"
{ yyval.obj = validaTipo('<', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 61:
//#line 189 "exemploSem.y"
{ yyval.obj = validaTipo('>', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 62:
//#line 190 "exemploSem.y"
{ yyval.obj = validaTipo(AND, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 63:
//#line 191 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 64:
//#line 192 "exemploSem.y"
{ yyval.obj = Tp_DOUBLE; }
break;
case 65:
//#line 193 "exemploSem.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 66:
//#line 194 "exemploSem.y"
{ yyval.obj = validaTipo(ATRIB, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj);  }
break;
case 67:
//#line 195 "exemploSem.y"
{ 

                                TS_entry call = currScope.parent.pesquisa(val_peek(3).sval);
                                if (call == null) yyerror("Method " + val_peek(3).sval + " not declared");

                                ArrayList<TS_entry> methods = currScope.parent.locais.getMethods(val_peek(3).sval);
                                ArrayList<TS_entry> aux;

                                TS_entry trueType = Tp_ERRO;
                                for ( TS_entry m : methods ) {

                                  aux = m.locais.getLista();

                                  if (compare(args,aux)) trueType = m.getTipo();

                                }

                                if (trueType == Tp_ERRO) {yyerror("Method " + val_peek(3).sval + " call's arguments mismatch");}

                                args.clear();
                                yyval.obj = trueType;
      }
break;
case 68:
//#line 219 "exemploSem.y"
{
                    TS_entry instance = currScope.parent.pesquisa(val_peek(0).sval);
                    if (instance == null) yyerror("Class " + val_peek(0).sval + " not declared"); 
                }
break;
case 69:
//#line 224 "exemploSem.y"
{ 

                                      ArrayList<TS_entry> methods = currScope.parent.locais.getMethods(val_peek(4).sval);
                                      ArrayList<TS_entry> aux;

                                      TS_entry trueType = Tp_ERRO;
                                      for ( TS_entry m : methods ) {

                                        aux = m.locais.getLista();

                                        if (compare(args,aux)) trueType = m.getTipo();

                                      }

                                      if (trueType == Tp_ERRO){ yyerror("Constructor method " + val_peek(4).sval + " call's arguments mismatch"); }
                                      if (trueType == Tp_VOID){ yyerror("Construtor returns void type."); trueType = Tp_ERRO; }
                                      args.clear();
                                      yyval.obj = trueType;
                                    }
break;
case 70:
//#line 245 "exemploSem.y"
{ 

        TS_entry nodo = currScope.pesquisa(val_peek(0).sval);
        if (nodo == null ) { yyerror("(sem) var <" + val_peek(0).sval + "> nao declarada"); yyval.obj = Tp_ERRO; } 
        else { yyval.obj = nodo.getTipo(); }

      }
break;
//#line 951 "Parser.java"
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
