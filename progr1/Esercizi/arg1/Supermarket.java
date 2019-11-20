package arg1;

import libs.SIn;

/*Esercizio 1:

calcolare il prezzo di quanto acquistiamo al supermercato:
3 pacchi di orecchiette
2 kg di pomodori

prezzo di un pacco di orecchiette: euro 1.50
prezzo al kg dei pomodori: euro 2.50

Scrivere una classe SuperMarket il cui
metodo main calcoli e visualizzi a video
la spesa totale.

Modellare il problema utilizzando delle variabili opportunamente dichiarate con tipi adeguati.
Utilizzare una variabile anche per rappresentare il totale.
Indentare e commentare opportunamente il programma.

Infine, fare inserire le quantità degli articoli alimentari dall'utente da tastiera (anziche' fissare
nel codice del programma i valori delle variabili).*/

public class Supermarket {
    public static void main(String[] args) {

        // Dichiarazione costanti prezzi
        final float P_ORECHHIETTE = 1.50f;
        final float P_POMODORI = 2.50f;

        // Input quantità acquistate
        System.out.println("Inserire il numero di pacchi di orecchiette acqusitati : ");
        int orecchiette = SIn.readInt();
        System.out.println("Inserire il numero di chili di pomodori acqusitati : ");
        int pomodori = SIn.readInt();

        // Calcolo del totale e output
        float tot = orecchiette * P_ORECHHIETTE + pomodori * P_POMODORI;
        System.out.println("Il totale speso e' : " + tot);
    }
}