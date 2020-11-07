public abstract class Parser {
    private String w; // stringa da riconoscere
    private int    i; // indice del prossimo simbolo da leggere

    // il metodo principale per avviare il parsing
    public void parse(String v) {
	// inizializza lo stato del parser aggiungendo alla stringa
	// di input il simbolo speciale $
	w = v + "$";
	i = 0;
	S();
	match('$');
    }

    // il metodo S corrisponde al simbolo iniziale della grammatica
    // e deve essere ridefinito opportunamente in una sottoclasse di
    // Parser
    protected abstract void S();

    // legge il simbolo corrente
    protected char peek() {
	return w.charAt(i);
    }

    // legge il simbolo corrente e passa al successivo
    protected void match() {
	match(peek());
    }

    // controlla che il simbolo corrente sia 'a' e, in tal caso, passa
    // al simbolo successivo. In caso contrario, segnala un errore di
    // sintassi
    protected void match(char a) {
	if (peek() == a) i = i + 1;
	else throw error(peek(), a);
    }

    // segnala un errore di sintassi in cui ci si aspettava di
    // riscrivere la variabile v ma il simbolo corrente non
    // appartiene all'insieme guida di v
    protected SyntaxError error(String v) {
	return new SyntaxError("at position " + i + ": unpredicted symbol '" + peek() + "' for " + v);
    }

    // segnala un errore di sintassi in cui ci si aspettava 'atteso'
    // mentre il simbolo corrente è 'a'
    protected SyntaxError error(char a, char atteso) {
	return new SyntaxError("at position " + i + ": unexpected symbol '" + a + "', expected '" + atteso + "'");
    }
}
