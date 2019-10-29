class SommaSuccessoreIterato {
    public static void main(String[] args) {
        System.out.print("Inserire il primo intero: ");
        int x = SIn.readInt();

        System.out.print("Inserire il secondo intero: ");
        int y = SIn.readInt();

        int res, i;

        System.out.print("Somma (while): ");
        res = x; i = y;
        while(i > 0) {
            res++;
            i--;
        }

        System.out.println(res);

        System.out.print("Somma (for): ");
        for(i = y, res = x; i > 0; i--) res++;

        System.out.println(res);

        System.out.print("Potenza x^y (while): ");
        i = 2; res = x;
        while(i <= y) {
            res *= x;
            i++;
        }

        System.out.println(res);

        System.out.print("Potenza x^y (for): ");
        for(i = 2, res = x; i <= y; i++) res *= x;

        System.out.println(res);
    }
}