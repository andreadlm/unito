package arg5;

// Classe data per consegna

// Pila di dimensione variable di interi, 
// realizzata come lista concatenata di Nodi
public class PilaDinamica {
    // Riferimento al nodo contenente l'elemento in cima alla pila
    // Invariante: top==null se la pila e' vuota, top != null altrimenti.
    private Node top;

    // Costruttore: crea una pila vuota
    public PilaDinamica() {
        this.top = null;
    }

    // Ritorna true se la pila e' vuota
    public boolean vuota() {
        return this.top == null;
    }

    // Ritorna true se la pila e' piena. 
    // Poiche' la pila ha dimensione dinamica, non puo' mai 
    // essere piena, a meno di finire la memoria
    public boolean piena() {
        return false;
    }

    // Aggiungi l'intero   x in cima alla pila
    public void push(int x) {
        top = new Node(x, top);
    }

    // Elimina l'elemento in cima alla pila, e ritornane il valore.
    public int pop() {
        assert !vuota() : "non si puo' chiamare il metodo pop() se la pila e' vuota.";
        int tmp = top.getElem();
        top = top.getNext();
        return tmp;
    }

    // Ritorna un array contenente tutti gli elementi nella pila
    // nell'ordine nel quale compaiono
    public int[] toArray() {
        int len = nodesNum();
        int[] ret = new int[len];

        Node curr = top;
        for(int i = 0; curr != null && i < len; i++) {
            ret[i] = curr.getElem();
            curr = curr.getNext();
        }

        return ret;
    }

    // Ritorna il numero di nodi all'interno della lista
    private int nodesNum() {
        return nodesNumRec(top);
    }

    private int nodesNumRec(Node n) {
        if(n != null) 
            return 1 + nodesNumRec(n.getNext());
        return 0;
    }
}