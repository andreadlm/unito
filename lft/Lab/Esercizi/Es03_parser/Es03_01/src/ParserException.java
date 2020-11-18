public class ParserException extends Exception {
    final static int exitStatus = 3;

    int line;
    String procedure;

    public ParserException(String procedure, int line) {
        this.procedure = procedure;
        this.line = line;
    }

    public ParserException(String s, String procedure, int line) {
        super(s);
        this.procedure = procedure;
        this.line = line;
    }

    public ParserException(String s) {
        super(s);
        this.line = -1;
    }

    @Override
    public String toString() {
        return "[ERROR] --> [ParserException] " +
                (line != -1 ? "--> near line " + line : "") +
                (procedure != null ? " --> procedure " + procedure : "") +
                (this.getLocalizedMessage() != null ? " --> " + this.getLocalizedMessage() : "");
    }
}
