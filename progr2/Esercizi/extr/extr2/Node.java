package extr.extr2;

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
    
    public Node neg() {
        if(next == null) return elem < 0 ? null : this;
        if(elem < 0) return next.neg();
        else next = next.neg(); return this;
    }


    public Node inv(Node p, Node q) {
        if(p == null) return q;
        return inv(p.next, new Node(p.elem, q));
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
        while(first != null && first.getElem() < 0) first = first.getNext();

        Node n = first, p = first;
        while(n != null && n.getNext() != null) {
            if(n.getNext().getElem() < 0) {
                n = n.getNext();
                p.setNext(n.getNext());
            } else n = p = n.getNext();
        }
    }
}

