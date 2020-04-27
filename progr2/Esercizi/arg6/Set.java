package arg6;

import libs.Node;

public class Set<T> {

    private Node<T> first = null;
    private int size = 0;

    public int size() { return size; }

    public boolean empty() { return size == 0; }

    public void add(T elem) {
        if(elem != null && !contains(elem)) {
            first = new Node<T>(elem, first);
            size++;
        }
    }

    public Set<T> union(Set<T> s) {
        Set<T> ret = new Set<T>();

        if(s != null)
            for(Node<T> tmp = s.first; tmp != null; tmp = tmp.getNext()) 
                ret.add(tmp.getElem());
        for(Node<T> tmp = this.first; tmp != null; tmp = tmp.getNext()) 
            ret.add(tmp.getElem());

        return ret;
    }

    public Set<T> intersection(Set<T> s) {
        if(s == null) return null;

        Set<T> ret = new Set<T>();

        for(Node<T> tmp = first; tmp != null; tmp = tmp.getNext()) 
            if(s.contains(tmp.getElem())) ret.add(tmp.getElem());

        return ret;
    }

    public boolean subsetOf(Set<T> s) {
        if(s == null) return false;

        for(Node<T> tmp = first; tmp != null; tmp = tmp.getNext())
            if(!s.contains(tmp.getElem())) return false;
        return true;
    }

    // TODO: da rivedere
    public boolean remove(T elem) {
        if(first == null) return false;

        if(first.getElem().equals(elem)) first = first.getNext();

        for(Node<T> p = first; p.getNext() != null; p = p.getNext())
            if(p.getNext().getElem().equals(elem)) {
                p.setNext(p.getNext().getNext());
                size--;
                return true;
            }

        return false;
    }

    public boolean equals(Set<T> s) {
        return this.subsetOf(s) && s.subsetOf(this);
    }

    public boolean contains(T elem) {
        if(elem == null) return false;

        for(Node<T> p = first; p != null; p = p.getNext())
            if(p.getElem().equals(elem)) return true;
            
        return false;
    }

    public String toString() {
        String ret = "";

        for(Node<T> p = first; p != null; p = p.getNext())
            ret += "[" + p.getElem().toString() + "] -> ";

        return ":" + size + ": " + ret.substring(0, ret.length() - 3);
    }
}