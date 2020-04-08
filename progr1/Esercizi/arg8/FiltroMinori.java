package arg8;

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

    /*private static int[] filtroMinoriDiDic(int[] arr, int x, int l, int r) {
        if(r == l) return new int[0];
        if(r == l + 1) 
            if(arr[l] < x) return new int[]{arr[l]};
            else return new int[0];
        
        int[] tmp1 = filtroMinoriDiDic(arr, x, l, (l + r) / 2);
        int[] tmp2 = filtroMinoriDiDic(arr, x, (l + r) / 2, r);

        int[] tmp3 = new int[tmp1.length + tmp2.length];
        for(int i = 0; i < tmp1.length; i++) tmp3[i] = tmp1[i];
        for(int i = 0; i < tmp2.length; i++) tmp3[tmp1.length - 1 + i] = tmp2[i];

        return tmp3;
    }*/

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