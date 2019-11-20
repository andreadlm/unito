package arg1;

import libs.SIn;

class Somma {
    public static void main(String[] args) {
        System.out.print("Inserire il primo numero : ");
        int n = SIn.readInt();
        System.out.print("Inserire il secondo numero : ");
        int m = SIn.readInt();
        
        int out = n + m;

        System.out.printf("%d + %d = %d", n, m, out);
    }
}