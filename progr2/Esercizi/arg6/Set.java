package arg6;

public class Set<T> {

    private Node<T> first = null;
    private int size = 0;

    public int size() { return size; }

    public boolean empty() { return size == 0; }

    public void add(T elem) {
        // In un insieme un elemento può comparire massimo una volta.
        // Per cui controllo che l'elemento non sia già presente
        if(!contains(elem)) {
            first = new Node<T>(elem, first);
            size++;
        }
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

    public boolean contains(T elem) {
        for(Node<T> p = first; p != null; p = p.getNext())
            if(p.getElem().equals(elem)) return true;
        return false;
    }

    public String toString() {
        String ret = "";
        int i = 0;

        for(Node<T> p = first; p != null; p = p.getNext())
            ret += "["+ i++ + "|" + p.getElem().toString() + "] -> ";

        return ret.substring(0, ret.length() - 3);
    }

    public static void main(String[] args) {
        Set<Integer> sInt = new Set<Integer>();
        sInt.add(3);
        sInt.add(5);
        sInt.add(3);
        sInt.add(6);
        sInt.remove(3);

        System.out.println(sInt);
    }
}