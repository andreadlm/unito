/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe composte da
 * {a, b} che contengono almeno una 'a' nelle ultime tre
 * lettere (se esistono, altrimenti la stringa deve
 * contenere almento una a).
 * 
 * NB: l'implementazione rispecchia al meglio la stuttura del DFA
 * "su carta", pertanto alcune ottimizzazioni sarebbero possibili.
 */

package Esercizi;

public class Es01_07 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0: /* Stato iniziale */
                    if(ch == 'b') state = 4; /* Lettura di b */
                    else if(ch == 'a') state = 1; /* Lettura di a */
                    else state = -1;
                    break;
                
                case 1: /* Ultimo carattere letto a */
                    if(ch == 'b') state = 2; /* Lettura b */
                    else if(ch == 'a') state = 1; /* Lettura a */
                    else state = -1;
                    break;

                case 2: /* Penultimo carattere letto a */
                    if(ch == 'b') state = 3; /* Lettura b */
                    else if(ch == 'a') state = 1; /* Lettura a */
                    else state = -1;
                    break;

                case 3: /* Terzultimo carattere letto a */
                    if(ch == 'b') state = 4; /* Lettura di b */
                    else if(ch == 'a') state = 1; /* Lettura di a */
                    else state = -1;
                    break;

                case 4: /* Ultimi tre caratteri letti bbb */
                    if(ch == 'b') state = 4; /* Lettura b */
                    else if(ch == 'a') state = 1; /* Lettura a */
                    else state = -1;
                    break;
            }
        }

        return state == 1 || state == 2 || state == 3;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[abb]: " + (scan("abb") == true ? "ok" : "err"));
        System.out.println("[bbaba]: " + (scan("bbaba") == true ? "ok" : "err"));
        System.out.println("[bbabbbbbbbb]: " + (scan("bbabbbbbbbb") == false ? "ok" : "err"));
        System.out.println("[baaaaaaa]: " + (scan("baaaaaaa") == true ? "ok" : "err"));
        System.out.println("[aaaaaaa]: " + (scan("aaaaaaa") == true ? "ok" : "err"));
        System.out.println("[ba]: " + (scan("ba") == true ? "ok" : "err"));
        System.out.println("[abbbbbb]: " + (scan("abbbbbb") == false ? "ok" : "err"));
    }
}
