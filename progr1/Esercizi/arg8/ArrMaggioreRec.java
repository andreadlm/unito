package arg8;

public class ArrMaggioreRec {

    public static boolean esisteMaggiore(int[] arr, int x) {
        if(arr != null && arr.length > 0)
            return esisteMaggioreCon(arr, x, 0);
        else
            return false;
    }

    /*private static boolean esisteMaggioreCo(int[] arr, int x,int i) {
        if(i == 0) return arr[i] > x;
        return arr[i] > x || esisteMaggioreCo(arr, x, i - 1);
    }*/

    private static boolean esisteMaggioreCon(int[] arr, int x,int i) {
        if(i == arr.length - 1) return arr[i] > x;
        return arr[i] > x || esisteMaggioreCon(arr, x, i + 1);
    }

}