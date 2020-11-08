// La classe implementa un parser per riconoscere
// le stringhe della forma a^nb^nc^m.
// Esempi di stringhe riconosciute: aaaabbbbcc, aabbcccc, abc

// Insiemi guida:
// GUIDA(S -> XC) = {a, c, $}
// GUIDA(X -> eps) = {b, c, $}
// GUIDA(X -> aXb) = {a}
// GUIDA(C -> eps) = {$}
// GUIDA(C -> cC) = {c}

public class AnBnCm extends Parser {
    @Override
    protected void S() throws SyntaxError {
        switch(peek()) {
            case 'a':
            case 'c':
            case '$':
                X();
                C();
                break;

            default:
                throw error("S");
        }
    }

    private void X() throws SyntaxError {
        switch(peek()) {
            case 'a':
                match('a');
                X();
                match('b');
                break;

            case 'b':
            case 'c':
            case '$':
                break;

            default:
                throw error("X");
        }
    }

    private void C() throws SyntaxError {
        switch(peek()) {
            case 'c':
                match('c');
                C();
                break;

            case '$':
                break;

            default:
                throw error("C");
        }
    }

    public static void main(String[] args) {
        try {
            new AnBnCm().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}
