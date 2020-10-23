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
                case 0:
                    if(ch == '/') state = 2;
                    else if(ch == 'a' || ch == '*') state = 1;
                    else state = -1;
                    break;

                case 1:
                    if(ch == 'a' || ch == '*') state = 1;
                    else if(ch == '/') state = 2;
                    else state = -1;
                    break;
                
                case 2:
                    if(ch == '/') state = 2;
                    else if(ch == '*') state = 3;
                    else if(ch == 'a') state = 1;
                    else state = -1;
                    break;

                case 3:
                    if(ch == '*') state = 5;
                    else if(ch == 'a' || ch == '/') state = 4;
                    else state = -1;
                    break;

                case 4:
                    if(ch == '*') state = 5;
                    else if(ch == 'a' || ch == '/') state = 4;
                    else state = -1;
                    break;

                case 5:
                    if(ch == '*') state = 5;
                    else if(ch == 'a') state = 4;
                    else if(ch == '/') state = 6;
                    else state = -1;
                    break;

                case 6:
                    if(ch == '/') state = 2;
                    else if(ch == 'a' || ch == '*') state = 1;
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
