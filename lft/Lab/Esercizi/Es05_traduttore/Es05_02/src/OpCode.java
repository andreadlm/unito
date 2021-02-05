public enum OpCode {
    ldc, imul, ineg, idiv, iadd,
    isub, istore, ior, iand, iload,
    if_icmpeq, if_icmple, if_icmplt, if_icmpne, if_icmpge,
    if_icmpgt, ifne, GOto, invokestatic, label;

    public static OpCode getRelopOpCodeFromLexeme(Token t) {
        return switch (((Word) t).lexeme) {
            case "<" -> OpCode.if_icmplt;
            case ">" -> OpCode.if_icmpgt;
            case "==" -> OpCode.if_icmpeq;
            case ">=" -> OpCode.if_icmpge;
            case "<=" -> OpCode.if_icmple;
            case "<>" -> OpCode.if_icmpne;
            default -> null;
        };
    }
}
