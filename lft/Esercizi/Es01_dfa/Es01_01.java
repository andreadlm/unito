/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe sull'alfabeto {0, 1}
 * che non contengono tre zeri (000) successivi
 */

package Esercizi;

public class Es01_01 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch == '0') state = 1;
                    else if(ch == '1') state = 0;
                    else state = -1;
                    break;
            
                case 1:
                    if(ch == '0') state = 2;
                    else if(ch == '1') state = 0;
                    else state = -1;
                    break;

                case 2:
                    if(ch == '0') state = -1;
                    else if(ch == '1') state = 0;
                    else state = -1;
                break;
            }
        }

        return state == 0 || state == 1 || state == 2;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[1100101]: " + (scan("1100101") == true ? "ok" : "err"));
        System.out.println("[1100010]: " + (scan("1100010") == false ? "ok" : "err"));
        System.out.println("[1001000]: " + (scan("1001000") == false ? "ok" : "err"));
        System.out.println("[1001001111]: " + (scan("1001001111") == true ? "ok" : "err"));
        System.out.println("[01001]: " + (scan("01001") == true ? "ok" : "err"));
        System.out.println("[100a10]: " + (scan("100a10") == false ? "ok" : "err"));
    }
}