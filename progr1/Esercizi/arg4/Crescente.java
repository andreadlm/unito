package arg4;

import libs.SIn;

class Crescente {
    public static void main(String[] args) {
        int c, prec = Integer.MIN_VALUE;
        boolean seq = true;

        System.out.print("Inserire una squenza di interi terminata da 0: ");

        c = SIn.readInt();
        while(seq && c != 0) {
            if(c < prec) seq = false;
            else prec = c;

            c = SIn.readInt();
        }

        System.out.print(seq);
    }
}