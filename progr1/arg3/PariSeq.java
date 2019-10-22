class PariSeq {
    public static void main(String[] args) {
        int cv;
        boolean pari = true;

        System.out.print("Inserire una sequenza di numeri interi terminata dallo 0: ");

        while(pari && (cv = SIn.readInt()) != 0)
            pari = (cv % 2) == 0; 
            
        System.out.println(pari ? "La sequenza contiene solamente numeri pari" : "La sequenza contiene almeno un numero dispari");

        /*
        cv = SIn.readInt();
        while(pari && cv != 0) {

            if(cv % 2 != 0)
                pari = false;
                
            cv = SIn.readInt();
        }

        if(pari) 
            System.out.println("La sequenza contiene solamente numeri pari");
        else
            System.out.println("La sequenza contiene almeno un numero dispari");
        */
    }
}