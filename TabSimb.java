import java.util.ArrayList;
import java.util.Iterator;


public class TabSimb
{
    private ArrayList<TS_entry> lista;
    
    public TabSimb() {  lista = new ArrayList<TS_entry>();  }
    
    public void insert( TS_entry nodo ) {   lista.add(nodo);    }    
    
    public void listar() {

        System.out.println("\n\nListagem da tabela de simbolos:\n");

        for (TS_entry nodo : lista) System.out.println(nodo);
    }
      
    public TS_entry pesquisa(String umId) {
        for (TS_entry nodo : lista) {
            if (nodo.getId().equals(umId)) {
	            return nodo;
            }
        }
        return null;
    }

    public boolean pesquisaPoli(TS_entry umaEntry) { // Retorna true se pode adicionar essa entry, false cc

        boolean returnValue = false;

        for (TS_entry nodo : lista) {
        
            if (nodo.getId().equals(umaEntry.getId())) {

                if (nodo.getSymbType() != SimbID.NomeFuncao) return false;

                ArrayList<TS_entry> tsNodo = nodo.locais.getLista();
                ArrayList<TS_entry> tsEntry = umaEntry.locais.getLista();

                if (tsEntry.size() == 0) {

                    if(tsNodo.size() == 0) return false;

                    for (TS_entry e : tsNodo) 
                        if (e.getSymbType() == SimbID.NomeParam)
                            returnValue = true;

                }

                if (returnValue == true) continue; // Passa pro próx método da classe

                returnValue = true;

                for (int i = 0; i < tsEntry.size(); i++) {
                    
                    if ( i > tsNodo.size() ) { if ( tsEntry.get(i).getSymbType() == SimbID.NomeParam ) break; }

                    TS_entry e = tsNodo.get(i);
                    if (e.getSymbType() == SimbID.NomeParam) {

                        if (e.getTipo() != tsEntry.get(i).getTipo()) continue;
                        else returnValue = false;
                    
                    } else {

                        if ( tsEntry.get(i).getSymbType() == SimbID.NomeParam ) break;
                        else if (!returnValue) return false;
                             else break;
                        
                    }

                }
            }
        }

        return true;
    }

    public  ArrayList<TS_entry> getLista() {    return lista;   }

    public ArrayList<TS_entry> getMethods(String nome) { 

        ArrayList<TS_entry> lista = new ArrayList<>();

        for ( TS_entry e : lista ) if (e.getId() == nome && e.getSymbType() == SimbID.NomeFuncao) lista.add(e);

        return lista;
    }
}



