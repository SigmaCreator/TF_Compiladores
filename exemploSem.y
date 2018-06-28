    
%{
  import java.io.*;
%}

%token CLASS, EXTENDS, PRIVATE, PUBLIC, IDENT, NEW
%token IF, ENDIF, ELSE, WHILE, ENDWHILE, FOR, ENDFOR, PRINT, SCAN
%token INT, DOUBLE, BOOL, NUM, STRING
%token LITERAL, AND, VOID, MAIN

%right '='
%nonassoc '>'
%left '+'
%left AND

%type <sval> IDENT
%type <ival> NUM
%type <obj> type
%type <obj> exp

%%


program : classList
        ;

classList : classList class
          | class
          ;

class : CLASS IDENT {

          if (ts.pesquisa($2) != null) { yyerror("Class " + $2 +  " already declared\n"); }
          else { 

            TS_entry newEntry = new TS_entry ($2, null, SimbID.Classe, null); 
            newEntry.parent = global;
            currScope = newEntry;
            ts.insert(newEntry);

          } // OK

        } 
        
        '{' atribs methods '}'

      | CLASS IDENT EXTENDS IDENT {

          if (ts.pesquisa($2) != null) { yyerror("Class " + $2 +  " already declared\n"); }
          else {

            TS_entry superClass = ts.pesquisa($4);
            if (superClass == null) { yyerror("Super Class " + $4 +  " does not exist\n"); }
            else { 

              TS_entry newEntry = new TS_entry ($2, null, SimbID.Classe, superClass);
              newEntry.parent = superClass;
              currScope = newEntry;
              ts.insert(newEntry);

            }
          } // OK

        }
          
        '{' atribs methods '}'
      ;

atribs : PRIVATE ':' { currSymb = SimbID.VarLocal; } declareList
       ;

declareList : declare declareList
            |
            ;

declare : type idList
     ;

type : INT    { currentType = Tp_INT; }
     | DOUBLE { currentType = Tp_DOUBLE; }
     | BOOL   { currentType = Tp_BOOL; }
     | IDENT  { 
                TS_entry nodo = currScope.pesquisa($1);
                if (nodo == null) { yyerror("Type <" + $1 + "> not Defined"); $$ = Tp_ERRO; }
                else { currentType = nodo.getTipo();}
              }
    | IDENT '[' ']' { 
                TS_entry nodo = currScope.pesquisa($1);
                if (nodo == null) { yyerror("Type <" + $1 + "> not Defined"); $$ = Tp_ERRO; }
                else { currentType = nodo.getTipo();}
              }
     ;

var : IDENT               { currScope.insert(new TS_entry($1, currentType, currSymb, null)); } 
    | IDENT '[' exp ']'   { TS_entry basetype = nodo.pesquisa($1);
                            if(basetype == null) { yyerror("Undefined base type " + "<" + $1 + ">");}
                            else{
                                if($3 <= 0) {yyerror("Array size is negative on <" + $1 + "> or zero.");}
                                else{ 
                                  currScope.insert(new TS_entry($1, currentType, currSymb, basetype, true, $3)); 
                                }
                            }                   
                          } 
    ;

idList : idList  ',' var
       | var
       ;

methods : PUBLIC ':' methodList
        ;

methodList : methodList method
           | method
           ;

method : type IDENT { 
            TS_entry newMethod = new TS_entry($2, currentType, SimbID.NomeFuncao, null); 
            newMethod.parent = currScope; 
            currScope = newMethod; 
          } 

         '(' paramList ')' { if (!currScope.parent.pesquisaPoli(currScope)) yyerror(" Method overload already defined "); }

          declareList block 
          
          { currScope.parent.locais.insert(currScope); currScope = currScope.parent; }

       | IDENT {
            if ($1 != currScope.getId()) yyerror("それはちがうよ");
            TS_entry newMethod = new TS_entry($1, currScope, SimbID.NomeFuncao, null); 
            newMethod.parent = currScope; 
            currScope = newMethod;
          }

          '(' paramList ')' {  if (!currScope.parent.pesquisaPoli(currScope)) yyerror(" Method overload already defined "); } 

          declareList block

          { currScope.parent.locais.insert(currScope); currScope = currScope.parent; }

       ; 

auxParamList : paramList
             | 
             ;

