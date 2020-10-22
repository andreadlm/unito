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
                case 0:
                    if(ch == '/') state = 1;
                    else state = -1;
                    break;
                
                case 1:
                    if(ch == '*') state = 2;
                    else state = -1;
                    break;

                case 2:
                    if(ch == '*') state = 4;
                    else if(ch == 'a' || ch == '/') state = 3;
                    else state = -1;
                    break;

                case 3:
                    if(ch == '*') state = 4;
                    else if(ch == 'a' || ch == '/') state = 3;
                    else state = -1;
                    break;

                case 4:
                    if(ch == '*') state = 4;
                    else if(ch == 'a') state = 3;
                    else if(ch == '/') state = 5;
                    else state = -1;
                    break;

                case 5:
                    state = -1;
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
