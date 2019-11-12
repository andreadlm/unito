class LibreriaAritmeticaTest {
    public static void main(String[] args) {
        int x, y;
        
        System.out.print("[Somma]\nInserire due interi: ");
        x = SIn.readInt();
        y = SIn.readInt();

        System.out.println("La somma di " + x + " e " + y + " vale : " + LibreriaAritmetica.piu(x,y));
        
        System.out.print("\n[Sottrazione]\nInserire due interi: ");
        x = SIn.readInt();
        y = SIn.readInt();

        System.out.println("La sottrazione di " + x + " e " + y + " vale : " + LibreriaAritmetica.meno(x,y));
        

        System.out.print("\n[Moltiplicazione]\nInserire due interi: ");
        x = SIn.readInt();
        y = SIn.readInt();

        System.out.println("La moltiplicazione di " + x + " e " + y + " vale : " + LibreriaAritmetica.per(x,y));
        

        System.out.print("\n[Divisione intera]\nInserire due interi: ");
        x = SIn.readInt();
        y = SIn.readInt();

        System.out.println("La divisione intera di " + x + " e " + y + " vale : " + LibreriaAritmetica.div(x,y));
        

        System.out.print("\n[Resto della divisione intera]\nInserire due interi: ");
        x = SIn.readInt();
        y = SIn.readInt();

        System.out.println("Il resto della divisione intera di " + x + " e " + y + " vale : " + LibreriaAritmetica.resto(x,y));
        

        System.out.print("\n[Potenza]\nInserire due interi: ");
        x = SIn.readInt();
        y = SIn.readInt();

        System.out.println("La potenza " + x + " elevato a " + y + " vale : " + LibreriaAritmetica.pot(x,y));
    }
}