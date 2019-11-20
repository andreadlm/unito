package arg1;

import libs.SIn;

public class Ordine {
    public static void main(String[] args) {
        System.out.print("Inserire il valore di v1: ");
        int v1 = SIn.readInt();

        System.out.print("Inserire il valore di v2: ");
        int v2 = SIn.readInt();

        System.out.print("Inserire il valore di v3: ");
        int v3 = SIn.readInt();

        int tmp;

        if(v1 > v2) {
            tmp = v1;
            v1 = v2;
            v2 = tmp;
        }

        if(v2 > v3) {
            tmp = v2;
            v2 = v3;
            v3 = tmp;
        }

        if(v1 > v2) {
            tmp = v1;
            v1 = v2;
            v2 = tmp;
        }

        System.out.println("[v1 = " + v1 + "] [v2 = " + v2 + "] [v3 = " + v3 + "]");

    }
}