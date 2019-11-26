package Esercizi.arg7;

import Esercizi.libs.SIn;

public class ArrUtil {
    public static int[] initArray() {
        int n;
        System.out.print("Inserire la dimensione dell'array: ");
        n = SIn.readLineInt();

        int[] arr = new int[n];

        for(int i = 0; i < n; i++) {
            System.out.print("Inserire l'elemento numero " + i + " dell'array: ");
            arr[i] = SIn.readLineInt();
        }

        return arr;
    }

    public static void stampa(int[] arr) {
        for(int i = 0; i < arr.length; i++) 
            System.out.print(arr[i] + " ");
    }

    public static boolean equals(int[] arr1, int[] arr2) {
        boolean eql = true;

        if(arr1.length != arr2.length) return false;

        for(int i = 0; eql && i < arr1.length; i++)
            if(arr1[i] != arr2[i]) eql = false;
        
        return eql;
    }

    public static int[] arrayClone(int[] arr) {
        int[] clone = new int[arr.length];

        for(int i = 0; i < arr.length; i++) clone[i] = arr[i];

        return clone;
    }

    public static boolean esisteMaggiore(int[] arr, int x) {
        boolean ext = false;
        
        for(int i = 0; !ext && i < arr.length; i++)
            if(arr[i] > x) ext = true;

        return ext;
    }

    // tutti min x o magg y
    public static boolean tuttiMaggioriOMinori(int[] arr, int x, int y) {
        boolean min = true;
        boolean max = true;

        for(int i = 0; (min || max) && i < arr.length; i++) {
            if(arr[i] >= x) min = false;
            if(arr[i] <= y) max = false;
        }

        return min || max;
    }

    public static int[] filtroMinoriDi(int[] arr, int l) {
        int c = 0;
        int[] ret;

        for(int i = 0; i < arr.length; i++)
            if(arr[i] < l) c++;

        ret = new int[c];

        for(int i = 0; i < arr.length; i++)
            if(arr[i] < l) ret[i] = arr[i];
        
        return ret;
    }
}