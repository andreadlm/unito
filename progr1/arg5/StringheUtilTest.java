class StringheUtilTest {
    public static void main(String[] args) {
        String s;
        char c;

        /*System.out.print("Inserire una stringa: ");
        s = SIn.readLine();

        System.out.print("Inserire un carattere: ");
        c = SIn.readChar();

        System.out.println(StringheUtil.trova(s,c));*/

        System.out.print("Inserire una stringa: ");
        s = SIn.readLine();

        System.out.print(StringheUtil.palindroma(s));
    }
}