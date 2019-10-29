class DiffMinMax {
    public static void main(String[] args) {
        final int N = 10;
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        int c;

        System.out.print("Inserire " + N + " numeri:  ");

        for(int i = 0; i < N; i++) {
            c = SIn.readInt();

            if(c > max) max = c;
            if(c < min) min = c;
        }

        System.out.println("La differenza tra il massimo e il minimo e': " + (max - min));
    }
}