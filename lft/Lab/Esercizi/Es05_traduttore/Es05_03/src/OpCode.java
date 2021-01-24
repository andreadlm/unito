public enum OpCode {
    ldc, imul, ineg, idiv, iadd,
    isub, istore, ior, iand, iload,
    if_icmpeq, if_icmple, if_icmplt, if_icmpne, if_icmpge,
    if_icmpgt, ifne, GOto, invokestatic, label;

    public static OpCode getOppositeRelopOpCodeFromLexeme(Token t) {
        switch (((Word)t).lexeme) {
            case "<" -> { return OpCode.if_icmpge; }
            case ">" -> { return OpCode.if_icmple; }
            case "==" -> { return OpCode.if_icmpne; }
            case ">=" -> { return OpCode.if_icmplt; }
            case "<=" -> { return OpCode.if_icmpgt; }
            case "<>" -> { return OpCode.if_icmpeq; }
            default -> { return null; }
        }
    }
}
