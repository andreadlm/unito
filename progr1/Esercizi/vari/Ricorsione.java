package vari;

import libs.SIn;

class Ricorsione {
    public static void main(String[] args) {
        int n, m, x;

        System.out.print("Inserire i due interi dei quali si vuole calcolare la somma: ");
        m = SIn.readInt();
        n = SIn.readInt();
        System.out.println("La somma di " + m + " e " + n + " vale: " + somma(m,n));

        System.out.print("Inserire i due interi dei quali si vuole calcolare il prodotto: ");
        m = SIn.readInt();
        n = SIn.readInt();
        System.out.println("La prodotto di " + m + " e " + n + " vale: " + prodotto(m,n));

        System.out.print("Inserire i due interi dei quali si vuole calcolare il prodotto come x*2^n: ");
        x = SIn.readInt();
        n = SIn.readInt();
        System.out.println("La prodotto di " + x + " e 2^" + n + " vale: " + ppot2(x,n));

        System.out.print("Inserire i due interi dei quali si vuole calcolare la differenza: ");
        m = SIn.readInt();
        n = SIn.readInt();
        System.out.println("La prodotto di " + m + " e " + n+ " vale: " + differenza(m,n));

        System.out.print("Inserire l'intero del quale si vogliono sapere i divisori: ");
        n = SIn.readInt();
        System.out.println("La lista dei divisori di " + n + " e':"); divisori(n);

        System.out.print("Inserire l'intero del quale si vuole conoscere la radice quadrata: ");
        n = SIn.readInt();
        System.out.println("L'approssimazione intera piu' vicina alla radice quadrata di " + n + " e': " + radice(n));
    }

    /** Dati n, m calcola la loro somma ricorsivamente */
    public static int somma(int m, int n) {
        if(n == 0) return m;
        return 1 + somma(m, n-1);
    }

    /** Dati m, n calcola il loro prodotto m*n ricorsivamente */
    public static int prodotto(int m, int n) {
        if(n == 0) return 0;
        return m + prodotto(m, n-1);
    }

    /** Dati x, n calcola x*2^n ricorsivamente */
    public static int ppot2(int x, int n) {
        if(n == 0) return x;
        return 2 * ppot2(x, n-1);
    }

    /** Dati m, n calcola la loro differenza ricorsivamente */
    public static int differenza(int m, int n) {
        if(n == 0) return m;
        return differenza(m, n - 1) - 1;
    }

    /** Dato un n scrive in stdout i divisori di n */
    public static void divisori(int n) {
        div(n, 1);
    }

    private static void div(int n, int i) {
        if(i <= n) div(n, i+1);
        if(n % i == 0) System.out.println(i);
    }

    /** Dato un n calcola la migliore approssimazione intera 
     *  della sua radice quadrata
     */
    public static int radice(int n) {
        return sr(n, 1);
    }

    private static int sr(int n, int i) {
        if(i * i <= n) {
            int nxt = sr(n, i+1);
            return (Math.abs(n - i * i) < Math.abs(n - nxt * nxt)) ? i : nxt;
        }
        return i;
    }
}