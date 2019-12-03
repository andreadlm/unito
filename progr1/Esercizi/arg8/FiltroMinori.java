package Esercizi.arg8;

public class FiltroMinori {

    // prende solo i valori minori di x
    public static int[] filtroMinoriDi(int[] arr, int x) {
        return filtroMinoriDiCo(arr, x, arr.length - 1);
    }

    private static int[] filtroMinoriDiCo(int[] arr, int x, int i) {
        if(i == 0) {
            if(arr[i] < x) return new int[]{arr[i]};
            else return new int[0];
        } else {
            int ret[];
            int[] tmp = filtroMinoriDiCo(arr, x, i - 1);

            if(arr[i] < x) {
                ret = new int[tmp.length + 1];
                int j;
                for(j = 0; j < tmp.length; j++) ret[j] = tmp[j];
                ret[j] = arr[i];
            } else {
                ret = tmp;
            }

            return ret;
        }
    }

    /*private static int[] filtroMinoriDiCon(int[] arr, int x, int i) {
        if(i == arr.length - 1) {
            if(arr[i] < x) return new int[]{arr[i]};
            else return new int[0];
        } else {
            int ret[];
            int[] tmp = filtroMinoriDiCon(arr, x, i + 1);

            if(arr[i] < x) {
                ret = new int[tmp.length + 1];
                int j;
                for(j = 0; j < tmp.length; j++) ret[j] = tmp[j];
                ret[j] = arr[i];
            } else {
                ret = tmp;
            }

            return ret;
        }
    }*/

}