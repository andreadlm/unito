package arg5;

import libs.SIn;

class StringheUtilTest {
    public static void main(String[] args) {
        String s;
        char c;

        System.out.print("Inserire una stringa per ricercarne all'interno un carattere: ");
        s = SIn.readLine();

        System.out.print("Inserire un carattere: ");
        c = SIn.readLineNonwhiteChar();

        System.out.println(StringheUtil.trova(s,c));

        System.out.print("Inserire una stringa per verificare se e' palindroma: ");
        s = SIn.readLine();

        System.out.print(StringheUtil.palindroma(s));
    }
}