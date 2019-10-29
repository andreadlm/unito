class DispariMaggiore10 {
    public static void main(String[] args) {
        int c;
        boolean cnd = false;

        System.out.print("Inserire una sequenza di interi terminata da 0: ");

        c = SIn.readInt();
        while(!cnd && c != 0) {
            cnd = c % 2 == 1 && c > 10;
            c = SIn.readInt();
        }

        System.out.println(cnd);
    }
}