package vari;

import libs.SIn;
import java.util.ArrayList;

class DecBin {
    public static void main(String[] args) {
        int n;
        System.out.print("Inserire l'intero da convertire: ");
        n = SIn.readInt();

        ArrayList<Integer> res = new ArrayList<>();
        conversionDecToBin(n, res);

        System.out.println(res);
    }

    public static void conversionDecToBin(int n,  ArrayList<Integer> res) {
        if(n>=1) {
            conversionDecToBin(n/2,res);
            res.add(n%2);
        }
    }
}