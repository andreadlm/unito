package arg2;

import libs.SIn;

class EmersioneMassimo {
    public static void main(String[] args) {

        int a, b, c, d, tmp;

        System.out.print("Inserire il primo numero: ");
        a = SIn.readInt();

        System.out.print("Inserire il secondo numero: ");
        b = SIn.readInt();

        System.out.print("Inserire il terzo numero: ");
        c = SIn.readInt();

        System.out.print("Inserire il quarto numero: ");
        d = SIn.readInt();

        if(a > b) {
            tmp = a;
            a = b;
            b = tmp;
        }

        if(b > c) {
            tmp = b;
            b = c;
            c = tmp;
        }

        if(c > d) {
            tmp = c;
            c = d;
            d = tmp;
        }

        System.out.println("[" + a + "] [" + b + "] [" + c + "] [" + d + "]");
    }
}