class Dispari {
    public static void main(String[] args) {
        final int N = 10;
        int cd = 0, sd = 0, c;

        System.out.print("Inserire " + N + " numeri:  ");

        for(int i = 0; i < N; i++) {
            c = SIn.readInt();

            if(c % 2 == 1){
                cd++;
                sd+=c;
            }
        }

        System.out.println("I numeri dispari nella sequenza sono " + cd + ", la loro somma vale " + sd);

    }
}