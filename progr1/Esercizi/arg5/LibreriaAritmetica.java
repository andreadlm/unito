package arg5;

/**
 * Libreria con operazioni aritmetiche definite tramite iterazioni di operazioni
 * piu' primitive.
 */
public class LibreriaAritmetica {

     public static int piu(int x, int y) {
        int r = x;
        int n = y;

        while (n > 0) {
            r++; // accumula il risultato in r
            n--;
        }

        return r;
    }

    public static int meno(int m, int s) { //m-s
        int r = m;
        int n = s;
        
        while(n > 0) {
            r--;
            n--;
        }

        return r;
    }

    public static int per(int x, int y) {
        int r = 0;
        int n = y;
        
        while(n > 0) {
            r = piu(r, x);
            n--;
        }

        return r;
    }

    public static int div(int d, int s) { //Dividendo, divisore
        int r = 0;
        int n = d;

        while(n >= s) {
            n = meno(n, s);
            r++;
        }

        return r;
    }

    public static int resto(int d, int s) { //Dividendo, divisore
        int r = d;

        while(r >= s) {
            r = meno(r, s);
        }

        return r;
    }

    public static int pot(int b, int e) { //Base, esponente
        int r = 1;

        while(e > 0) {
            r = per(r, b);
            e--;
        }

        return r;
    }
}