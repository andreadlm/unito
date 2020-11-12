// La classe implementa un parser per riconoscere
// le stringhe nella forma di espressioni booleane infisse.
// Esempi di stringhe riconosciute: t&f&t, t|f&f, t&~f|t&t

// Inisiemi guida:
// GUIDA(B -> CE) = {t, f, ~, (}
// GUIDA(E -> eps) = {$, (}
// GUIDA(E -> |B) = {|}
// GUIDA(C -> DF) = {t, f, ~, (}
// GUIDA(F -> eps) = {$, |, (}
// GUIDA(F -> &C) = {&}
// GUIDA(D -> t) = {t}
// GUIDA(D -> f) = {f}
// GUIDA(D -> (B)) = {(}
// GUIDA(D -> ~D) = {~}

public class InfixBoolExp extends Parser {
    @Override
    protected void S() throws SyntaxError {
        B();
    }

    private void B() throws SyntaxError {
        switch(peek()) {
            case 't':
            case 'f':
            case '~':
            case '(':
                C();
                E();
                break;

            default:
                throw error("B");
        }
    }

    private void E() throws SyntaxError {
        switch(peek()) {
            case '$':
            case '(':
                break;

            case '|':
                match('|');
                B();
                break;

            default:
                throw error("E");
        }
    }

    private void C() throws SyntaxError {
        switch(peek()) {
            case 't':
            case 'f':
            case '~':
            case '(':
                D();
                F();
                break;

            default:
                throw error("C");
        }
    }

    private void F() throws SyntaxError {
        switch(peek()) {
            case '$':
            case '|':
            case '(':
                break;

            case '&':
                match('&');
                C();
                break;

            default:
                throw error("F");
        }
    }

    private void D() throws SyntaxError {
        switch(peek()) {
            case 't':
                match('t');
                break;

            case 'f':
                match('f');
                break;

            case '(':
                match('(');
                B();
                match(')');
                break;

            case '~':
                match('~');
                D();
                break;

            default:
                throw error("D");
        }
    }

    public static void main(String[] args) {
        try {
            new InfixBoolExp().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}