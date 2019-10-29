class ContieneA {
    public static void main(String[] args) {
        final int N = 5;
        System.out.print("Inserire" + " " + N + " " + "caratteri: ");

        boolean a = false;
        for(int i = 0; !a && i < N; i++)
            a = SIn.readNonwhiteChar() == 'a';
        
        System.out.println(a);
    }
}