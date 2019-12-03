package Esercizi.arg8;

import Esercizi.libs.SIn;
import Esercizi.arg7.ArrUtil;

class RecTest{
    public static void main(String[] args) {
        int[] arr1;
        int x, y;
        arr1 = ArrUtil.initArray();

        System.out.println("Gli elementi nel vettore " + (ArrPariRec.isArrPari(arr1) ? "" : "non ") + "sono tutti pari");

        System.out.print("Inserire un intero: ");
        x = SIn.readLineInt();

        System.out.println((ArrMaggioreRec.esisteMaggiore(arr1, x) ? "Esiste almeno " : "Non esiste ") + "un elemento nel vettore maggiore di " + x);
    
        System.out.print("Inserire un intero: ");
        y = SIn.readLineInt();

        System.out.println((ArrMaggioriMinoriRec.tuttiMaggioriOMinori(arr1, x, y) ? "Tutti " : "Non tutti ") + "gli elementi sono minori di " + x + " o maggiori di " + y);

        System.out.print("Vettore filtrato su " + x + ": ");
        ArrUtil.stampa(FiltroMinori.filtroMinoriDi(arr1, x));

        StampaArrRec.stampaArr(arr1);
    
    }
}