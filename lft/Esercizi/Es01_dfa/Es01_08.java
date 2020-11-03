/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe dove nela parola
 * Andrea (con qualsiasi combinazione di maiuscole/minuscole) un
 * singolo carattere è sostituito con un carattere casuale.
 */

package Esercizi;

public class Es01_08 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch == 'A' || ch == 'a') state = 1;
                    else state = 2;
                    break;
            
                case 1:
                    if(ch == 'N' || ch == 'n') state = 3;
                    else state = 4;
                    break;

                case 2:
                    if(ch == 'N' || ch == 'n') state = 4;
                    else state = -1;
                    break;

                case 3:
                    if(ch == 'D' || ch == 'd') state = 5;
                    else state = 4;
                    break;

                case 4:
                    if(ch == 'D' || ch == 'd') state = 6;
                    else state = -1;
                    break;

                case 5:
                    if(ch == 'R' || ch == 'r') state = 7;
                    else state = 8;
                    break;

                case 6:
                    if(ch == 'R' || ch == 'r') state = 8;
                    else state = -1;
                    break;

                case 7:
                    if(ch == 'E' || ch == 'e') state = 9;
                    else state = 10;
                    break;
                
                case 8:
                    if(ch == 'E' || ch == 'e') state = 10;
                    else state = -1;
                    break;

                case 9:
                    state = 11;
                    break;

                case 10:
                    if(ch == 'A' || ch == 'a') state = 11;
                    else state = -1;
                    break;

                case 11:
                    state = -1;
                    break;
            }
        }

        return state == 11;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[Andrea]: " + (scan("Andrea") == true ? "ok" : "err"));
        System.out.println("[A%drea]: " + (scan("A%drea") == true ? "ok" : "err"));
        System.out.println("[A%d/ea]: " + (scan("A%d/ea") == false ? "ok" : "err"));
        System.out.println("[and1ea]: " + (scan("and1ea") == true ? "ok" : "err"));
        System.out.println("[*ndreA]: " + (scan("*ndreA") == true ? "ok" : "err"));
        System.out.println("[And_EA]: " + (scan("And_EA") == true ? "ok" : "err"));
        System.out.println("[__drea]: " + (scan("__drea") == false ? "ok" : "err"));
    }
}
