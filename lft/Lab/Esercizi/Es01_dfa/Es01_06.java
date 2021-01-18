/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe composte da
 * {a, b} che contengono almeno una 'a' nelle prime tre
 * lettere (se esistono, altrimenti la stringa deve
 * contenere almento una a).
 * 
 * NB: l'implementazione rispecchia al meglio la stuttura del DFA
 * "su carta", pertanto alcune ottimizzazioni sarebbero possibili.
 */

package Esercizi;

public class Es01_06 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0: /* Stato iniziale */
                    if(ch == 'b') state = 1; /* Lettura lettera b in prima posizione */
                    else if(ch == 'a') state = 4; /* Lettura lettera a in prima posizione -> stringa essere accettata */
                    else state = -1;
                    break;
                
                case 1: /* b letta in prima posizione */
                    if(ch == 'b') state = 2; /* Lettura b in seconda posizione */
                    else if(ch == 'a') state = 4; /* Lettura a in seconda posizione -> stringa accettata */
                    else state = -1; 
                    break;

                case 2: /* b letta in prima posizione e in seconda posizione */
                    if(ch == 'b') state = 3; /* Lettura b in terza posizione -> stringa rifiutata */
                    else if(ch == 'a') state = 4;
                    else state = -1;
                    break;

                case 3: /* b letta nella prime tre posizioni: stato pozzo */
                    if(ch == 'b' || ch == 'a') state = 3;
                    else state = -1;
                    break;

                case 4: /* Stato finale */
                    if(ch == 'b' || ch == 'a') state = 4;
                    else state = -1;
                    break;
            }
        }

        return state == 4;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[abb]: " + (scan("abb") == true ? "ok" : "err"));
        System.out.println("[abbbbbb]: " + (scan("abbbbbb") == true ? "ok" : "err"));
        System.out.println("[bbbababab]: " + (scan("bbbababab") == false ? "ok" : "err"));
        System.out.println("[bbabbbbbbbb]: " + (scan("bbabbbbbbbb") == true ? "ok" : "err"));
        System.out.println("[aaaaaaa]: " + (scan("aaaaaaa") == true ? "ok" : "err"));
        System.out.println("[b]: " + (scan("b") == false ? "ok" : "err"));
        System.out.println("[a]: " + (scan("a") == true ? "ok" : "err"));
        System.out.println("[ab]: " + (scan("ab") == true ? "ok" : "err"));
        System.out.println("[ba]: " + (scan("ba") == true ? "ok" : "err"));
    }
}
