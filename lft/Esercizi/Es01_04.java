/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe composte da un numero
 * di matricola seguito dal cognome dello studente, nel formato
 * [numero_matricola][cognome].
 * Inoltre, per essere accettata, la stringa deve contentere informazioni
 * relative ad uno studente del turno T2 o T3 (rispettivamente, congome A...K
 * e matricola pari, cognome L...Z e matricola dispari).
 * La stringa può essere preceduta e seguita da un numero indefinito >= 0 di 0,
 * inoltre la matricola e il cognome possono essere separati da un numero
 * qualsiasi di 0.
 * 
 * NB: l'implementazione rispecchia al meglio la stuttura del DFA
 * "su carta", pertanto alcune ottimizzazioni sarebbero possibili.
 */

package Esercizi;

public class Es01_04 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if(ch == ' ') state = 0;
                    else if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 4;
                    else if(ch >= '0' && ch <= '9' && (ch - 48) % 2 == 0) state = 1;
                    else if(ch >= '0' && ch <= '9' && (ch - 48) % 2 == 1) state = 2;
                    else state = -1;
                    break;
            
                case 1:
                    if(ch >= '0' && ch <= '9' && (ch - 48) % 2 == 0) state = 1;
                    else if(ch >= '0' && ch <= '9' && (ch - 48) % 2 == 1) state = 2;
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 4;
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 3;
                    else if(ch == ' ') state = 6;
                    else state = -1;
                    break;

                case 2:
                    if(ch >= '0' && ch <= '9' && (ch - 48) % 2 == 0) state = 1;
                    else if(ch >= '0' && ch <= '9' && (ch - 48) % 2 == 1) state = 2;
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 3;
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 4;
                    else if(ch == ' ') state = 5;
                    else state = -1;
                    break;

                case 3:
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 3;
                    else if(ch >= '0' && ch <= '9') state = 4;
                    else if(ch == ' ') state = 7;
                    else state = -1;
                    break;

                case 4: //pozzo
                    if((ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z') ||
                       (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;

                case 5:
                    if(ch == ' ') state = 5;
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 3;
                    else if((ch >= 'A' && ch <= 'K') ||
                            (ch >= 'a' && ch <= 'k') || 
                            (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;

                case 6:
                    if(ch == ' ') state = 6;
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 3;
                    else if((ch >= 'l' && ch <= 'z') || 
                            (ch >= 'L' && ch <= 'Z') ||
                            (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;

                case 7:
                    if(ch == ' ') state = 7;
                    else if((ch >= 'a' && ch <= 'z') || 
                            (ch >= 'A' && ch <= 'Z') || 
                            (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;
            }
        }

        return state == 3 || state == 7;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[654321 Rossi]: " + (scan("654321 Rossi") == true ? "ok" : "err"));
        System.out.println("[ 123456 Bianchi ]: " + (scan(" 123456 Bianchi ") == true ? "ok" : "err"));
        System.out.println("[1234 56Bianchi]: " + (scan("1234 56Bianchi") == false ? "ok" : "err"));
        System.out.println("[654321Rossi]: " + (scan("654321Rossi") == true ? "ok" : "err"));
        System.out.println("[122B]: " + (scan("122B") == true ? "ok" : "err"));
        System.out.println("[123456Bia nchi]: " + (scan("123456Bia nchi") == false ? "ok" : "err"));
        System.out.println("[Rossi]: " + (scan("Rossi") == false ? "ok" : "err"));
        System.out.println("[2Bianchi]: " + (scan("2Bianchi") == true ? "ok" : "err"));
    }
}
