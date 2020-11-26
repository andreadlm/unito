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

    public void start() throws ParserException, LexerException {
        if(look.tag == Tag.NUM || look.tag == '(' ||look.tag == -1) {
            expr();
            match(Tag.EOF);
        } else {
            throw new ParserException("start", lex.line);
        }
    }

    private void expr() throws ParserException, LexerException {
        if(look.tag == '(' || look.tag == Tag.NUM) {
            term();
            exprp();
        } else {
            throw new ParserException("expr", lex.line);
        }
    }

    private void exprp() throws ParserException, LexerException {
        switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
            case -1:
            case ')':
                break;
            default:
                throw new ParserException("exprp", lex.line);
        }
    }

    private void term() throws ParserException, LexerException {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact();
                termp();
                break;

            default:
                throw new ParserException("term", lex.line);
        }
    }

    private void termp() throws ParserException, LexerException {
        switch(look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;

            case '/':
                match('/');
                fact();
                termp();
                break;

            case '+':
            case '-':
            case -1:
            case ')':
                break;

            default:
                throw new ParserException("termp", lex.line);
        }
    }

    private void fact() throws ParserException, LexerException {
        switch(look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            default:
                throw new ParserException("fact", lex.line);
        }
    }
}
