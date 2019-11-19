class Test {
    public static void main(String[] args) {
        int n, a, b;
        String s;
        char c;
        /* System.out.print("Inserire il numero di cui si vuole calcolare il fattoriale: ");
        n = SIn.readInt();
        System.out.println("Il fattoriale di " + n + " vale: " + Fattoriale.fattorialeRec(n)); */
        
        /*System.out.print("Inserire la base di cui si vuole calcolare il potenza: ");
        a = SIn.readInt();
        System.out.print("Inserire l'esponente di cui si vuole calcolare il potenza: ");
        b = SIn.readInt();
        System.out.println("Il potenza di " + a + "^" + b + " vale: " + Potenza.potenzaRec(a,b));*/

        /*System.out.print("Inserire il numero di cui si vuole calcolare la sommatoria 0 + 1 + ... + n: ");
        n = SIn.readInt();
        System.out.println("La sommatoria di " + n + " vale: " + Sommatoria.sommatoriaRec(n));*/

        /*System.out.println("Il valore minimo tra quelli inseriti e': " + Minimo.minimoRec());*/

        System.out.print("Inserire la stringa nella quale si vuole cercare il carattere: ");
        s = SIn.readLine();
        System.out.print("Inserire il carattere che si vuole ricercare nella stringa: ");
        c = SIn.readChar();

        System.out.println("Il numero di occorrenze del carattere " + c + " nella stringa " + s + " e': " + Stringhe.occorrenzeCarattereRec(s, c));

        /*System.out.println("Inserire la stringa che si vuole invertire: ");
        s = SIn.readLine();

        System.out.print("La stringa invertita e': " + Stringhe.inversaRec(s) + " - " + Stringhe.inversaIt(s));*/
    }
}