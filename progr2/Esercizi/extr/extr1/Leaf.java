package extr.extr1;

// Classe fornita per consegna, implementa 
// la classe Leaf per rappresentare alberi vuoti.
// Modificata con le implementazioni richieste

public class Leaf extends Tree {
    public Leaf() { }

    public boolean empty() {
	    return true;
    }

    public int max() {
        // e` illegale invocare l'operazione max sull'albero vuoto...
        assert false;
        // ...tuttavia il compilatore mi costringe a ritornare
        // comunque un valore perche' il metodo ha tipo di ritorno
        // int
        return 0;
    }

    public boolean contains(int x) {
	    return false;
    }

    public Tree insert(int x) {
	    return new Branch(x, this, this);
    }

    public Tree remove(int x) {
	    return this;
    }

    // realizziamo anche un metodo toString per stampare un albero
    // binario sul terminale
    public String toString() {
	    return "Leaf";
    }

    public int depth() {
	    return 0;
    }

    public int sum() {
        return 0;
    }

    public int size() {
        return 0;
    }

    public boolean contains(int x, int n) {
        return false;
    }

    public Tree filter_le(int x) {
        return this;
    }

    public int get(int i) {
        return -1;
    }
}
