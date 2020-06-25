package arg3;

// Classe fornita per consegna, modificata aggiungendo
// le implementazioni richieste

// Rubrica.java
public  class Rubrica { 
    /** Invariante: 
      (i) una volta costruita, una rubrica non contiene lo stesso nome due volte, 
      (ii) (0<=numContatti <= lunghezza vettore contatti) */

    // numero dei contatti attualmente memorizzati nella rubrica
    private int numContatti;        // all'inizio parte a 0
    // array pre-allocato di contatti
    private Contatto[] contatti;    // all'inizio vale null

    public Rubrica(int maxContatti) {
        // costruisce una rubrica con max. num. di contatti  = maxContatti
        numContatti = 0;
        //all'inizio i contatti significativi nella rubrica sono 0
        contatti =  new Contatto[maxContatti];
        /** all'inizio tutti i contatti nella rubrica non sono significativi:
        hanno nome e email uguali a null */
    }
    // La nuova rubrica costruita soddisfa l'invariante

    public  int getNumContatti() {
        return numContatti;
    }
    /** NOTA: non forniamo un metodo get per ottenere il vettore dei contatti:
    conoscendolo, un'altra classe potrebbe leggere e modificare i
    contatti in modo errato (in contraddizione con l’invariante) */

    public void scriviOutput() {
        int i = 0;
        System.out.println("Num. contatti = " + numContatti);
        //Stampiamo i contatti di indice da 0 fino a numContatti-1.
        //Gli altri contatti sono privi di significato
        while(i < contatti.length) { 
            if(contatti[i] != null) contatti[i].scriviOutput();
            else System.out.println(" -");
            ++i;
        }
    }

    /** Il metodo cercaIndice(n) restituisce l'unico indice i di un
    contatto di nome n se c'e', restituisce numContatti se non c'e'. Il
    metodo cercaIndice(n) e' privato per evitare che le altre classi
    modifichino un contatto in contraddizione con l’invariante */
    private int cercaIndice(String n) {
        return cercaIndiceDic(n, 0, numContatti - 1);
    }

    private int cercaIndiceDic(String n, int s, int e) {
        if(s > e) return numContatti;

        if(s == e) {
            if(contatti[s].getNome().equals(n)) return s;
            return numContatti;
        }

        int p1 = cercaIndiceDic(n, s, (s + e)/2);
        int p2 = cercaIndiceDic(n, ((s + e) / 2) + 1, e);
        if(p1 != numContatti) return p1;
        if(p2 != numContatti) return p2;

        return numContatti;
    }

    /** usando cercaIndice(n) possiamo stabilire se il nome n e' presente
    nella rubrica */
    public boolean presente(String n) {
        return (cercaIndice(n) < numContatti);
    }

    /** usando cercaIndice(n) possiamo trovare quale e-mail corrisponde a
    un nome presente nella rubrica (restituiamo "" per nome assente) */
    public String cercaEmail(String n) {
        int i= cercaIndice(n);
        if (i<numContatti) 
            return contatti[i].getEmail(); 
        else return "";
    }

    /** controlliamo se una rubrica e' piena, cioe' se il numContatti e'
    uguale al numero di elementi che possiamo inserire nel vettore
    contatti */
    public boolean piena() {
        return (numContatti == contatti.length);
    }

    // Aggiungi un nuovo contatto. Se non e' possibile, ritorna false
    public boolean aggiungi(String n, String e) {
        if (presente(n)) 
            return false;      // rubrica contiene n: fallimento
        if (piena()) estendi();

        // aggiungo il contatto nella posizione corretta
        // secondo l'ordine lessicografico dei nomi
        int p; // posizione di inserimento
        for(p = 0; p < numContatti && n.compareTo(contatti[p].getNome()) > 0; p++) {}
        for(int i = numContatti - 1; i >= p; i--) contatti[i + 1] = contatti[i];
        contatti[p] = new Contatto(n, e);

        numContatti++;

        return true; //successo
    }

    private void estendi() {
        if(contatti.length == 0) contatti = new Contatto[1];
        else {
            Contatto[] tmp = new Contatto[contatti.length * 2];

            for(int i = 0; i < contatti.length; i++)
                tmp[i] = contatti[i];
    
            contatti = tmp;
        }

        assert numContatti <= contatti.length && numContatti >= 0;
    }

    // Rimuovi un contatto di nome @n, e riposiziona tutti i
    // contatti che seguono per ricompattare l'array.
    public boolean rimuovi(String n) {
        int i = cercaIndice(n);
        if (i == numContatti) 
            return false; // il contatto manca: fallimento

        //se invece il contatto c'e': diminuiamo di uno i contatti
        --numContatti;
        // spostiamo tutti i contatti indietro di uno sovrascrivendo 
        // il contatto numero i con il successore
        while (i < numContatti) {
            contatti[i] = contatti[i+1]; 
            ++i;
        }

        return true; //successo
    }

    // cerco un contatto di nome n e se lo trovo cambio il nome a n2
    public boolean cambiaNome(String n, String n2) {
        int i = cercaIndice(n), j = cercaIndice(n2);

        //contatto di nome n non trovato oppure di nome n2 trovato:fallimento
        if ((i == numContatti) || (j<numContatti)) 
            return false;

        //Altrimenti cambiamo il nome del contatto i da n a n2
        contatti[i].setNome(n2);
        return true;
    }

    // cerco un contatto di nome n e se lo trovo cambio la email a e2
    public boolean cambiaEmail(String n, String e2) {
        int i = cercaIndice(n);
        if (i == numContatti) 
            return false; //contatto di nome n non trovato: fallimento
        //se n e' presente modifichiamo la email
        contatti[i].setEmail(e2);
        return true;
    }
}

