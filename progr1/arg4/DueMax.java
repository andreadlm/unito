class DueMax {
    public static void main(String[] args) {
        int c;
        int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;

        System.out.print("Inserire una sequenza di interi teminata da 0: ");

        while((c = SIn.readInt()) != 0) {
            if(c > max1) {
                max2 = max1;
                max1 = c;
            }else if(c > max2) max2 = c;
        }

        System.out.println("I due numeri maggiori sono: " + (max1 != Integer.MIN_VALUE ? max1 : "") +
                                                      " " + (max2 != Integer.MIN_VALUE ? max2 : ""));
    }
}