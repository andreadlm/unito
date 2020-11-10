import java.io.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) throws LexerException {
        // TODO: un miglioramento potrebbe essere incrementare line una sola volta nel primo if
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
                    do { readch(br); } while (peek != '\n');
                } else if(peek == '*') {
                    // Caso commento multilinea: un commento multilinea è definito come
                    // una sequenza di caratteri che inizia con /* e temina con */
                    // Il riconoscimento del termine del commento viene eseguito attraverso
                    // un semplice DFA dotato di 3 stati (0, 1 e lo stato finale 2)
                    int state = 0;
                    do {
                        readch(br);
                        switch(state) {
                            case 0:
                                if(peek == '*') state = 1;
                                if(peek == '\n') line++;
                                break;

                            case 1:
                                if(peek == '/') state = 2;
                                else if(peek != '*') {
                                    state = 0;
                                    if(peek == '\n') line++;
                                }
                                break;
                        }
                        // La condizione di uscita dal DFA è il raggiungimento dello stato
                        // 2, rappresentate la fine del commento (una sequenza */ è stata
                        // identificata
                    } while(state != 2 && peek != (char)-1);
                    // TODO: scrivere la condizione EOF come parte del DFA
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
            case '!':
                peek = ' ';
                return Token.not;

            case ';':
                peek = ' ';
                return Token.semicolon;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    throw new LexerException("& is not an operator");
                }

            case '|':
                readch(br);
                if(peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    throw new LexerException("| is not an operator ");
                }

            case '>':
                readch(br);
                if(peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '<':
                readch(br);
                if(peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if(peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }

            case '=':
                readch(br);
                if(peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }

            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {
                    StringBuilder buf = new StringBuilder();

                    while(peek == '_') {
                        buf.append(peek);
                        readch(br);
                    }

                    if(Character.isLetter(peek) || Character.isDigit(peek)) {
                        do {
                            buf.append(peek);
                            readch(br);
                        } while(Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');
                    } else {
                        throw new LexerException("erroneous sequence for an identifier: _ must be followed by a letter or a digit");
                    }

                    String s = buf.toString();

                    switch (s) {
                        case "cond":
                            return Word.cond;

                        case "when":
                            return Word.when;

                        case "then":
                            return Word.then;

                        case "else":
                            return Word.elsetok;

                        case "while":
                            return Word.whiletok;

                        case "do":
                            return Word.dotok;

                        case "seq":
                            return Word.seq;

                        case "print":
                            return Word.print;

                        case "read":
                            return Word.read;

                        default:
                            return new Word(Tag.ID, s);
                    }

                } else if (Character.isDigit(peek)) {
                    if(peek == '0') {
                        peek = ' ';
                        return new NumberTok(0);
                    } else {
                        int num = 0;

                        do {
                            num = num * 10 + (peek - '0');
                            readch(br);
                        }while(Character.isDigit(peek));

                        return new NumberTok(num);
                    }
                } else{
                    throw new LexerException("erroneous character, " + peek + " not recognised");
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Andrea\\Documents\\unito\\lft\\Lab\\Esercizi\\Es02_lexer\\Es02_03\\src\\lex_test.txt"; // il percorso del file da leggere

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;

            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);

            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (LexerException le) {
            System.err.println(le.toString());
        }
    }
}