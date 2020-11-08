// La classe implementa un parser per riconoscere le
// stringhe composte da sole parenteso quadre bilanciate.
// Esempi di stringhe riconosciute: [[]], [[][][]][][[]], [][][[]]

// Insiemi guida:
// GUIDA(S -> eps) = {$, ]}
// GUIDA(S -> [S]S) = {[}

public class BalancedBrackets extends Parser {
    @Override
    protected void S() throws SyntaxError {
        switch(peek()) {
            case '[':
                match('[');
                S();
                match(']');
                S();
                break;

            case '$':
                break;

            case ']':
                break;

            default:
                throw error("S");
        }
    }

    public static void main(String[] args) {
        try {
            new BalancedBrackets().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}
