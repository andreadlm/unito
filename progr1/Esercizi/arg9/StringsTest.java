package arg9;

import libs.SIn;

public class StringsTest {
    public static void main(String[] args) {
        String s1, s2;

        System.out.print("Inserire la prima stringa: ");
        s1 = SIn.readLine();
        System.out.print("Inserire la seconda stringa: ");
        s2 = SIn.readLine();

        System.out.println("Le due stringhe sono " + (StringsUtil.stringEquals(s1, s2) ? "uguali" : "diverse"));

        System.out.println("La stringa " + (StringsUtil.palindroma(s1) ? "" : "non ") + "e' palindroma");
    }
}