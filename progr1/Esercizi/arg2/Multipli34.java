package arg2;

import libs.SIn;

class Multipli34 {
    public static void main(String[] args) {
        
        int a;

        System.out.print("Inserire un numero: ");
        a = SIn.readInt();

        // Controllo se il numero è pari e multiplo di 4
        if(a % 2 == 0)
            if(a % 4 == 0) System.out.println("Il numero e' multiplo di 4");
            else System.out.println("Il numero non e' multiplo di 4");
        else
            if(a % 3 == 0) System.out.println("Il numero e' multiplo di 3");
            else System.out.println("Il numero non e' multiplo di 3");
    }
}