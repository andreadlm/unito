// La classe implementa un parser per riconoscere
// le stringhe nella forma di espressioni aritmetiche prefisse.
// Esempi di stringhe riconosciute: +12, *2+34, 5

// Inisiemi guida:
// GUIDA(S -> 0) = {0}
// GUIDA(S -> 1) = {1}
// ...
// GUIDA(S -> 9) = {9}
// GUIDA(S -> +SS) = {+}
// GUIDA(S -> *SS) = {*}

public class PrefixExp extends Parser {
    @Override
    protected void S() throws SyntaxError {
        switch(peek()) {
            case '0':
                match('0');
                break;

            case '1':
                match('1');
                break;

            case '2':
                match('2');
                break;

            case '3':
                match('3');
                break;

            case '4':
                match('4');
                break;

            case '5':
                match('5');
                break;

            case '6':
                match('6');
                break;

            case '7':
                match('7');
                break;

            case '8':
                match('8');
                break;

            case '9':
                match('9');
                break;

            case '+':
                match('+');
                S();
                S();
                break;

            case '*':
                match('*');
                S();
                S();
                break;

            default:
                throw error("S");
        }
    }

    public static void main(String[] args) {
        try {
            new PrefixExp().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}
