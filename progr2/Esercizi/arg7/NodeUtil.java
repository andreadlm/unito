package arg7;

import libs.Node;

public class NodeUtil {

    public static <T> int size(Node<T> p) {
        if(p == null) return 0;
        return 1 + size(p.getNext());
    }

    public static <T> int occurrences(Node<T> p, T x) {
        if(p ==  null) return 0;
        return (p.getElem().equals(x) ? 1 : 0) +  occurrences(p.getNext(), x);
    }

    private static <T> Node<T> sameElem(Node<T> p, T x) {
        if(p == null) return null;
        if(p.getElem().equals(x)) return p;
        return sameElem(p.getNext(), x);
    }

    // Versione ricorsiva
    public static <T> boolean included(Node<T> p, Node<T> q) {
        if(p == null) return true;

        Node<T> elem = sameElem(q, p.getElem());

        if(elem != null) return included(p.getNext(), elem);
        else return false;
    }

    // Versione iterativa
    public static <T> boolean includedIt(Node<T> p, Node<T> q) {
        while(p != null && q != null) {
            while(p.getElem() != q.getElem()) q = q.getNext();
            p = p.getNext();
        }

        return p == null ? true : false;
    }

    public static <T> void printList(Node<T> p) {
        if(p != null) {
            System.out.print("[" + p.getElem() + "]");
            printList(p.getNext());
        } else {
            System.out.println("\n");
        }
    }

    // Versione ricorsiva
    public static <T> Node<T> reverse(Node<T> p) {
        return reverse(p, null);
    }

    private static <T> Node<T> reverse(Node<T> curr, Node<T> prev) {
        if(curr.getNext() != null)
             return reverse(curr.getNext(), new Node<T>(curr.getElem(), prev));
         else
             return new Node<T>(curr.getElem(), prev);
    }

    // Versione iterativa
    public static <T> Node<T> reverseIt(Node<T> p) {
        Node<T> ret = null;

        for(ret = null; p != null; p = p.getNext()) 
            ret = new Node<T>(p.getElem(), ret);

        return ret;
    }

    public static <T> Node<Integer> cardinality(Node<Node<T>> p) {
        if(p == null) return null;
        return new Node<Integer>(size(p.getElem()), cardinality(p.getNext()));
    }
}