package arg9;

import arg7.ArrUtil;
import libs.SIn;

public class MatrixTest {
    public static void main(String[] args) {
        int n;

        System.out.println("Inserire la dimensione della matrice quadrata: ");
        n = SIn.readLineInt();

        Matrix.printMatrix(Matrix.tavolaPitagorica(n));

        int[][] m = { {10,5,15}, {3,6,9,22,24}, {1,2}, {3,3,1,1} };
        Matrix.printMatrix(m);

        System.out.println("La somma delle righe della matrice vale: ");
        ArrUtil.stampa(Matrix.sommaColonne(m));

        System.out.println("\n");
        Matrix.printMatrix(Matrix.azzeraColonnaMax(m));
        System.out.println("\n");
        Matrix.printMatrix(Matrix.azzeraColonnaMax(m));
        System.out.println("\n");
        Matrix.printMatrix(Matrix.azzeraColonnaMax(m));

        System.out.println(Matrix.dom(m));
        
    }
}