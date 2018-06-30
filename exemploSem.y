    
%{
  import java.io.*;
  import java.util.ArrayList;
%}

%token CLASS, EXTENDS, PRIVATE, PUBLIC, IDENT, NEW, OVERRIDE, PUBLIC, PRIVATE
%token IF, ENDIF, ELSE, WHILE, ENDWHILE, FOR, ENDFOR, PRINT, SCAN, BREAK, RETURN
%token INT, DOUBLE, BOOL, NUMINT, NUMDOUBLE, STRING
%token LITERAL, AND, VOID, MAIN

%right '='
%nonassoc '>'
%nonassoc '<'
%right '*'
%right '/'
%left '+'
%left '-'
%left AND

%type <sval> IDENT
%type <ival> NUMINT
%type <dval> NUMDOUBLE
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

declare : type idList ';'
        ;

type : INT    { currentType = Tp_INT; }
     | DOUBLE { currentType = Tp_DOUBLE; }
     | BOOL   { currentType = Tp_BOOL; }
     | IDENT  { 
                 TS_entry nodo = currScope.pesquisa($1);
                 if (nodo == null) { yyerror("Type <" + $1 + "> not Defined"); currentType = Tp_ERRO;}
                 else { currentType = nodo.getTipo(); }
               }
     | VOID { currentType = Tp_VOID; }
     ;

idList : idList  ',' IDENT { currScope.locais.insert(new TS_entry($3, currentType, currSymb, null)); } 
       | IDENT { currScope.locais.insert(new TS_entry($1, currentType, currSymb, null)); }
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

         '(' auxParamList ')' { if (!currScope.parent.pesquisaPoli(currScope)) yyerror(" Method overload already defined "); }

          declareList block 
          
          { currScope.parent.locais.insert(currScope); currScope = currScope.parent; }

       | IDENT {
            if (!$1.equals(currScope.getId())) yyerror("それはちがうよ");
            TS_entry newMethod = new TS_entry($1, currScope, SimbID.NomeFuncao, null); 
            newMethod.parent = currScope; 
            currScope = newMethod;
          }

          '(' auxParamList ')' {  if (!currScope.parent.pesquisaPoli(currScope)) yyerror(" Method overload already defined "); } 

          declareList block

          { currScope.parent.locais.insert(currScope); currScope = currScope.parent; }
       | OVERRIDE type IDENT { 
                                TS_entry newMethod = new TS_entry($3, currentType, SimbID.NomeFuncao, null); 
                                newMethod.parent = currScope; 
                                currScope = newMethod; 
                             } 

                              '(' auxParamList ')' 
                              
                             { if (currScope.parent.pesquisaPoli(currScope)) yyerror("No method to override on base class with given parameters."); }

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
    | WHILE { forWhileCounter++; } exp ':' cmd ENDWHILE { forWhileCounter--;  }
    | FOR { forWhileCounter++; }  exp ';' exp ';' exp ':' ENDFOR { forWhileCounter--; }
    | PRINT '(' argList ')' ';' { args.clear(); }
    | SCAN '(' IDENT ')' ';'
    | block
    | BREAK { if(forWhileCounter == 0 ) yyerror("break out of for or while loop. Break in to break out.");  }
    | RETURN exp { if(((TS_entry)$2).getTipo() != currScope.getTipo()) yyerror("retun value is wrong type. Expected " + currScope.getTipo()); 
                   if(currScope.getSymbType() != SimbID.NomeFuncao) yyerror("Return command in non-method scope."); } ';'
    ;

auxArgList : argList
           |
           ;

argList : argList ',' exp { args.add((TS_entry)$3); }
        | exp
        ;

exp : exp '+' exp { $$ = validaTipo('+', (TS_entry)$1, (TS_entry)$3); }
    | exp '*' exp { $$ = validaTipo('*', (TS_entry)$1, (TS_entry)$3); }
    | exp '-' exp { $$ = validaTipo('-', (TS_entry)$1, (TS_entry)$3); }
    | exp '/' exp { $$ = validaTipo('/', (TS_entry)$1, (TS_entry)$3); }
    | exp '<' exp { $$ = validaTipo('<', (TS_entry)$1, (TS_entry)$3); }
    | exp '>' exp { $$ = validaTipo('>', (TS_entry)$1, (TS_entry)$3); }
    | exp AND exp { $$ = validaTipo(AND, (TS_entry)$1, (TS_entry)$3); }
    | NUMINT         { $$ = Tp_INT; }
    | NUMDOUBLE         { $$ = Tp_DOUBLE; }
    | '(' exp ')' { $$ = $2; }
    | exp '=' exp  { $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  }
    | IDENT  '(' auxArgList ')' { 

                                TS_entry call = currScope.parent.pesquisa($1);
                                if (call == null) yyerror("Method " + $1 + " not declared");

                                ArrayList<TS_entry> methods = currScope.parent.locais.getMethods($1);
                                ArrayList<TS_entry> aux;

                                TS_entry trueType = Tp_ERRO;
                                for ( TS_entry m : methods ) {

                                  aux = m.locais.getLista();

                                  if (compare(args,aux)) trueType = m.getTipo();

                                }

                                if (trueType == Tp_ERRO) {yyerror("Method " + $1 + " call's arguments mismatch");}

                                args.clear();
                                $$ = trueType;
      } 
    

    | NEW IDENT {
                    TS_entry instance = currScope.parent.pesquisa($2);
                    if (instance == null) yyerror("Class " + $2 + " not declared"); 
                }
    
                '(' auxArgList ')' { 

                                      ArrayList<TS_entry> methods = currScope.parent.locais.getMethods($2);
                                      ArrayList<TS_entry> aux;

                                      TS_entry trueType = Tp_ERRO;
                                      for ( TS_entry m : methods ) {

                                        aux = m.locais.getLista();

                                        if (compare(args,aux)) trueType = m.getTipo();

                                      }

                                      if (trueType == Tp_ERRO){ yyerror("Constructor method " + $2 + " call's arguments mismatch"); }
                                      if (trueType == Tp_VOID){ yyerror("Construtor returns void type."); trueType = Tp_ERRO; }
                                      args.clear();
                                      $$ = trueType;
                                    }
                  

    | IDENT       { 

        TS_entry nodo = currScope.pesquisa($1);
        if (nodo == null ) { yyerror("(sem) var <" + $1 + "> nao declarada"); $$ = Tp_ERRO; } 
        else { $$ = nodo.getTipo(); }

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
