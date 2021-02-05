import java.io.*;

public class Lexer {

    public int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) throws LexerException {
        while(peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r' || peek == '/') {
            if(peek == '/') {
                // Caso '/': lo '/' va trattato separatamente dagli altri caratteri
                // per via del fatto che può assumere significati multipli, tra cui
                // essere l'inzio di una stringa di commento
                readch(br);
                if(peek == '/') {
                    // Caso commento su singola linea: un commento su singola linea è
                    // definito come una sequenza di caratteri che inizia con // e
                    // termina con CR/LF.
                    // Ogni carattere di qui alla fine della riga viene ignorato
                    do { readch(br); } while (peek != '\n' && peek != (char)-1);
                } else if(peek == '*') {
                    // Caso commento multilinea: un commento multilinea è definito come
                    // una sequenza di caratteri che inizia con /* e temina con */
                    // Il riconoscimento del termine del commento viene eseguito attraverso
                    // un semplice DFA dotato di 3 stati (0, 1 e lo stato finale 2)
                    int state = 0;
                    do {
                        readch(br);
                        switch(state) {
                            case 0 -> {
                                if (peek == '*') state = 1;
                                if (peek == '\n') line++;
                                // else state = 0; qualunque altro carattere non altera lo
                                // stato corrente. Il commento può infatti contenere quasiasi
                                // carattere, compresi CR/LF, TAB e caratteri speciali
                            }

                            case 1 -> {
                                if (peek == '/') state = 2;
                                else if (peek != '*') {
                                    state = 0;
                                    if (peek == '\n') line++;
                                }
                                // else state = 1; qualunque altro * letto fa permanere
                                // il DFA nello stato corrente. Il primo * seguito da un
                                // / determinerà la fine del commento
                            }
                        }
                        // La condizione di uscita dal DFA è il raggiungimento dello stato
                        // 2, rappresentate la fine del commento (una sequenza */ è stata
                        // identificata
                    } while(state != 2 && peek != (char)-1);
                    // Ogni commento deve essere chiuso prima della fine del file da specifiche
                    // di implementazione
                    if(peek == (char)-1)
                        throw new LexerException("Comment not closed before the end of the file");
                    // Forzo il programma a leggere il prossimo carattere, in modo tale
                    // che l'ultimo carattere letto (lo '/' a chiusura del commento)
                    // non venga valutato all'inzio della prossima iterazione del while
                    peek = ' ';
                } else {
                    // Il carattere '/' non è seguito da nessun carattere che ne permetta
                    // l'identificazione come commento.
                    // Viene restituito un token che rappresenza l'operatore di divisone
                    return Token.div;
                }
            } else {
                // Caso caratteri senza significato (spazio, TAB, LF, CR)
                if(peek == '\n') line++;
                readch(br);
            }
        }

        switch (peek) {
            case '!' -> {
                peek = ' ';
                return Token.not;
            }

            case ';' -> {
                peek = ' ';
                return Token.semicolon;
            }

            case '{' -> {
                peek = ' ';
                return Token.lpg;
            }

            case '}' -> {
                peek = ' ';
                return Token.rpg;
            }

            case '+' -> {
                peek = ' ';
                return Token.plus;
            }

            case '-' -> {
                peek = ' ';
                return Token.minus;
            }

            case '*' -> {
                peek = ' ';
                return Token.mult;
            }

            case '(' -> {
                peek = ' ';
                return Token.lpt;
            }

            case ')' -> {
                peek = ' ';
                return Token.rpt;
            }

            case '&' -> {
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    throw new LexerException("'&' is not an operator", line);
                }
            }

            case '|' -> {
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    throw new LexerException("'|' is not an operator ", line);
                }
            }

            case '>' -> {
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            }

            case '<' -> {
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }
            }

            case '=' -> {
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }
            }

            case (char) -1 -> {
                return new Token(Tag.EOF);
            }

            default -> {
                if (Character.isLetter(peek) || peek == '_') {
                    // Vengono considerati identificatori sequenze non vuote
                    // di lettere, numeri e il carattere underscore che non
                    // inizino con un numero
                    StringBuilder buf = new StringBuilder();

                    while (peek == '_') {
                        // Gli underscore iniziali vengono gestiti separatamente poichè una
                        // sequenza di underscore devrà essere seguita da un numero o da una
                        // lettera
                        buf.append(peek);
                        readch(br);
                    }

                    if (Character.isLetter(peek) || Character.isDigit(peek)) {
                        do {
                            buf.append(peek);
                            readch(br);
                        } while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');
                    } else {
                        // Sequenze di soli underscore non vengono accettate
                        throw new LexerException("Erroneous sequence for an identifier: '_' must be followed by a letter or a digit", line);
                    }

                    String s = buf.toString();

                    // Una sequenza di lettere può identificare anche identificare parole
                    // riservate del linguaggio, che rappresentano particolari istruzioni
                    return switch (s) {
                        case "cond" -> Word.cond;
                        case "when" -> Word.when;
                        case "then" -> Word.then;
                        case "else" -> Word.elsetok;
                        case "while" -> Word.whiletok;
                        case "do" -> Word.dotok;
                        case "seq" -> Word.seq;
                        case "print" -> Word.print;
                        case "read" -> Word.read;
                        case "true" -> Word.truetok;
                        case "false" -> Word.falsetok;
                        default -> new Word(Tag.ID, s);
                    };

                } else if (Character.isDigit(peek)) {
                    if (peek == '0') {
                        // Nessun numero può essere preceduto da 0. Nell'implementazione
                        // corrente 0478 viene interpretato come due token, 0 e 478, in
                        // successione
                        peek = ' ';
                        return new NumberTok(0);
                    } else {
                        int num = 0;

                        do {
                            // Ogni numero viene 'assemblato' a partire dalla prima cifra
                            // a sinistra e procedendo verso destra
                            num = num * 10 + (peek - '0');
                            readch(br);
                        } while (Character.isDigit(peek));

                        return new NumberTok(num);
                    }
                } else {
                    // Ogni carattere che non rientri in quelli ammessi nei precedenti casi
                    // non viene ammesso
                    throw new LexerException("Erroneous character, " + peek + " not recognised", line);
                }
            }
        }
    }
}