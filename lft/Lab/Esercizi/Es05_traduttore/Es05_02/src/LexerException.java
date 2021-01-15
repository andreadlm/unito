public class LexerException extends Exception {
    public final static int exitStatus = 2;

    int line = -1;

    public LexerException(String s, int line) {
        super(s);
        this.line = line;
    }

    public LexerException(String s) { super(s); }

    @Override
    public String toString() {
        return "[Error | LexerException]:" +
                (line != -1 ? " near line " + line + " " : "") +
                this.getLocalizedMessage();
    }
}
