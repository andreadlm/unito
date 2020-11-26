import java.io.*;

public class Valutatore {
    private final Lexer lex;
    private final BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) throws LexerException {
        lex = l;
        pbr = br;
        move();
    }

    void move() throws LexerException {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void match(int t) throws ParserException, LexerException {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else throw new ParserException("Syntax error", null, lex.line);
    }

    public void start() throws ParserException, LexerException {
        if(look.tag == Tag.NUM || look.tag == '(' ||look.tag == -1) {
            int expr_val = expr();
            match(Tag.EOF);
            System.out.println(expr_val);
        } else {
            throw new ParserException("start", lex.line);
        }
    }

    private int expr() throws ParserException, LexerException {
        if(look.tag == '(' || look.tag == Tag.NUM) {
            int term_val = term();
            int exprp_val = exprp(term_val);
            return exprp_val;
        } else {
            throw new ParserException("expr", lex.line);
        }
    }

    private int exprp(int exprp_i) throws ParserException, LexerException {
        int term_val, exprp_val = 0;
        switch (look.tag) {
            case '+' -> {
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
            }
            case '-' -> {
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
            }
            case -1, ')' -> exprp_val = exprp_i;
            default -> throw new ParserException("exprp", lex.line);
        }

        return exprp_val;
    }

    private int term() throws ParserException, LexerException {
        switch (look.tag) {
            case '(', Tag.NUM -> {
                int fact_val = fact();
                int term_val = termp(fact_val);
                return term_val;
            }
            default -> throw new ParserException("term", lex.line);
        }
    }

    private int termp(int termp_i) throws ParserException, LexerException {
        int fact_val, termp_val = 0;
        switch(look.tag) {
            case '*' -> {
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
            }
            case '/' -> {
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
            }
            case -1, '+', '-', ')' -> termp_val = termp_i;
            default -> throw new ParserException("termp", lex.line);
        }

        return termp_val;
    }

    private int fact() throws ParserException, LexerException {
        int fact_val;
        switch(look.tag) {
            case '(' -> {
                match('(');
                int expr_val = expr();
                match(')');
                fact_val = expr_val;
            }
            case Tag.NUM -> {
                int num = ((NumberTok)look).lexeme;
                match(Tag.NUM);
                fact_val = num;
            }
            default -> throw new ParserException("fact", lex.line);
        }

        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Andrea\\Documents\\unito\\lft\\Lab\\Esercizi\\Es04_valutatore\\Es04_01\\src\\valutatore_test_expr.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        } catch(ParserException pe) {
            pe.printStackTrace();
            System.exit(ParserException.exitStatus);
        } catch(LexerException le) {
            le.printStackTrace();
            System.exit(LexerException.exitStatus);
        }
    }
}
