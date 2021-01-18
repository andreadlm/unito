/*
 * Creato da Andrea Delmastro
 * 
 * Il DFA implementato riconosce le stringhe composte da un numero
 * di matricola seguito dal cognome dello studente, nel formato
 * [numero_matricola][cognome].
 * Inoltre, per essere accettata, la stringa deve contentere informazioni
 * relative ad uno studente del turno T2 o T3 (rispettivamente, congome A...K
 * e matricola pari, cognome L...Z e matricola dispari).
 * La stringa può essere preceduta e seguita da un numero indefinito >= 0 di ' ',
 * inoltre la matricola e il cognome possono essere separati da un numero
 * qualsiasi di ' '.
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
                case 0: /* Stato iniziale */
                    if(ch == ' ') state = 0; /* Lettura spazio */
                    else if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 4; /* Le stringhe devono cominciare (dopo una eventuale sequenza di ' ') con un numero */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 1; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 2; /* Lettura numero dispari */
                    else state = -1;
                    break;
            
                case 1: /* Ultimo numero letto pari */
                    if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 1; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 2; /* Lettura numero dispari */
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 4; /* Lettura lettera compresa tra L e Z -> la matricola termina per numero pari, la stringa deve essere rifiutata */
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 3; /* Lettura lettera compresa tra A e K -> la matricola termina per numero pari */
                    else if(ch == ' ') state = 6; /* Lettura spazio */
                    else state = -1;
                    break;

                case 2: /* Ultimo numero letto dispari */
                    if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 0) state = 1; /* Lettura numero pari */
                    else if(ch >= '0' && ch <= '9' && (ch - '0') % 2 == 1) state = 2; /* Lettura numero dispari */
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 3; /* Lettura lettera compresa tra L e Z -> la matricola termina per numero dispari */
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 4; /* Lettura lettera compresa tra A e K -> la matricola termina per numero pari, la stringa deve essere rifiutata */
                    else if(ch == ' ') state = 5; /* Lettura spazio */
                    else state = -1;
                    break;

                case 3: /* Stato finale: cognome dello studente */ 
                    if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) state = 3; /* Lettura lettera */
                    else if(ch >= '0' && ch <= '9') state = 4; /* Il cognome delle studente non può contere numeri */
                    else if(ch == ' ') state = 7; /* Lettura spazio */
                    else state = -1;
                    break;

                case 4: //pozzo
                    if((ch >= 'A' && ch <= 'Z') || 
                       (ch >= 'a' && ch <= 'z') ||
                       (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;

                case 5: /* Ultimo carattere letto spazio successivo ad una matricola dispari */
                    if(ch == ' ') state = 5;
                    else if((ch >= 'l' && ch <= 'z') || (ch >= 'L' && ch <= 'Z')) state = 3;
                    else if((ch >= 'A' && ch <= 'K') ||
                            (ch >= 'a' && ch <= 'k') || 
                            (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;

                case 6: /* Ultimo carattere letto spazio successivo ad una matricola pari */
                    if(ch == ' ') state = 6;
                    else if((ch >= 'a' && ch <= 'k') || (ch >= 'A' && ch <= 'K')) state = 3;
                    else if((ch >= 'l' && ch <= 'z') || 
                            (ch >= 'L' && ch <= 'Z') ||
                            (ch >= '0' && ch <= '9')) state = 4;
                    else state = -1;
                    break;

                case 7: /* Stato finale: sequenze di ' ' */
                    if(ch == ' ') state = 7;
                    else if((ch >= 'a' && ch <= 'z') || 
                            (ch >= 'A' && ch <= 'Z') || 
                            (ch >= '0' && ch <= '9')) state = 4; /* Simboli successivi agli spazi finali non sono ammessi */
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
        System.out.println("[  1123 Delmastro   ]: " + (scan("  1123 Delmastro   ") == false ? "ok" : "err"));
    }
}
