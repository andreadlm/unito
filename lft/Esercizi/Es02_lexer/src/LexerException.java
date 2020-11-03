public class LexerException extends Exception {
    public LexerException(String s) {
        super(s);
    }

    @Override
    public String toString() {
        return "[ERROR] " + super.toString();
    }
}
