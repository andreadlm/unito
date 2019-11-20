package arg1;

class SwapBool {
    public static void main(String[] args) {
        boolean b1 = true;
        boolean b2 = false;
        boolean tmp;

        System.out.println("b1 : " + b1 + " b2 : " + b2);

        tmp = b1;
        b1 = b2;
        b2 = tmp;

        System.out.println("b1 : " + b1 + " b2 : " + b2);
    }
}