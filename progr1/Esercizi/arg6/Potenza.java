package arg6;

public class Potenza {
    /** a^b */
    public static int potenzaRec(int a, int b) {
        if(b == 0) return 1;
        return a * potenzaRec(a, b-1);
    }
}