package arg4;

import libs.SIn;

class StringheCiao {
    public static void main(String[] args) {
        final int N = 5;
        final String cmp = "ciao";

        System.out.println("Inserire le " + N + " stringhe separate da [invio]: ");

        boolean u = true;
        for(int i = 0; i < N; i++)
            u = SIn.readLine().equals(cmp) && u;
          
        System.out.println(u);
    }
}