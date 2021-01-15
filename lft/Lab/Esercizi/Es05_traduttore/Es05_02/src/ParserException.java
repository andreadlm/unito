public class ParserException extends Exception {
    public final static int exitStatus = 3;

    String procedure = null;
    Token tok = null;
    int line = -1;

    public ParserException(String procedure, Token tok, int line) {
        this.procedure = procedure;
        this.tok = tok;
        this.line = line;
    }

    public ParserException() { super("Syntax error"); }

    @Override
    public String toString() {
        return "[Error | ParserException]:" +
                (line != -1 ? " near line " + line : "") +
                (procedure != null ? " in procedure ~" + procedure + "~" : "") +
                (tok != null ? " unpredicted token " + tok : "") +
                (this.getLocalizedMessage() != null ? " --> " + this.getLocalizedMessage() : "");
    }
}
