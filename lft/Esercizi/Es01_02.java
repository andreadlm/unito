/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce gli identificatori di un linguaggio
 * stile java.
 * 
 * NB: l'implementazione rispecchia al meglio la stuttura del DFA
 * "su carta", lo stesso effetto può essere ottenuto eliminando lo
 * stato 3 e sostituendolo con l'ipotetico stato -1 e eliminando 
 * alcune transazioni ridondanti.
 */

package Esercizi;

public class Es01_02 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch >= '0' && ch <= '9') state = 3;
                    else if(ch == '_') state = 1;
                    else if((ch >= 'A' && ch <= 'Z') || 
                            (ch >= 'a' && ch <= 'z')) state = 2;
                    else state = -1;
                    break;
            
                case 1:
                    if((ch >= '0' && ch <= '9') || 
                       (ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z')) state = 2;
                    else if(ch == '_') state = 1;
                    else state = -1;
                    break;

                case 2:
                    if((ch >= '0' && ch <= '9') || 
                       (ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z') ||
                       ch == '_') state = 2;
                    else state = -1;
                    break;

                case 3:
                    if((ch >= '0' && ch <= '9') || 
                       (ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z') ||
                       ch == '_') state = 3;
                    else state = -1;
                    break;
            }
        }

        return state == 2;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[a2]: " + (scan("a2") == true ? "ok" : "err"));
        System.out.println("[221B]: " + (scan("221B") == false ? "ok" : "err"));
        System.out.println("[___]: " + (scan("___") == false ? "ok" : "err"));
        System.out.println("[x2y2]: " + (scan("x2y2") == true ? "ok" : "err"));
        System.out.println("[lft_lab]: " + (scan("lft_lab") == true ? "ok" : "err"));
        System.out.println("[9_to_5]: " + (scan("9_to_5") == false ? "ok" : "err"));
    }
}
