package extr.extr2;

/* 
 * Implementare un metodo 'modifica' che aggiunge in fondo a una
 * lista la somma di tutti gli elementi positivi della lista.
 *
 * Ecco alcuni esempi di liste prima e dopo l'esecuzione del metodo:
 *
 * prima :
 * dopo  : 0
 *
 * prima : 5
 * dopo  : 5, 5
 *
 * prima : 5, -3, 1
 * dopo  : 5, -3, 1, 6
 *
 * prima : -1, -1, -6
 * dopo  : -1, -1, -6, 0
 *
 * NON E` CONSENTITO:
 * - usare break o continue all'interno di cicli
 * - usare strutture dati ausiliarie (ad esempio array)
 * - modificare il codice al di fuori dal metodo da implementare
 *
 */

class Node {
    private int elem;
    private Node next;

    public Node(int elem, Node next) {
        this.elem = elem;
        this.next = next;
    }

    public int getElem() { 
        return elem; 
    }

    public void setElem(int elem) { 
        this.elem = elem; 
    }

    public Node getNext() { 
        return next; 
    }

    public void setNext(Node next) {
        this.next = next; 
    }
}

class List {
    private Node first;

    public List() { 
        first = null;
    }

    public void insertFirst(int elem) { 
        first = new Node(elem, first); 
    }

    public String toString() {
        String s = "";
        for (Node p = first; p != null; p = p.getNext()) {
            if (p != first) s += ", ";
            s += p.getElem();
	    }
	    return s;
    }

    public void modifica() {
        int s = 0;
        Node n = first;

        if(n != null) {
            while(n.getNext() != null) {
                s = s + (n.getElem() > 0 ? n.getElem() : 0);
                n = n.getNext();
            }

            n.setNext(new Node(s + (n.getElem() > 0 ? n.getElem() : 0), null));
        }
    }

    public void elimina() {
        Node n = first, p = first;

        while(n != null && n.getNext() != null)
            if(n.getElem() > n.getNext().getElem()) {
                p.setNext(n.getNext().getNext());
                n = n.getNext();
            } else n = p = n.getNext();
    }

    public void inversioni() {
        Node n = first;
        int c = 0;

        while(n != null && n.getNext() != null) {
            if(n.getElem() > n.getNext().getElem()) c++;
            n = n.getNext();
        }

        if(n != null) n.setNext(new Node(c, null));
    }

    public void neg() {        
        while(first != null && first.getElem() < 0) 
            first = first.getNext();

        Node n = first;
        while(n != null && n.getNext() != null) {
            if(n.getNext().getElem() < 0) n.setNext(n.getNext().getNext());
            n = n.getNext();
        }
    }
}

