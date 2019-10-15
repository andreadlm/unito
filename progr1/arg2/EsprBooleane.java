class EsprBooleane {
    public static void main(String[] args) {
        System.out.println("1) 3>5 || 10 == 7 + 3 is " + (3>5 || 10 == 7 + 3));
        System.out.println("2) 3 != 5 && (6 < 2 || 5+2 == 10 - 3) is " + (3 != 5 && (6 < 2 || 5+2 == 10 - 3)));
        System.out.println("3) 3 < 5 && 5 < 7 is " + (3 < 5 && 5 < 7));
        System.out.println("4) 3 < 5 && 5 < 7 is " + (3 < 5 && 5 < 7));
        System.out.println("5) 3 < 5 && 7 < 5 is " + (3 < 5 && 7 < 5 ));
        System.out.println("6) true && false is " + (true && false));
        System.out.println("7) true && 5 < 7 is " + (true && 5 < 7));
        System.out.println("8) false || 5 < 10 is " + (false || 5 < 10));
    }
}