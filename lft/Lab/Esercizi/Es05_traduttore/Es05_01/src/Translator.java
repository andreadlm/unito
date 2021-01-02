import java.io.*;

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
        } else throw new ParserException("Syntax error", null, lex.line);
    }

    public void prog() throws ParserException, LexerException {
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
            code.toJasmin();
        }
        catch(java.io.IOException e) {
            System.out.println(e.toString());
        }
    }

    private void statlist(int statlist_next) throws ParserException, LexerException {
        if(look.tag == '=' || look.tag == Tag.PRINT || look.tag == Tag.COND ||
           look.tag == Tag.READ || look.tag == Tag.WHILE || look.tag == '{') {
            int lnext_stat = code.newLabel();
            stat(lnext_stat);
            code.emitLabel(lnext_stat);
            statlistp(statlist_next);
        } else {
            throw new ParserException("statlist", lex.line);
        }
    }

    void statlistp(int statlistp_next) throws ParserException, LexerException {
        if(look.tag == ';') {
            match(';');
            int lnext_stat = code.newLabel();
            stat(lnext_stat);
            code.emitLabel(lnext_stat);
            statlistp(statlistp_next);
        } else if (look.tag == Tag.EOF || look.tag == '}') {
            code.emit(OpCode.GOto, statlistp_next);
        } else {
            throw new ParserException("statlistp", lex.line);
        }
    }

    void stat(int stat_next) throws ParserException, LexerException {
        switch(look.tag) {
            case '=' -> {
                // Assegnamento di una variabile
                // Esempio: = x + 10 7 -> assegna a x 17
                match('=');
                if(look.tag == Tag.ID) {
                    // Ricerca dell'indice corrispondente alla variabile
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if(id_addr == -1) {
                        // La variabile non è mai stata utilizzata, viene creata
                        id_addr = count;
                        st.insert(((Word)look).lexeme, count++);
                    }
                    match(Tag.ID);
                    expr();
                    code.emit(OpCode.istore, id_addr);
                    code.emit(OpCode.GOto, stat_next); //TODO: necessario?
                } else {
                    throw new ParserException("stat", lex.line);
                }
            }
            case Tag.PRINT -> {
                // Stampa di una lista di espressioni
                match(Tag.PRINT);
                match('(');
                exprlist(OpCode.invokestatic);
                match(')');
                // Per ogni espressione di exprlist eseguo una stampa
                // Goto alla prossima linea di codice
                code.emit(OpCode.GOto, stat_next); // TODO: necessario?
            }
            case Tag.READ -> {
                // Lettura di una variabile
                // Esempio: read 
                match(Tag.READ);
                match('(');
                if(look.tag == Tag.ID) {
                    int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic,0);
                    code.emit(OpCode.istore,id_addr);
                    // Goto alla prossima linea di codice
                    code.emit(OpCode.GOto, stat_next); // TODO: necessario?
                } else {
                    throw new ParserException("stat", lex.line);
                }
            }
            case Tag.COND -> {
                int elseLabel = code.newLabel();
                match(Tag.COND);
                whenlist(stat_next, elseLabel);
                match(Tag.ELSE);
                code.emit(OpCode.label, elseLabel); //TODO: necessario?
                stat(stat_next);
            }
            case Tag.WHILE -> {
                int stat1_next = code.newLabel();
                int bexpr_if_true = code.newLabel();
                match(Tag.WHILE);
                match('(');
                code.emit(OpCode.label, stat1_next);
                bexpr(bexpr_if_true, stat_next);
                code.emit(OpCode.label, bexpr_if_true);
                match(')');
                stat(stat1_next);
            }
            case '{' -> {
                match('{');
                statlist(stat_next);
                match('}');
            }
            default -> throw new ParserException("stat", lex.line);
        }
    }

    /**
     * @author Andrea Delmastro
     * @param ifFound etichetta a cui saltare se una condizione risulta vera dopo averne eseguito il corpo.
     * @param ifNFound etichetta a cui saltare se nessuna condizione risultasse vera (etichetta corrispondente
     * all'inizio del blocco else).
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void whenlist(int ifFound, int ifNFound) throws ParserException, LexerException {
        if (look.tag == Tag.WHEN) {
            /* Etichetta relativa al prossimo blocco condizonale when a cui saltare nel caso in
             * cui questa condizione risultasse falsa */
            int nextWhenLbl = code.newLabel();
            /* Il prossimo blocco salta al successivo blocco when se la condizione è falsa (ci sarà sempre
             * un successivo blocco, nel caso di blocco vuoto conterrà goto, salta a ifFound se la condizione
             * è vera */
            whenitem(ifFound, nextWhenLbl);
            /* Emissione dell'etichetta relativa al prossimo blocco when */
            code.emit(OpCode.label, nextWhenLbl);
            /* Se si è raggiunta la clausola ELSE, significa che non ci sono più condizioni da valutare. Viene
             * effettuato un salto verso la porzione di codice contenente le operazioni da eseguire in caso di
             * else. */
            whenlistp(ifFound, ifNFound);
        } else {
            /* Errore di parsificazione */
            throw new ParserException("whenlist", lex.line);
        }
    }

    /**
     * @author Andrea Delmastro.
     * @param ifFound etichetta a cui saltare se una condizione risulta vera dopo averne eseguito il corpo.
     * @param ifNFound etichetta a cui saltare se nessuna condizione risulta vera (etichetta corrispondente
     * all'inizio del blocco else).
     * @throws ParserException errore in fase di analisi sintattica
     * @throws LexerException errore in fase di analisi lessicale
     */
    void whenlistp(int ifFound, int ifNFound) throws ParserException, LexerException {
        if (look.tag == Tag.WHEN) {
            /* Etichetta relativa al prossimo blocco condizonale when a cui saltare nel caso in
             * cui questa condizione risultasse falsa */
            int nextWhenLbl = code.newLabel();
            /* Il prossimo blocco salta al successivo blocco when se la condizione è falsa (ci sarà sempre
             * un successivo blocco, nel caso di blocco vuoto conterrà goto, salta a ifFound se la condizione
             * è vera */
            whenitem(ifFound, nextWhenLbl);
            /* Emissione dell'etichetta relativa al prossimo blocco when */
            code.emit(OpCode.label, nextWhenLbl);
            /* Questo blocco salta al successivo blocco when se la condizione è falsa, salta a ifNoOneFound
             * se nessuna condizione risulta vera. */
            whenlistp(ifFound, ifNFound);
        } else if (look.tag == Tag.ELSE) {
            /* Se si è raggiunta la clausola ELSE, significa che non ci sono più condizioni da valutare. Viene
             * effettuato un salto verso la porzione di codice contenente le operazioni da eseguire in caso di
             * else. */
            code.emit(OpCode.GOto, ifNFound);
        } else {
            /* Errore di parsificazione */
            throw new ParserException("whenlistp", lex.line);
        }
    }

    /**
     * @author Andrea Delmastro.
     * @param ifTrue etichetta a cui saltare se la condizione risulta vera dopo averne eseguito il corpo (etichetta
     * corrispondente al termine della condizione).
     * @param ifFalse etichetta a cui saltare se la condizione risulta falsa (etichetta corrispondente al prossimo
     * blocco when).
     */
    void whenitem(int ifTrue, int ifFalse) throws ParserException, LexerException {
        if (look.tag == Tag.WHEN) {
            match(Tag.WHEN);
            match('(');
            /* Etichetta relativa alla porzione di codice che contiene le istruzioni da eseguire nel caso
             * la condizione risultasse vera. */
            int statLabel = code.newLabel();
            /* Se la condizione è vera, salta all'etichetta appena creata, altrimenti salta a ifFalse */
            bexpr(statLabel, ifFalse);
            match(')');
            match(Tag.DO);
            /* Emissione dell'etichetta relativa alla porzione di codice che contiene le istruzioni da
             * eseguire nel caso la condizione risultasse vera */
            code.emit(OpCode.label, statLabel);
            /* La prossima istruzione eseguirà e salterà a ifTrue */
            stat(ifTrue);
        } else {
            throw new ParserException("whenitem", lex.line);
        }
    }

    void expr() throws ParserException, LexerException {
        switch(look.tag) {
            case '+' -> {
                match('+');
                match('(');
                exprlist(OpCode.iadd);
                match(')');
            }
            case '*' -> {
                match('*');
                match('(');
                exprlist(OpCode.imul);
                match(')');
            }
            case '-' -> {
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
            }
            case '/' -> {
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
            }
            case Tag.NUM -> {
                NumberTok numTok = (NumberTok)look;
                match(Tag.NUM);
                code.emit(OpCode.ldc, numTok.lexeme);
            }
            case Tag.ID -> {
                Word word = (Word)look;
                match(Tag.ID);
                // Ricerca dell'indice corrispondente alla variabile
                int id_addr = st.lookupAddress(word.lexeme);
                if(id_addr == -1) {
                    // La variabile non è mai stata utilizzata, viene creata
                    id_addr = count;
                    st.insert(((Word)look).lexeme, count++);
                }
                code.emit(OpCode.iload, id_addr);
            }
            default -> throw new ParserException("expr", lex.line);
        }
    }

    private void exprlist(OpCode opc) throws ParserException, LexerException {
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' ||
           look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            if(opc == OpCode.invokestatic) code.emit(OpCode.invokestatic, 1);
            exprlistp(opc);
        } else {
            throw new ParserException("exprlist", lex.line);
        }
    }

    private void exprlistp(OpCode opc) throws ParserException, LexerException {
        if(look.tag == '+' || look.tag == '-' || look.tag == '*' ||
           look.tag == '/' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr();
            if(opc == OpCode.invokestatic) code.emit(OpCode.invokestatic, 1);
            else code.emit(opc);
            exprlistp(opc);
        } else if(look.tag != ')') {
            throw new ParserException("exprlist", lex.line);
        }
    }

    void bexpr(int if_true, int if_false) throws ParserException, LexerException {
        if (look.tag == Tag.RELOP) {
            Token tmpLook = look;
            match(Tag.RELOP);
            expr();
            expr();
            code.emit(OpCode.getRelopOpCodeFromLexeme(tmpLook), if_true);
            code.emit(OpCode.GOto, if_false);
        } else {
            throw new ParserException("bexpr", lex.line);
        }
    }

    // TODO: rivedere il main
    /**
     * @author Andrea Delmastro
     * @param args riceve come parametro il percorso del file da tradurre
     */
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = args[0];
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        } catch(ParserException pe) {
            pe.printStackTrace();
            System.exit(ParserException.exitStatus);
        } catch(LexerException le) {
            le.printStackTrace();
            System.exit(LexerException.exitStatus);
        }
    }
}