package arg6;

public class Sommatoria {
    public static int sommatoriaRec(int n) {
        if(n == 0) return n;
        return n + sommatoriaRec(n - 1);
    }
}