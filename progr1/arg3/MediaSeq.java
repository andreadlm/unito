class MediaSeq {
    public static void main(String[] args) {
        int sum = 0;
        int cv;
        int c = 0;

        System.out.print("Inserire una sequenza di numeri interi terminata dallo 0: ");

        while((cv = SIn.readInt()) != 0) {
            sum += cv;
            c++;
        }

        System.out.println("La media dei valori inseriti e': " + (float)sum/c);
    }
}