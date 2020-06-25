package esercitazioni.eserc1;

import libs.Node;

// Classe fornita per consegna, modificata con l'implementazione
// richiesta

public class NodeSimulazione {
    // Metodo statico di conversione a stringa, perche' siamo fuori di Node
    public static String toString(Node<Integer> p) {
        if (p==null) 
            return ""; 
        else 
            return p.getElem() + "\t" + toString(p.getNext());
    }

    public static int contaMinoriUguali(Node<Integer> p, Node<Integer> q) {     
        //Inserite qui il codice richiesto, cancellando prima "return 0;"
        if(p == null || q == null) return 0;
        return contaMinoriUguali(p.getNext(), q.getNext()) + ((p.getElem() > q.getElem()) ? 1 : 0);
    }

    // verifica che il valore @trovato corrisponda con quello @atteso
    public static void check(String espr, int trovato, int atteso) {
        assert trovato==atteso: 
                espr + " vale " + trovato + " valore atteso " + atteso; 
        System.out.println("OK " + espr + "\t=\t" + atteso);
    }

    // metodo di test per contaMinoriUguali
    public static void main(String[] args) {
        Node<Integer> p=new Node<Integer>(1,new Node<Integer>(2,new Node<Integer>(3,null)));
        Node<Integer> q=new Node<Integer>(3,new Node<Integer>(2,new Node<Integer>(1,null)));
        Node<Integer> r=new Node<Integer>(1,new Node<Integer>(1,new Node<Integer>(1,null)));
        Node<Integer> s=new Node<Integer>(10,new Node<Integer>(20,null));

        System.out.println("p\t=\t" + toString(p));
        System.out.println("q\t=\t" + toString(q));
        System.out.println("r\t=\t"  + toString(r));
        System.out.println("s\t=\t"  + toString(s) + "\n");

        check("contaMinoriUguali(s,p)",contaMinoriUguali(s,p),0);
        check("contaMinoriUguali(p,r)",contaMinoriUguali(p,r),1);
        check("contaMinoriUguali(p,q)",contaMinoriUguali(p,q),2);
        check("contaMinoriUguali(p,s)",contaMinoriUguali(p,s),2); 
        check("contaMinoriUguali(r,p)",contaMinoriUguali(r,p),3);
        check("contaMinoriUguali(p,p)",contaMinoriUguali(p,p),3);
        check("contaMinoriUguali(p,null)",contaMinoriUguali(p,null),0);
        check("contaMinoriUguali(null,q)",contaMinoriUguali(null,q),0);
        check("contaMinoriUguali(null,null)",contaMinoriUguali(null,null),0); 

        System.out.println("\nOK: Tutti i valori trovati sono i valori attesi");
    }
}