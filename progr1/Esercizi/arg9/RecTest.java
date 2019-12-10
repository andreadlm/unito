package Esercizi.arg9;

import Esercizi.arg7.ArrUtil;
import Esercizi.libs.SIn;

public class RecTest {
    public static void main(String[] args) {
        int x;
        int[] arr;

        arr = ArrUtil.initArray();

        System.out.print("Inserire l'intero da ricercare: ");
        x = SIn.readLineInt();

        /*System.out.println("La prima occorrenza dell'intero " + x + " nel vettore e' in posizione " + Rec.indice(arr, x));*/

        System.out.println("L'intero " + x + " compare " + Rec.conteggio(arr, x) + " volte nel vettore");
    }
}