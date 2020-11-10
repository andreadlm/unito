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
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
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

            case '/':
                peek = ' ';
                return Token.div;

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
                    throw new LexerException("erroneous character, & can't be followed by " + peek);
                }

            case '|':
                readch(br);
                if(peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    throw new LexerException("erroneous character, | can't be followed by " + peek);
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
                if (Character.isLetter(peek)) {
                    StringBuilder buf = new StringBuilder();
                    do {
                        buf.append(peek);
                        readch(br);
                    } while(Character.isLetter(peek) || Character.isDigit(peek));

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
                        readch(br);
                        if(Character.isDigit(peek))
                            throw new LexerException("erroneous character, 0 can't be followed by " + peek);
                        else return new NumberTok(0);
                    } else {
                        int num = 0;

                        do {
                            num = num * 10 + (peek - '0');
                            readch(br);
                        }while(Character.isDigit(peek));

                        return new NumberTok(num);
                    }
                } else
                    throw new LexerException("erroneous character, " + peek + " not recognised");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Andrea\\Documents\\unito\\lft\\Lab\\Esercizi\\Es02_lexer\\Es02_01\\src\\lex_test.txt"; // il percorso del file da leggere

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