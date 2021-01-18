/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe composte da un numero
 * di matricola seguito dal cognome dello studente, nel formato
 * [numero_matricola][cognome].
 * Inoltre, per essere accettata, la stringa deve contentere informazioni
 * relative ad uno studente del turno T2 o T3 (rispettivamente, congome A...K
 * e matricola pari, cognome L...Z e matricola dispari)
 * 
 * NB: l'implementazione rispecchia al meglio la stuttura del DFA
 * "su carta", pertanto alcune ottimizzazioni sarebbero possibili.
 */

package Esercizi;

public class Es01_03 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0: /* Stato iniziale */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 4; /* Le stringhe devono cominciare con un numero */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 1; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 2; /* Lettura numero dispari */
                    else state = -1; /* Carattere non riconosciuto */
                    break;
            
                case 1: /* Ultimo numero letto pari */
                    if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 1; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 2; /* Lettura numero dispari */
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 4; /* Lettura lettera compresa tra L e Z -> la matricola termina per numero pari, la stringa deve essere rifiutata */
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 3; /* Lettura lettera compresa tra A e K -> la matricola termina per numero pari */
                    else state = -1;
                    break;

                case 2: /* Utlimo numero letto dispari */
                    if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 1; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 2; /* Lettura numero dispari */
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 3; /* Lettura lettera compresa tra L e K -> la matricola termina per numero dispari */
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 4; /* Lettura lettera compresa tra A e K -> la matricola termina per numero dispari, la stringa deve essere rifuitata */
                    else state = -1;
                    break;

                case 3: /* Stato finale */
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 3; /* Letta lettera */
                    else if(ch >= '0' && ch <= '9') state = 4; /* Il cognome dello studente non può contenere un numero */
                    else state = -1;
                    break;

                case 4: /* Stato pozzo */
                    if((ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z') ||
                       (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;
            }
        }

        return state == 3;
    }

    // Test
    public static void main(String[] args) {
        System.out.println("[123456Bianchi]: " + (scan("123456Bianchi") == true ? "ok" : "err"));
        System.out.println("[123456Rossi]: " + (scan("123456Rossi") == false ? "ok" : "err"));
        System.out.println("[654321Bianchi]: " + (scan("654321Bianchi") == false ? "ok" : "err"));
        System.out.println("[654321Rossi]: " + (scan("654321Rossi") == true ? "ok" : "err"));
        System.out.println("[122B]: " + (scan("122B") == true ? "ok" : "err"));
        System.out.println("[654322]: " + (scan("654322") == false ? "ok" : "err"));
        System.out.println("[Rossi]: " + (scan("Rossi") == false ? "ok" : "err"));
        System.out.println("[2Bianchi]: " + (scan("2Bianchi") == true ? "ok" : "err"));
    }
}
