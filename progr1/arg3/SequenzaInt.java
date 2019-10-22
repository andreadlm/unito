class SequenzaInt {
    public static void main(String[] args) {
        int cv;
        int max = 0;

        System.out.print("Inserire una sequenza di numeri interi terminata dallo 0: ");

        while((cv = SIn.readInt()) != 0) if(cv > max) max = cv;

        System.out.println("Il massimo numero inserito e': " + max);
    }
}