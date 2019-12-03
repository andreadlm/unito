package Esercizi.arg8;

public class ArrMaggioriMinoriRec {

    // tutti minori di x o tutti maggiori di y
    public static boolean tuttiMaggioriOMinori(int[] arr, int x, int y) {
        if(arr != null && arr.length > 0)
            return tuttiMaggioriCon(arr, y, 0) || tuttiMinoriCon(arr, x, 0);
        else
            return false;
    }

    /*private static boolean tuttiMaggioriCo(int[] arr, int y, int i) {
        if(i == 0) return arr[i] > y;
        return arr[i] > y && tuttiMaggioriCo(arr, y, i - 1);
    }

    private static boolean tuttiMinoriCo(int[] arr, int x, int i) {
        if(i == 0) return arr[i] < x;
        return arr[i] < x && tuttiMinoriCo(arr, x, i - 1);
    }*/

    private static boolean tuttiMaggioriCon(int[] arr, int y, int i) {
        if(i == arr.length - 1) return arr[i] > y;
        return arr[i] > y && tuttiMaggioriCon(arr, y, i + 1);
    }

    private static boolean tuttiMinoriCon(int[] arr, int x, int i) {
        if(i == arr.length - 1) return arr[i] < x;
        return arr[i] < x && tuttiMinoriCon(arr, x, i + 1);
    }

}