package Esercizi.arg9;

public class Rec {
    public static int indice(int[] arr, int x) {
        return indiceCon(arr, x, 0);
    } 

    private static int indiceCon(int[] arr, int x, int i) {
        if(i == arr.length) return -1;
        if(arr[i] == x) return i;
        return indiceCon(arr, x, i+1);
    }

    public static int conteggio(int[] arr, int x) {
        return conteggioDic(arr, x, 0, arr.length - 1);
    }

    private static int conteggioDic(int[] arr, int x, int l, int r) {
        if(l == r) return (arr[l] == x) ? 1 : 0;
        return conteggioDic(arr, x, l, (l + r) / 2) + conteggioDic(arr, x, (l + r) / 2 + 1, r);
    }
}