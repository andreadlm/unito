public class WcWr extends Parser {
    @Override
    protected void S() throws SyntaxError {
        switch(peek()) {
            case 'c':
                match('c');
                break;

            case '0':
                match('0');
                S();
                match('0');
                break;

            case '1':
                match('1');
                S();
                match('1');
                break;

            default:
               throw error("S");
        }
    }

    public static void main(String[] args) {
        try {
            new WcWr().parse(args[0]);

            System.out.println("[String accepted]");
        } catch(SyntaxError e) {
            System.out.println("[String not accepted]:\n\t" + e.toString());
        }
    }
}
