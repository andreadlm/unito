package arg1;

/** Calcolatrice che esegue le operazioni
 *  (+ * - / %) in RPN */
public class Calcolatrice {
    private static final int MAX_STACK_SIZE = 100;
    private int[] stack = new int[MAX_STACK_SIZE];
    private int sp = -1;

    public int execute(String exp) {
        int op1, op2;

        for(int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if(c >= '0' && c <= '9') push(c - '0');
            else if(c == '+') push(pop() + pop()); // commutativa
            else if(c == '*') push(pop() * pop()); // commutativa
            else if(c == '-') {
                op2 = pop();
                op1 = pop();
                push(op1 - op2);
            } else if(c == '/') {
                op2 = pop();
                op1 = pop();
                push(op1 / op2);
            } else if(c == '%') {
                op2 = pop();
                op1 = pop();
                push(op1 % op2);
            } else if(c == '#') print();
        }

        return pop();
    }
    
    private void push(int x) { stack[++sp] = x; }

    private int pop() { return stack[sp--]; }

    /** Stampa una stringa contenente il contenuto dello 
     *  stack e la sua dimensione */
    private void print() {
        String ret = "";
        
        for(int i = 0; i < sp + 1; i++) ret = ret + '[' + stack[i] + ']' + '\n';

        System.out.println("Stack size : " + (sp + 1) + '\n' + ret.substring(0, ret.length() - 1));
    }

    public static void main(String[] args) {
        Calcolatrice calc = new Calcolatrice();
        System.out.println(calc.execute("34*56+*")); // 12 * 11 = 132
        System.out.println(calc.execute("96+97++98+*")); // (15+16) * 17 = 527
        System.out.println(calc.execute("12*3*4*5*6*7*8*9*91+*")); // 10! = 3628800
        System.out.println(calc.execute("19*9*9*9*9*9*9*9*9*9*9*9*9*9*9*9*9*")); // overflow --> - x
        System.out.print(calc.execute("74#%")); // 7 % 4 = 3 */
    }
}