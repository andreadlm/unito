import java.io.*;

public class Parser {
    private final Lexer lex;
    private final BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) throws LexerException {
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

    void prog() throws ParserException, LexerException {
        if(look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.COND ||
           look.tag == Tag.READ || look.tag == Tag.WHILE || look.tag == '{') {
            statlist();
            match(Tag.EOF);
        } else {
            throw new ParserException("prog", lex.line);
        }
    }

    void statlist() throws ParserException, LexerException {
        if(look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.COND ||
           look.tag == Tag.READ || look.tag == Tag.WHILE || look.tag == '{') {
            stat();
            statlistp();
        } else {
            throw new ParserException("statlist", lex.line);
        }
    }

    void statlistp() throws ParserException, LexerException {
        if(look.tag == ';') {
            match(';');
            stat();
            statlistp();
        } else if (look.tag != Tag.EOF && look.tag != '}') {
            throw new ParserException("statlistp", lex.line);
        }
    }

    void stat() throws ParserException, LexerException {
        switch(look.tag) {
            case '=' -> {
                match('=');
                match(Tag.ID);
                expr();
            }
            case Tag.PRINT -> {
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
            }
            case Tag.READ -> {
                match(Tag.READ);
                match('(');
                match(Tag.ID);
                match(')');
            }
            case Tag.COND -> {
                match(Tag.COND);
                whenlist();
                match(Tag.ELSE);
                stat();
            }
            case Tag.WHILE -> {
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
            }
            case '{' -> {
                match('{');
                statlist();
                match('}');
            }
            default -> throw new ParserException("stat", lex.line);
        }
    }

    void whenlist() throws ParserException, LexerException {
        if(look.tag == Tag.WHEN) {
            whenitem();
            whenlistp();
        } else {
            throw new ParserException("whenlist", lex.line);
        }
    }

    void whenlistp() throws ParserException, LexerException {
        if(look.tag == Tag.WHEN) {
            whenitem();
            whenlistp();
        } else if(look.tag != Tag.ELSE){
            throw new ParserException("whenlistp", lex.line);
        }
    }

    void whenitem() throws ParserException, LexerException {
        if(look.tag == Tag.WHEN) {
            match(Tag.WHEN);
            match('(');
            bexpr();
            match(')');
            match(Tag.DO);
            stat();
        } else {
            throw new ParserException("whenitem", lex.line);
        }
    }

    void bexpr() throws ParserException, LexerException {
        if(look.tag == Tag.RELOP) {
            match(Tag.RELOP);
            expr();
            expr();
        } else {
            throw new ParserException("bexpr", lex.line);
        }
    }

    void expr() throws ParserException, LexerException {
        switch(look.tag) {
            case '+' -> {
                match('+');
                match('(');
                exprlist();
                match(')');
            }
            case '*' -> {
                match('*');
                match('(');
                exprlist();
                match(')');
            }
            case '-' -> {
                match('-');
                expr();
                expr();
            }
            case '/' -> {
                match('/');
                expr();
                expr();
            }
            case Tag.NUM -> match(Tag.NUM);
            case Tag.ID -> match(Tag.ID);
            default -> throw new ParserException("expr", lex.line);
        }
    }

    void exprlist() throws ParserException, LexerException {
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' ||
           look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            exprlistp();
        } else {
            throw new ParserException("exprlist", lex.line);
        }
    }

    void exprlistp() throws ParserException, LexerException {
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' ||
           look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            exprlistp();
        } else if(look.tag != ')') {
            throw new ParserException("exprlist", lex.line);
        }
    }


    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Andrea\\Documents\\unito\\lft\\Lab\\Esercizi\\Es03_parser\\Es03_02\\src\\parser_test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("\nInput OK");
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
