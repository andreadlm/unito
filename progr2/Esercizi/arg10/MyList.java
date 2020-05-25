package arg10;

import libs.Node;

// Classe fornita dall'esercizio, aggiunto il metodo
// modifica()

// MyList.java
public class MyList {
    private Node<Integer> first; // Riferimento al primo nodo della lista

    public MyList() {
        this.first = null;
    }

    public void insert(int elem) {
        first = new Node<Integer>(elem, first);
    }

    public String toString() {
        String res = "";
        for (Node<Integer> p = first; p != null; p = p.getNext()) {
            res += p.getElem();
            if (p.getNext() != null) res += ", ";
        }
        return res;
    }

    public void modifica() {
        modificaRic(first, 0);
    }

    private void modificaRic(Node<Integer> n, int s) {
        if(n != null) {
            n.setElem(n.getElem() + s);
            modificaRic(n.getNext(), n.getElem());
        }
    }

    public void pushSomma() {
        first = new Node<Integer>(sommaPositivi(first), first);
    }

    private int sommaPositivi(Node<Integer> n) {
        if(n == null) return 0;
        return (n.getElem() > 0) ? n.getElem() + sommaPositivi(n.getNext()) : sommaPositivi(n.getNext());
    }

    public static void main(String[] args) {
        MyList list1 = new MyList();
        MyList list2 = new MyList();
        
        list1.insert(4);
        list1.insert(0);
        list1.insert(1);
        list1.insert(-1);
        list1.modifica();

        System.out.println(list1);

        list2.insert(-4);
        list2.insert(-6);
        list2.insert(7);
        list2.insert(4);
        list2.insert(-2);
        list2.insert(2);
        list2.pushSomma();

        System.out.println(list2);

    }
}
