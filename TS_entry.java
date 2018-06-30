import java.util.ArrayList;

public class TS_entry {
    private String id;
    private SimbID classe;  
    private TS_entry tipo;
    private TS_entry tipoBase;
    public TabSimb locais;
    public TS_entry parent;

    public TS_entry(String umId, TS_entry umTipo, SimbID umaClasse, TS_entry tp) {
        locais = new TabSimb();
        id = umId;
        tipo = umTipo;
        classe = umaClasse;
        tipoBase = tp;
    }

    public String getId() { return id; }

    public TS_entry getTipo() { return tipo; }

    public SimbID getSymbType() { return classe; }
   
    public TS_entry getTipoBase() { return tipoBase; }

    public TS_entry pesquisa(String umId) { 

        TS_entry entry = locais.pesquisa(umId);
        if (entry != null) return entry;
        else if (parent != null) return parent.pesquisa(umId);
        else return null;
    }

    public boolean pesquisaPoli(TS_entry newEntry) { return locais.pesquisaPoli(newEntry); }
   
    public String toString() {

       StringBuilder aux = new StringBuilder("");
        
       aux.append("Id: ");
       aux.append(String.format("%-10s", id));

       aux.append("\tClasse: ");
       aux.append(classe);
       aux.append("\tTipo: "); 
       aux.append(tipo2str(this.tipo)); 
       
      return aux.toString();

    }

    public String getTipoStr() { return tipo2str(this); }

    public String tipo2str(TS_entry tipo) {

        if (tipo == null)                   return "null"; 
        else if (tipo == Parser.Tp_INT)     return "int"; 
        else if (tipo == Parser.Tp_BOOL)    return "boolean"; 
        else if (tipo == Parser.Tp_DOUBLE)  return "double";
        else if (tipo == Parser.Tp_ERRO)    return  "_erro_";
        else                                return "erro/tp";

    }

    public ArrayList<TS_entry> getMethods(String nome) { 

        ArrayList<TS_entry> lista = locais.getMethods(nome);

        if (parent != null) lista.addAll(parent.getMethods(nome));

        return lista;
    }

}






