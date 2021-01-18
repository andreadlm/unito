/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe commento
 * di un ipotetico linguaggio composto solo da a, oltre
 * ovviamente a * e /. Viene permessa una sola sequenza
 * finale.
 */

package Esercizi;

public class Es01_09 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0: /* Stato iniziale */
                    if(ch == '/') state = 1; /* Lettura / */
                    else state = -1; /* La stringa deve cominciare con / */
                    break;
                
                case 1: /* Letto / iniziale */
                    if(ch == '*') state = 2; /* Lettura * */
                    else state = -1; /* La stringa deve cominciare con /* */
                    break;

                case 2: /* Letto /* */
                    if(ch == '*') state = 4; /* Letto * */
                    else if(ch == 'a' || ch == '/') state = 3; /* Letto carattere ammesso nel commento */
                    else state = -1;
                    break;

                case 3: /* Letto /* più una sequenza di caratteri e * di cui l'ultimoo simbolo è un carattere */
                    if(ch == '*') state = 4; /* Lettura * */
                    else if(ch == 'a' || ch == '/') state = 3; /* Letto carattere ammesso nel commento */
                    else state = -1;
                    break;

                case 4: /* Letto /* più una sequenza di caratteri e * di cui l'ultimoo simbolo è un * */
                    if(ch == '*') state = 4; /* Lettura * */
                    else if(ch == 'a') state = 3; /* Lettura carattere ammesso nel commento (non / che chiuderebe il commento) */
                    else if(ch == '/') state = 5; /* Lettura /, l'* era quello finale */
                    else state = -1;
                    break;

                case 5: /* Letto un commento completo */
                    state = -1; /* Qualunque simbolo invaliderebbe la stringa */
                    break;
            }
        }

        return state == 5;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[/****/]: " + (scan("/****/") == true ? "ok" : "err"));
        System.out.println("[/*a*a*/]: " + (scan("/*a*a*/") == true ? "ok" : "err"));
        System.out.println("[/*/]: " + (scan("/*/") == false ? "ok" : "err"));
        System.out.println("[/*a/**/]: " + (scan("/*a/**/") == true ? "ok" : "err"));
        System.out.println("[/**/]: " + (scan("/**/") == true ? "ok" : "err"));
        System.out.println("[/**a///a/a**/]: " + (scan("/**a///a/a**/") == true ? "ok" : "err"));
        System.out.println("[/**/***/]: " + (scan("/**/***/") == false ? "ok" : "err"));
        System.out.println("[/*/*/]: " + (scan("/*/*/") == true ? "ok" : "err"));
    }
}
