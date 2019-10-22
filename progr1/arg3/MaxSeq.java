class MaxSeq {
    public static void main(String[] args) {
        System.out.print("Inserire il numero di valori che si desidera valutare: ");
        int n = SIn.readLineInt();

        int max = 0;
        int v;

        while(n > 0) {
            System.out.print("Inserire il valore: ");
            v = SIn.readLineInt();

            if(v <= 0) System.out.println("Il valore inserito non e' strettamente maggiore di 0");
            else {
                if(v > max) max = v;
                n--;
            }   
        }

        System.out.println("Il valore maggiore tra quelli inseriti e': " + max);
    }
}