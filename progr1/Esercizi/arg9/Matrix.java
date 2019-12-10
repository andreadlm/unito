package Esercizi.arg9;

import Esercizi.libs.SIn;

public class Matrix {
    public static int[][] tavolaPitagorica(int n) {
        int[][] ret = new int[n - 1][n - 1];

        for(int i = 2; i <= n; i++)
            for(int j = 2; j <= n; j++)
                ret[i - 2][j - 2] = i * j;
                
        return ret;
    }

    public static void printMatrix(int[][] m){
        for(int i = 0; i < m.length; System.out.print("\n"), i++) 
            for(int j = 0; j < m[i].length; j++) 
                System.out.print("[" + m[i][j] + "]\t");
    }

    public static int[][] initMatrix() {
        System.out.print("Inserire il numero di righe della matrice: ");
        int n = SIn.readLineInt();

        System.out.print("Inserire il numero di colonne della matrice: ");
        int m = SIn.readLineInt();

        int[][] ret = new int[n][m];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                System.out.println("Inserire il valore nella posizione [" + i + "][" + j + "]: ");
                ret[i][j] = SIn.readLineInt();
            }
        }

        return ret;
    }

    public static int[] sommaColonne(int[][] m) {
        int mC = 0;
        for(int i = 0; i < m.length; i++) 
            if(m[i].length > mC) mC = m[i].length;

        int[] ret = new int[mC];

        for(int i = 0; i < mC; i++) ret[i] = 0;

        for(int i = 0; i < m.length; i++)
            for(int j = 0; j < m[i].length; j++) 
                ret[j] += m[i][j];

        return ret;
    }

    public static int[][] azzeraColonnaMax(int[][] m) {
        int[] sC = sommaColonne(m);

        int cM = 0;
        for(int i = 1; i < sC.length; i++)
            if(sC[i] > sC[cM]) cM = i;

        for(int i = 0; i < m.length; i++)
            if(m[i].length > cM) m[i][cM] = 0;

        return m;
    }

    public static boolean dom(int[][] m) {

        boolean ret = true;
        boolean domR;

        for(int i = 0; i < m.length && ret; i++) {
            domR = false;
            for(int j = 0; j < m[i].length && !domR; j++) {
                domR = domR || dominante(m[i], m[i][j]);
            }
            ret = ret && domR;
        }

        return ret;
    }

    private static boolean dominante(int[] r, int e) {
        int i;
        for(i = 0; i < r.length && (r[i] % e == 0); i++){}
        
        return i == r.length;
    }

}