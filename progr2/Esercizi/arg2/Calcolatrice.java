package arg2;

import libs.Stack;

public class Calcolatrice {
    private static final int MAX_STACK_SIZE = 100;

    Stack stack = null;

    public Calcolatrice() {
        stack = new Stack(MAX_STACK_SIZE);
    }

    public int execute(String exp) {
        int op1, op2;

        for(int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if(c >= '0' && c <= '9') stack.push(c - '0');
            else if(c == '+') stack.push(stack.pop() + stack.pop()); // commutativa
            else if(c == '*') stack.push(stack.pop() * stack.pop()); // commutativa
            else if(c == '-') {
                op2 = stack.pop();
                op1 = stack.pop();
                stack.push(op1 - op2);
            } else if(c == '/') {
                op2 = stack.pop();
                op1 = stack.pop();
                stack.push(op1 / op2);
            } else if(c == '%') {
                op2 = stack.pop();
                op1 = stack.pop();
                stack.push(op1 % op2);
            }
        }

        return stack.pop();
    }
}