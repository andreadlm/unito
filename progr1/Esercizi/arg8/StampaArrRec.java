package Esercizi.arg8;

public class StampaArrRec {
    public static void stampaArr(int[] arr) {
        stampaArrCon(arr, 0);
    }

    /*private static void stampaArrCo(int[] arr, int i) {
        if(i == 0) System.out.print("[" + arr[i] + "]");
        else {
            stampaArrCo(arr, i - 1);
            System.out.print("[" + arr[i] + "]");
        }
    }*/

    private static void stampaArrCon(int[] arr, int i) {
        if(i == arr.length - 1) System.out.print("[" + arr[i] + "]");
        else {
            System.out.print("[" + arr[i] + "]");
            stampaArrCon(arr, i + 1);
        }
    }

}