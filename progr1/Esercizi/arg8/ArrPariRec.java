package Esercizi.arg8;

public class ArrPariRec {

    public static boolean isArrPari(int[] arr) {
        if(arr != null && arr.length > 0)
            return isArrPariCon(arr, 0);
        else
            return false;
    }

    /*private static boolean isArrPariCo(int[] arr, int i) {
        if(i == 0) return arr[i] % 2 == 0;
        return (arr[i] % 2 == 0) && isArrPariCo(arr, i - 1);
    }*/

    private static boolean isArrPariCon(int[] arr, int i) {
        if(i == arr.length - 1) return arr[i] % 2 == 0;
        return (arr[i] % 2 == 0) && isArrPariCon(arr, i + 1);
    }
}