paramList : paramList ',' type IDENT { currScope.locais.insert(new TS_entry($4, currentType, SimbID.NomeParam, null)); }
          | type IDENT { currScope.locais.insert(new TS_entry($2, currentType, SimbID.NomeParam, null)); }
          ;

block : '{' cmdList '}' 
      ;

cmdList : cmdList cmd
        |
        ;

cmd : exp ';'
    | IF exp ':' cmd ELSE ':' cmd ENDIF
    | IF exp ':' cmd ENDIF
    | WHILE exp ':' cmd ENDWHILE
    | FOR exp ';' exp ';' exp ':' ENDFOR
    | PRINT '(' argList ')' ';' { args.clear(); }
    | SCAN '(' IDENT ')' ';'
    | block 
    ;

auxArgList : argList
           |
           ;

argList : argList ',' exp { args.add($3); }
        | exp { args.add($1); }
        ;

exp : exp '+' exp { $$ = validaTipo('+', (TS_entry)$1, (TS_entry)$3); }
    | exp '>' exp { $$ = validaTipo('>', (TS_entry)$1, (TS_entry)$3); }
    | exp AND exp { $$ = validaTipo(AND, (TS_entry)$1, (TS_entry)$3); }
    | NUM         { $$ = Tp_INT; }
    | '(' exp ')' { $$ = $2; }
    | exp '=' exp  {  $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  }
    | IDENT {
        TS_entry call = currScope.parent.pesquisa($1);
        if (call == null) { yyerror("Method " + $1 + " not declared");

    } '(' auxArgList ')' { 

      ArrayList<TS_entry> methods = currScope.parent.locais.getMethods($1);
      ArrayList<TS_entry> aux;

      TS_entry trueType;
      for ( TS_entry m : methods ) {

        aux = m.locais.getLista();

        if (compare(args,aux)) trueType = m.getTipo();

      }

      if (trueType == null) yyerror("Method " + $1 + " call's arguments mismatch");

      args.clear();

    }

    | NEW IDENT {
                  TS_entry instance = currScope.parent.pesquisa($2);
                  if (instance == null) { yyerror("Class " + $2 + " not declared");
                  }
                }

                '(' auxArgList ')' { 

      ArrayList<TS_entry> methods = currScope.parent.locais.getMethods($2);
      ArrayList<TS_entry> aux;

      TS_entry trueType;
      for ( TS_entry m : methods ) {

        aux = m.locais.getLista();

        if (compare(args,aux)) trueType = m.getTipo();

      }

      if (trueType == null) yyerror("Constructor method " + $1 + " call's arguments mismatch");

      args.clear();

    }

    | IDENT { 

        TS_entry nodo = currScope.pesquisa($1);
        if (nodo == null) { yyerror("(sem) var <" + $1 + "> nao declarada"); $$ = Tp_ERRO; } 
        else { $$ = nodo.getTipo(); }

      }

//como garantir q $3 eh um numero
    | IDENT '[' exp ']' { 
        TS_entry nodo = currScope.pesquisa($1);
        if (nodo == null) { yyerror("(sem) var <" + $1 + "> nao declarada"); $$ = Tp_ERRO; } 
        else if(nodo.indexed && nodo.tamanho ){
          if(> $3 && $3 >= 0)   { $$ = nodo.getTipoBase(); } 
          else  {yyerror("Index out of bounds of: " + $1); $$ = Tp_ERRO;}
        } 
        else {yyerror($1 + " isn't an indexed variable" ); $$ = Tp_ERRO;}
    }


    ;


%%

  private Yylex lexer;

  private TS_entry global;
  private TabSimb ts;

  public static TS_entry Tp_INT =  new TS_entry("int", null, SimbID.TipoBase, null);
  public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  SimbID.TipoBase, null);
  public static TS_entry Tp_BOOL = new TS_entry("bool", null,  SimbID.TipoBase, null);

  public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  SimbID.TipoBase, null);

  public static final int ATRIB = 1600;

  private TS_entry currScope;
  private SimbID currSymb;
  private TS_entry currentType;
  private ArrayList<TS_entry> args;

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

            case '>' :
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

  if (args == 0) { 
    if (methodLocals == 0) return true; 
    else return false; 
  } else if (methodLocals == 0) return false;

  for (int i = 0; i < methodLocals.size(); i++) {

    if (i > args.size()) { return methodLocals.get(i).getSymbType() != SimbID.NomeParam; }
    if (args.get(i) != methodLocals.get(i).getTipo()) return false;

  }

  return true;


}
