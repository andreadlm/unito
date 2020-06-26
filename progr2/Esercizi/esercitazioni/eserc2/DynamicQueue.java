package esercitazioni.eserc2;

import libs.Node;

// Classe che gestisce una coda dinamica di nodi generici
public class DynamicQueue<T> {
    private Node<T> first;
    private Node<T> last;

    public DynamicQueue() { first = last = null; }

    @Override
    public String toString() {
        return toString(first);
    }

    private String toString(Node<T> node) {
        if(node != null) return toString(node.getNext()) + 
                                (node.getNext() != null ? " <-- " : "") + 
                                node.toString();
        return "";
    }

    public void enqueue(T elem) {
        Node<T> nl = new Node<T>(elem, null);
        if(empty()) first = last = nl;
        else {
            last.setNext(nl);
            last = nl;
        }
    }

    public T dequeue() {
        assert !empty();

        Node<T> tmp = first;
        first = first.getNext();
        
        if(size() == 0) last = first = null;

        return tmp.getElem();
    }

    public int size() {
        return size(first);
    }

    private int size(Node<T> node) {
        return (node == null) ? 0 : 1 + size(node.getNext());
    }

    public T front() {
        return first.getElem();
    }

    public boolean empty() { return first == null; }

    public boolean contains(T elem) {
        return contains(elem, first);
    }

    private boolean contains(T elem, Node<T> p) {
        if(p == null) return false;
        if(p.getElem() == elem) return true;
        return contains(elem, p.getNext());
    }
}