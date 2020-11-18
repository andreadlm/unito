public class LexerException extends Exception {
    final static int exitStatus = 2;

    int line;

    public LexerException(String s, int line) {
        super(s);
        this.line = line;
    }

    public LexerException(String s) {
        super(s);
        this.line = -1;
    }

    @Override
    public String toString() {
        return "[ERROR] --> [LexerException] " +
                (line != -1 ? "--> near line " + line + " --> " : "") +
                this.getLocalizedMessage();
    }
}
