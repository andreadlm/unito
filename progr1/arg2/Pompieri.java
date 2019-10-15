class Pompieri {
    public static void main(String[] args) {
        
        int n;

        System.out.print("Inserire il numero di pompieri: ");
        n = SIn.readInt();

        if(n > 0) {
            int i = 1;
            System.out.println(i + " pompiere + 1 pomperire = " + (++i) + " pompieri");
            while(i < n)
                System.out.println(i + " pompieri + 1 pomperire = " + (++i) + " pompieri");
        } else {
            System.out.println("Numero di pompieri immesso non valido");
        }
    }
}