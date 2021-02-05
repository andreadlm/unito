import java.io.*;

/**
 * @author Andrea Delmastro
 * La classe permette la traduzione di un programma lft in codice java
 */
public class Translator {
    private final Lexer lex;
    private final BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) throws LexerException {
        lex = l;
        pbr = br;
        move();
    }

    void move() throws LexerException {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void match(int t) throws ParserException, LexerException {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else throw new ParserException();
    }

    /**
     * @author Andrea Delmastro
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    public void prog() throws ParserException, LexerException, IOException {
        if(look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.COND ||
                look.tag == Tag.READ || look.tag == Tag.WHILE || look.tag == '{') {
            // <prog> -> <statlist>EOF
            // Etichetta a cui saltare al termine dell'esecuzione del programma
            // L'etichetta è necessaria poichè la procedura statlist() richiede
            // come attributo ereditato l'etichetta a cui saltare al temine
            // dell'esecuzione di tutti gli statement
            int lNextProg = code.newLabel();
            // La lista di statement dovrà eseguire tutte le istruzioni e poi
            // saltare al termine del programmma
            statlist(lNextProg);
            code.emitLabel(lNextProg);
            match(Tag.EOF);
            code.toJasmin();
        } else {
            throw new ParserException("prog", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param next etichetta a cui saltare al termine dell'esecuzione di tutti
     *             gli statement
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    private void statlist(int next) throws ParserException, LexerException {
        if(look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.COND ||
           look.tag == Tag.READ || look.tag == Tag.WHILE || look.tag == '{') {
            // <statlist> -> <stat><statlistp>
            // Etichetta associata all'inzio del prossimo statement nella lista
            int lNextStat = code.newLabel();
            stat(lNextStat);
            code.emitLabel(lNextStat);
            statlistp(next);
        } else {
            throw new ParserException("statlist", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param next etichetta a cui saltare al termine dell'esecuzione di tutti gli
     *             statement
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void statlistp(int next) throws ParserException, LexerException {
        if(look.tag == ';') {
            // <statlistp> -> <stat><statlistp>
            match(';');
            // Etichetta associata all'inzio del prossimo statement nella lista
            int lNextStat = code.newLabel();
            stat(lNextStat);
            code.emitLabel(lNextStat);
            statlistp(next);
        } else if (look.tag == Tag.EOF || look.tag == '}') {
            // <statlistp> -> eps
            // Termine dell'esecuzione di tutti gli statement, salto all'etichetta
            // associata alla prossima istruzione
            code.emit(OpCode.GOto, next);
        } else {
            throw new ParserException("statlistp", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param next etichetta a cui saltare dopo aver eseguito lo statement
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void stat(int next) throws ParserException, LexerException {
        switch(look.tag) {
            case '=' -> {
                // <stat> -> =ID<expr>
                match('=');
                if (look.tag == Tag.ID) {
                    // Ricerca dell'indice corrispondente alla variabile
                    int idAddr = st.lookupAddress(((Word) look).lexeme);
                    if (idAddr == -1) {
                        // La variabile non è mai stata utilizzata, viene creata
                        idAddr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    expr();
                    code.emit(OpCode.istore, idAddr);
                    code.emit(OpCode.GOto, next);
                } else {
                    throw new ParserException("stat", look, lex.line);
                }
            }

            case Tag.PRINT -> {
                // <stat> -> print(<exprlist>)
                match(Tag.PRINT);
                match('(');
                exprlist(OpCode.invokestatic);
                match(')');
                code.emit(OpCode.GOto, next);
            }

            case Tag.READ -> {
                // <stat> -> read(ID)
                match(Tag.READ);
                match('(');
                if (look.tag == Tag.ID) {
                    // Ricerca dell'indice corrispondente alla variabile
                    int idAddr = st.lookupAddress(((Word) look).lexeme);
                    if (idAddr == -1) {
                        // La variabile non è mai stata utilizzata, viene creata
                        idAddr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, idAddr);
                    code.emit(OpCode.GOto, next);
                } else {
                    throw new ParserException("stat", look, lex.line);
                }
            }

            case Tag.COND -> {
                // <stat> -> cond<whenlist>else<stat>
                match(Tag.COND);
                whenlist(next);
                match(Tag.ELSE);
                stat(next);
            }

            case Tag.WHILE -> {
                // <stat> -> while(<bexpr>)<stat>
                int nextStatLable = code.newLabel();
                int bodyLable = code.newLabel();
                match(Tag.WHILE);
                match('(');
                code.emit(OpCode.label, nextStatLable);
                bexpr(bodyLable, next);
                code.emit(OpCode.label, bodyLable);
                match(')');
                stat(nextStatLable);
            }

            case '{' -> {
                // <stat> -> {<statlist>}
                match('{');
                statlist(next);
                match('}');
            }

            default -> throw new ParserException("stat", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param ifFound etichetta a cui saltare dopo aver eseguito uno degli statements
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void whenlist(int ifFound) throws ParserException, LexerException {
        if (look.tag == Tag.WHEN) {
            // <whenlist> -> <whenitem><whenlistp>
            // Etichetta relativa al prossimo blocco condizonale when a cui saltare nel caso in
            // cui questa condizione risultasse falsa
            int nextWhenLbl = code.newLabel();
            whenitem(ifFound, nextWhenLbl);
            // Emissione dell'etichetta relativa al prossimo blocco when
            code.emit(OpCode.label, nextWhenLbl);
            whenlistp(ifFound);
        } else {
            throw new ParserException("whenlist", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro.
     * @param ifFound etichetta a cui saltare dopo aver eseguito uno degli statements
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void whenlistp(int ifFound) throws ParserException, LexerException {
        if (look.tag == Tag.WHEN) {
            // <whenlistp> -> <whenitem><whenlistp>
            // Etichetta relativa al prossimo blocco condizonale when a cui saltare nel caso in
            // cui questa condizione risultasse falsa
            int nextWhenLbl = code.newLabel();
            whenitem(ifFound, nextWhenLbl);
            // Emissione dell'etichetta relativa al prossimo blocco when
            code.emit(OpCode.label, nextWhenLbl);
            whenlistp(ifFound);
        // if(look.tag == Tag.ELSE) { <whenlistp> -> ε non è necessario fare nulla, viene eseguito il corpo dell'else}
        } else if (look.tag != Tag.ELSE) {
            throw new ParserException("whenlistp", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro.
     * @param ifTrue etichetta a cui saltare se la condizione risulta vera dopo averne eseguito il corpo (etichetta
     * corrispondente al termine della condizione)
     * @param ifFalse etichetta a cui saltare se la condizione risulta falsa (etichetta corrispondente al prossimo
     * blocco when)
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void whenitem(int ifTrue, int ifFalse) throws ParserException, LexerException {
        if (look.tag == Tag.WHEN) {
            // <whenitem> -> when(<bexpr>)do<stat>
            match(Tag.WHEN);
            match('(');
            // Etichetta relativa alla porzione di codice che contiene le istruzioni da eseguire nel caso
            // la condizione risultasse vera
            int statLabel = code.newLabel();
            bexpr(statLabel, ifFalse);
            match(')');
            match(Tag.DO);
            // Emissione dell'etichetta relativa alla porzione di codice che contiene le istruzioni da
            // eseguire nel caso la condizione risultasse vera
            code.emit(OpCode.label, statLabel);
            stat(ifTrue);
        } else {
            throw new ParserException("whenitem", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void expr() throws ParserException, LexerException {
        switch(look.tag) {
            case '+' -> {
                // <expr> -> +(<exprlist>)
                match('+');
                match('(');
                exprlist(OpCode.iadd);
                match(')');
            }

            case '*' -> {
                // <expr> -> *(<exprlist>)
                match('*');
                match('(');
                exprlist(OpCode.imul);
                match(')');
            }

            case '-' -> {
                // <expr> -> - <expr><expr>
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
            }

            case '/' -> {
                // <expr> -> /<expr><expr>
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
            }

            case Tag.NUM -> {
                // <expr> -> NUM
                NumberTok numTok = (NumberTok) look;
                match(Tag.NUM);
                code.emit(OpCode.ldc, numTok.lexeme);
            }

            case Tag.ID -> {
                // <expr> -> ID
                // Ricerca dell'indice corrispondente alla variabile
                int idAddr = st.lookupAddress(((Word) look).lexeme);
                if (idAddr == -1)
                    // Indice non trovato all'interno della symbol table
                    // variabile mai inizializzata
                    throw new ParserException("expr", look, lex.line);
                code.emit(OpCode.iload, idAddr);
                match(Tag.ID);
            }

            default -> throw new ParserException("expr", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param opc opcode da emettere. La procedura exprlist() può essere richiamata inzialmente
     *            dalla procedura stat() nel caso di stat -> print(exprlist) oppure dalla
     *            procedura expr() nei casi expr -> +(explist) e expr -> *(exprlist). Il risultato
     *            atteso da exprlist varia a seconda del contesto dove viene invocata. Il parametro
     *            opCode serve per discriminare sulla base del contesto di invocazione
     * @see #stat(int)
     * @see #expr()
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    private void exprlist(OpCode opc) throws ParserException, LexerException {
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' ||
           look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            // Una discriminante è necessaria: se l'operazione richiesta è la stampa
            // ad ogni expr() deve seguire una istruzione di stampa, altrimenti bisogna
            // prima attendere il caricamento di due operandi e poi eseguire la somma
            // o moltiplicazione
            if(opc == OpCode.invokestatic) code.emit(OpCode.invokestatic, 1);
            exprlistp(opc);
        } else {
            throw new ParserException("exprlist", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param opc opcode da emettere. La procedura exprlistp() può essere richiamata a partire
     *            dalla procedura stat() nel caso di stat -> print(exprlist) oppure dalla
     *            procedura expr() nei casi expr -> +(explist) e expr -> *(exprlist). Il risultato
     *            atteso da exprlistp varia a seconda del contesto dove viene invocata. Il parametro
     *            opCode serve per discriminare sulla base del contesto di invocazione
     * @see #stat(int)
     * @see #expr()
     * @see #exprlist(OpCode)
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    private void exprlistp(OpCode opc) throws ParserException, LexerException {
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' ||
           look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            if(opc == OpCode.invokestatic) code.emit(OpCode.invokestatic, 1);
            else code.emit(opc);
            exprlistp(opc);
        } else if(look.tag != ')') {
            throw new ParserException("exprlistp", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param ifTrue etichetta a cui saltare se l'espressione risulta vera
     * @param ifFalse etichetta a cui saltare se l'espressione risulta falsa
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void bexpr(int ifTrue, int ifFalse) throws ParserException, LexerException {
        switch(look.tag) {
            case Tag.TRUE -> {
                // <bexpr> -> true
                match(Tag.TRUE);
                code.emit(OpCode.GOto, ifTrue);
            }

            case Tag.FALSE -> {
                // <bexpr> -> false
                match(Tag.FALSE);
                code.emit(OpCode.GOto, ifFalse);
            }

            case Tag.AND -> {
                // <bexpr> -> &&<bexpr><bexpr>
                match(Tag.AND);
                int trueLabel = code.newLabel();
                bexpr(trueLabel, ifFalse);
                code.emit(OpCode.label, trueLabel);
                bexpr(ifTrue, ifFalse);
            }

            case Tag.OR -> {
                // <bexpr> -> ||<bexpr><bexpr>
                match(Tag.OR);
                int falseLabel = code.newLabel();
                bexpr(ifTrue, falseLabel);
                code.emit(OpCode.label, falseLabel);
                bexpr(ifTrue, ifFalse);
            }

            case '!' -> {
                // <bexpr> -> !<bexpr>
                match('!');
                bexpr(ifFalse, ifTrue);
            }

            case '(' -> {
                // <bexpr> -> (<bexpr>)
                match('(');
                bexpr(ifTrue, ifFalse);
                match(')');
            }

            case Tag.RELOP -> {
                // <bexpr> -> RELOP<bexpr><bexpr>
                Token tmpLook = look;
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.getRelopOpCodeFromLexeme(tmpLook), ifTrue);
                code.emit(OpCode.GOto, ifFalse);
            }

            default -> throw new ParserException("bexpr", look, lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param args riceve come parametro il percorso del file da tradurre
     */
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = args[0];

        if(args.length != 1) {
            System.out.println("Usage: Transaltor PATH_FILE_TO_TRANSLATE");
            System.exit(3);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch(ParserException pe) {
            pe.printStackTrace();
            System.exit(ParserException.exitStatus);
        } catch(LexerException le) {
            le.printStackTrace();
            System.exit(LexerException.exitStatus);
        }

        System.exit(0);
    }
}