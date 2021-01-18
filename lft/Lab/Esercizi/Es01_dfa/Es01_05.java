/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe composte dal cognome di uno
 * studente seguito dalla matricola nel formato [cognome][numero_matricola].
 * Inoltre, per essere accettata, la stringa deve contentere informazioni
 * relative ad uno studente del turno T2 o T3 (rispettivamente, congome A...K
 * e matricola pari, cognome L...Z e matricola dispari)
 * 
 * NB: l'implementazione rispecchia al meglio la stuttura del DFA
 * "su carta", pertanto alcune ottimizzazioni sarebbero possibili.
 */

package Esercizi;

public class Es01_05 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0: /* Stato iniziale */
                    if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 1; /* Lettura lettera compresa tra A e K */
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 2; /* Lettura lettera compresa tra L e Z */
                    else if((ch >= '0' && ch <= '9')) state = 7; /* La stringa deve cominciare con il cognome */
                    else state = -1;
                    break;

                case 1: /* Letta sequenza di caratteri cominciante per A-K */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 1; /* Lettura lettera */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 3; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 4; /* Lettura numero dispari */
                    else state = -1;
                    break;

                case 2: /* Letta sequenza di caratteri cominciante per L-Z */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 2; /* Lettura lettera */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 5; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 6; /* Lettura numero dispari */
                    else state = -1;
                    break;

                case 3: /* Ultimo numero letto pari arrivando da cognome A-K, stato finale*/ 
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 7; /* La matricola non può contenere lettere */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 3; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 4; /* Lettura numero dispari */
                    else state = -1;
                    break;

                case 4: /* Ultimo numero letto dispari arrivando da cognome A-K */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 7; /* La matricola non può contenere lettere */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 3; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 4; /* Lettura numero dispari */
                    else state = -1;
                    break;

                case 5: /* Ultimo numero letto pari arrivando da cognome L-Z */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 7; /* La matricola non può contenere lettere */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 5; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 6; /* Lettura numero dispari */
                    else state = -1;
                    break;

                case 6: /* Ultimo numero letto dispari arrivando da cognome L-Z, stato finale */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 7; /* La matricola non può contenere lettere */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 5; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 6; /* Lettura numero dispari */
                    else state = -1;
                    break;
                
                    case 7: /* Stato pozzo */
                    if((ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z') ||
                       (ch >= '0' && ch <= '9')) state = 7;
                    else state = -1;
                    break;
            }
        }

        return state == 3 || state == 6;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[Rossi654321]: " + (scan("Rossi654321") == true ? "ok" : "err"));
        System.out.println("[Bainchi123456]: " + (scan("Bainchi123456") == true ? "ok" : "err"));
        System.out.println("[B122]: " + (scan("B122") == true ? "ok" : "err"));
        System.out.println("[Rossi]: " + (scan("Rossi") == false ? "ok" : "err"));
        System.out.println("[Bianchi2]: " + (scan("Bianchi2") == true ? "ok" : "err"));
        System.out.println("[Delmastro1123]: " + (scan("Delmastro1123") == false ? "ok" : "err"));
    }
}
