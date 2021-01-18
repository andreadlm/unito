/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe commento
 * di un ipotetico linguaggio composto solo da a, oltre
 * ovviamente a * e /. Viene permessa una sola sequenza
 * finale. Ogni commento può essere preceduto e seguito
 * da una sequenza di simboli dell'alfabeto.
 */

package Esercizi;

public class Es01_10 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0: /* Stato iniziale */
                    if(ch == '/') state = 2; /* Lettura / */
                    else if(ch == 'a' || ch == '*') state = 1; /* Lettura a o * */
                    else state = -1;
                    break;

                case 1: /* Letta sequenza di a o * o / fuori dal commento il cui ultimo simbolo è a o * */
                    if(ch == 'a' || ch == '*') state = 1; /* Letto a o * */
                    else if(ch == '/') state = 2; /* Letto / */
                    else state = -1;
                    break;
                
                case 2: /* Letta sequenza di a o * o / fuori dal commento il cui ultimo simbolo è / */
                    if(ch == '/') state = 2;
                    else if(ch == '*') state = 3; /* Lettura * */
                    else if(ch == 'a') state = 1; /* Lettura a */ 
                    else state = -1;
                    break;

                case 3: /* Letti /* -> inizio commento */
                    if(ch == '*') state = 5; /* Letto  * */
                    else if(ch == 'a' || ch == '/') state = 4; /* Letto carattere ammesso nel commento */
                    else state = -1;
                    break;

                case 4: /* Letta sequenza di caratteri interna al commento il cui ultimo simbolo non è * */
                    if(ch == '*') state = 5; /* Letto * */
                    else if(ch == 'a' || ch == '/') state = 4; /* Letto carattere ammesso nel commento */
                    else state = -1;
                    break;

                case 5: /* Letta sequenza di caratteri interna al commento il cui ultimo simbolo è * */
                    if(ch == '*') state = 5; /* Letto * */
                    else if(ch == 'a') state = 4; /* Letto carattere ammesso nel commento (non / che chiuderebbe il commento) */
                    else if(ch == '/') state = 6; /* Letto / */
                    else state = -1;
                    break;

                case 6: /* Chiuso un commento */
                    if(ch == '/') state = 2; /* Lettura / */
                    else if(ch == 'a' || ch == '*') state = 1; /* Lettura a o * */
                    else state = -1;
                    break;
            }
        }

        return state == 1 || state == 2 || state == 6;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[aaa/****/aa]: " + (scan("aaa/****/aa") == true ? "ok" : "err"));
        System.out.println("[aa/*a*a*/]: " + (scan("aa/*a*a*/") == true ? "ok" : "err"));
        System.out.println("[a/**//***a]: " + (scan("a/**//***a") == false ? "ok" : "err"));
        System.out.println("[aaaa]: " + (scan("aaaa") == true ? "ok" : "err"));
        System.out.println("[/****/]: " + (scan("/****/") == true ? "ok" : "err"));
        System.out.println("[/*aa*/]: " + (scan("/*aa*/") == true ? "ok" : "err"));
        System.out.println("[aaa/*/aa]: " + (scan("aaa/*/aa") == false ? "ok" : "err"));
        System.out.println("[*/a]: " + (scan("*/a") == true ? "ok" : "err"));
        System.out.println("[a/**/***a]: " + (scan("a/**/***a") == true ? "ok" : "err"));
        System.out.println("[a/**/***/a]: " + (scan("a/**/***/a") == true ? "ok" : "err"));
        System.out.println("[a/**/aa/***/a]: " + (scan("a/**/aa/***/a") == true ? "ok" : "err"));
        System.out.println("[aa/*aa]: " + (scan("aa/*aa") == false ? "ok" : "err"));
    }
}
