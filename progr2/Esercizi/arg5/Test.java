package arg5;

public class Test {
    public static void main(String[] args) {
        printList(nodeFromTo(-5, 1));

        Node l1 = new Node(1, new Node(2, new Node(3, new Node(4, null))));
        Node l2 = new Node(2, new Node(3, new Node(4, new Node(5, null))));
       
        printList(reverse(l1));

        printList(zipSum(l1, l2));

    }

    // Produce una lista contenente i numeri da m a n (estremi compresi)
    public static Node nodeFromTo(int m, int n) {
        if(m > n) return null;
        
        Node top = null;
        for(int i = n; i >= m; i--) top = new Node(i, top);

        return top;
    }

    // Ritorna il numero di occorrenze di un numero x
    // in una lista
    public static int occourences(Node n, int x) {
        if(n != null) 
            return (n.getElem() == x ? 1 : 0) + occourences(n.getNext(), x);
        return 0;
    }

    // Ritorna true se la lista p è contenuta nella lista q
    public static boolean included(Node p, Node q) {
        Node pTmp = p, qTmp = q;

        while(!pTmp.equals(qTmp)) qTmp = qTmp.getNext();

        while(pTmp != null && pTmp.equals(qTmp)) {
            pTmp = pTmp.getNext();
            qTmp = qTmp.getNext();
        }

        return pTmp == null;
    }

    // Ritorna una copia dell'inverso della lista passata come parametro
    public static Node reverse(Node p) {
        return reverse(p, null);
    }

    public static Node reverse(Node curr, Node prev) {
       if(curr.getNext() != null)
            return reverse(curr.getNext(), new Node(curr.getElem(), prev));
        else
            return new Node(curr.getElem(), prev);
    }

    // Date due liste produce una nuova lista i cui elementi sono la 
    // somma degli elementi delle liste parametro
    public static Node zipSum(Node p, Node q) {
        if(p != null && q != null)
            return new Node(p.getElem() + q.getElem(), zipSum(p.getNext(), q.getNext()));
        return null;
    }

    public static void printList(Node n) {
        if(n != null) {
            System.out.println(n.getElem());
            printList(n.getNext());
        }
    }
}