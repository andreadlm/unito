package Esercizi.arg7;

import Esercizi.libs.SIn;

class ArrTest {
    public static void main(String[] args) {
        int[] arr1, arr2;
        int x, y, l;
        arr1 = ArrUtil.initArray();

        ArrUtil.stampa(arr1);
        arr2 = ArrUtil.initArray();
        System.out.println("I due array sono " + ((ArrUtil.equals(arr1, arr2)) ? "uguali" : "diversi"));

        arr2 = ArrUtil.arrayClone(arr1);
        System.out.println("I due array sono " + ((ArrUtil.equals(arr1, arr2)) ? "uguali" : "diversi"));


        System.out.print("Inserire il numero di cui ricercare il maggiore: ");
        x = SIn.readLineInt();
        System.out.print("Nel vettore " + ((ArrUtil.esisteMaggiore(arr1, x)) ? "" : "non ") + "esisite un numero maggiore di " + x);

        System.out.print("Inserire il numero x: ");
        x = SIn.readLineInt();

        System.out.print("Inserire il numero y: ");
        y = SIn.readLineInt();

        System.out.println("\nNel vettore i numeri " + (ArrUtil.tuttiMaggioriOMinori(arr1, x, y) ? "" : "non ") + "sono tutti minori di " + x + " o maggiori di " + y);

        System.out.print("Inserire il limite l: ");
        l = SIn.readLineInt();

        ArrUtil.stampa(ArrUtil.filtroMinoriDi(arr1, l));
    }
}