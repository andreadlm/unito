package arg6;

import libs.SIn;

public class Minimo {
    public static int minimoRec() {
        System.out.print("Inserire un intero: ");
        int n = SIn.readInt();

        if(n == 0) return Integer.MAX_VALUE;
        
        int np = minimoRec();
        return n < np ? n : np;
    }
}