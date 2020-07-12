package extr.extr3;

public class Lists {
    public static <T> void truncate(Node<Node<T>> ll, int x){
        while(ll != null) {
            if(x == 0) ll.setElem(null);
            else {
                // Prelevo il nodo corrente
                Node<T> n = ll.getElem();

                int c = 0;
                while(n != null && c < x - 1) {
                    n = n.getNext();
                    c++;
                }

                if(n != null) n.setNext(null);
            }

            ll = ll.getNext();
        }
    }

    private static void removeLessThan(Node<Node<Integer>> ll, int x) {
        while(ll != null) {
            // Prelevo il nodo corrente
            Node<Integer> n = ll.getElem();
            // Scorro il nodo corrente
            while(n != null && n.getElem() < x) {
                ll.setElem(n.getNext());
                n = n.getNext();
            }

            while(n != null && n.getNext() != null) {
                if(n.getNext().getElem() < x) n.setNext(n.getNext().getNext());
                n = n.getNext();
            }

            ll = ll.getNext();
        }
    }

    private static void addOdd(Node<Node<Integer>> ll) {
        while(ll != null) {
            // Prelevo il nodo
            Node<Integer> n = ll.getElem();
            // Scorro il nodo
            while(n != null) {
                if(n.getElem() % 2 == 0) {
                    n.setNext(new Node<Integer>(n.getElem() + 1, n.getNext()));
                    n = n.getNext(); // Salto l'elaborazione del nodo sicuramente dispari
                }
                n = n.getNext();
            }

            ll = ll.getNext();
        }
    }

    public static Node<Node<Integer>> removeEmptyLists(Node<Node<Integer>> ll) {
        if(ll == null) return null;
        if(ll.getElem() == null) return removeEmptyLists(ll.getNext());
        else return new Node<Node<Integer>>(ll.getElem(), removeEmptyLists(ll.getNext()));
    }

    private static int totalNumberOfElementsInLists(Node<Node<Integer>> ll) {
        int s = 0;

        while(ll != null) {
            Node<Integer> l = ll.getElem();

            while(l != null) {
                s++;
                l = l.getNext();
            }

            ll = ll.getNext();
        }

        return s;
    }

    private static Node<Node<Integer>> addListOfMaxs(Node<Node<Integer>> ll) {
        Node<Integer> p = null;
        Node<Node<Integer>> tmp = ll;

        while(tmp != null) {
            Node<Integer> n = tmp.getElem();
            Integer max = Integer.MIN_VALUE;

            while(n != null) {
                if(n.getElem() > max) max = n.getElem();
                n = n.getNext();
            }

            if(max != Integer.MIN_VALUE) p = new Node<Integer>(max, p);

            tmp = tmp.getNext();
        }

        return ll == null ? ll : new Node<Node<Integer>>(p, ll);
    }
}

class Node<T>{
    private T elem;
    private Node<T> next;

    public Node(T elem, Node<T> next){
        this.elem=elem;
        this.next=next;
    }

    public T getElem(){
        return elem;
    }

    public Node<T> getNext(){
        return next;
    }

    public void setElem(T elem){
        this.elem=elem;
    }
    public void setNext(Node<T> next){
        this.next=next;
    }

    @Override
    public String toString() {
        String res = "[";
        res += elem==null?"[]":elem.toString();
        Node<T> p = next;
        while (p != null) {
            res += ","+((p.elem == null?"[]":p.elem.toString()));
            p = p.next;
        }   
        return res += "]";
    }  
}

class List<T extends Comparable<T>> {
    private Node<T> first;

    public List()
    { first = null; }

    public void insertFirst(T elem)
    { first = new Node<>(elem, first); }

    @Override
    public String toString() {
        String s = "";
        for (Node<T> p = first; p != null; p = p.getNext()) {
            if (p != first) s += ", ";
            s += p.getElem();
        }
        return s;
    }

    public void delete(T x) {
        while(first != null && first.getElem().equals(x)) first = first.getNext();

        Node<T> n = first;
        while(n != null && n.getNext() != null) {
            if(n.getNext().getElem().equals(x)) n.setNext(n.getNext().getNext());
            n = n.getNext();
        }
    }
}
