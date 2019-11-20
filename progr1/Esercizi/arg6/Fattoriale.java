package arg6;

public class Fattoriale {
    public static int fattorialeRec(int n) {
        if(n == 0) return 1;
        return n * fattorialeRec(n-1);
    }
}