// La classe implementa un parser per riconoscere le stringhe
// nel formato AnBn.
// Esempi di stringhe riconosciute: aaabbb, ab, eps

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
