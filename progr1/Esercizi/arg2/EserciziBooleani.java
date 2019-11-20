package arg2;

import libs.SIn;

class EserciziBooleani {
    public static void main(String[] args) {

        int a, b, c, max;
        boolean b1, b2;

        // es1
        System.out.println("== ESERCIZIO 1 ==");

        System.out.print("Inserire il primo intero: ");
        a = SIn.readInt();
        System.out.print("Inserire il secondo intero: ");
        b = SIn.readInt();

        // Controllo se a è multiplo di b e stampo il risltato
        System.out.println(a % b == 0);

        // es2
        System.out.println("\n== ESERCIZIO 2 ==");

        System.out.print("Inserire il voto: ");
        a = SIn.readInt();

        // Controllo se è compreso tra 1 e 30 e stampo il risultato
        System.out.println(a >= 1 && a <= 30);

        // es3
        System.out.println("\n== ESERCIZIO 3 ==");

        System.out.print("Inserire il primo booleano: ");
        b1 = SIn.readLineBoolean();

        System.out.print("Inserire il secondo booleano: ");
        b2 = SIn.readLineBoolean();

        // Controllo se sono entrambi veri e stampo il risultato
        System.out.println(!b1 || !b2);

        // es4
        System.out.println("\n== ESERCIZIO 4 ==");

        System.out.print("Inserire il primo intero: ");
        a = SIn.readInt();
        System.out.print("Inserire il secondo intero: ");
        b = SIn.readInt();

        // Controllo il massimo tra i due e stampo il risultato
        if(a > b) System.out.println("Il numero massimo e' : " + a);
        else if(b > a) System.out.println("Il numero massimo e' " + b);
        else System.out.println("I due valori sono uguali");

        // es5
        System.out.println("\n== ESERCIZIO 5 ==");

        System.out.print("Inserire il primo intero: ");
        a = SIn.readInt();
        System.out.print("Inserire il secondo intero: ");
        b = SIn.readInt();
        System.out.print("Inserire il terzo intero: ");
        c = SIn.readInt();

        // Controllo il massimo tra i tre e stampo il risultato
        max = a;
        if(b > max) max = b;
        if(c > max) max = c;

        System.out.println("Il numero massimo e': " + max);

        // es6
        System.out.println("\n== ESECIZIO 6 ==");

        System.out.print("Inserire il primo intero: ");
        a = SIn.readInt();
        System.out.print("Inserire il secondo intero: ");
        b = SIn.readInt();
        System.out.print("Inserire il terzo intero: ");
        c = SIn.readInt();

        // Controllo che siano ordinati in ordine creascente e stampo il risultato
        System.out.println(a <= b && b <= c);
    }
}