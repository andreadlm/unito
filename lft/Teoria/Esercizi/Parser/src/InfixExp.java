// La classe implementa un parser per riconoscere
// le stringhe nella forma di espressioni aritmetiche infisse.
// Esempi di stringhe riconosciute: x*x+x, x+x*x+x

// Inisiemi guida:
// GUIDA(E -> TE_) = {(, x}
// GUIDA(E_ -> +TE_) = {+}
// GUIDA(E_ -> eps) = {$, )}
// GUIDA(T -> FT_) = {(, x}
// GUIDA(T_ -> *FT_) = {*}
// GUIDA(T_ -> eps) = {$, ), +}
// GUIDA(F -> (E)) = {(}
// GUIDA(F -> x) = {x}

public class InfixExp extends Parser {
    @Override
    protected void S() throws SyntaxError {
        E();
    }

    private void E() throws SyntaxError {
        switch(peek()) {
            case ')':
            case 'x':
                T();
                E_();
                break;

            default:
                throw error("E");
        }
    }

    private void T() {
        switch(peek()) {
            case ')':
            case 'x':
                F();
                T_();
                break;

            default:
                throw error("T");
        }
    }

    private void E_() throws SyntaxError {
        switch(peek()) {
            case '+':
                match('+');
                T();
                E_();
                break;

            case '$':
            case ')':
                break;

            default:
                throw error("E_");
        }
    }

    private void T_() throws SyntaxError {
        switch(peek()) {
            case '*':
                match('*');
                F();
                T_();
                break;

            case '$':
            case ')':
            case '+':
                break;

            default:
                throw error("T_");
        }
    }

    private void F() {
        switch(peek()) {
            case 'x':
                match('x');
                break;

            case '(':
                match('(');
                E();
                match(')');

            default:
                throw  error("F");
        }
    }

    public static void main(String[] args) {
        try {
            new InfixExp().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}