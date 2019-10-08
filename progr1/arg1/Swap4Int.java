class Swap4Int {
    public static void main(String[] args) {
        int int1 = 1;
        int int2 = 2;
        int int3 = 3;
        int int4 = 4;
        int tmp;

        tmp = int1;
        int1 = int2;
        int2 = int3;
        int3 = int4;
        int4 = tmp;

        System.out.println("[int1 : " + int1 + "] [int2 : " + int2 + "] [int3 : " + int3 + "] [int4 : " + int4 + "]");
    }
}