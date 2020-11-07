// La classe implementa un parser per riconoscere le stringhe
// nel formato wcw^r.
// Esempi di stringhe riconosciute: 00c00, 01c10, 001001c100100

// Insiemi guida:
// GUIDA(S -> c) = {c}
// GUIDA(S -> 0S0) = {0}
// GUIDA(S -> 1S1) = {1}

public class AnBn extends Parser {
    @Override
    protected void S() throws SyntaxError {
        switch(peek()) {
            case 'a':
                match('a');
                S();
                match('b');
                break;

            case 'b':
                break;

            case '$':
                break;

            default:
                throw error("S");
        }
    }

    public static void main(String[] args) {
        try {
            new AnBn().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}
