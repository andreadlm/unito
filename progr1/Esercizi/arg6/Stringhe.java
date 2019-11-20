package arg6;

public class Stringhe {
    public static int occorrenzeCarattereRec(String s, char c) {
        if(s.length() == 0) return 0;
        if(s.charAt(0) == c) 
            return 1 + occorrenzeCarattereRec(s.substring(1), c);
        else 
            return occorrenzeCarattereRec(s.substring(1), c);
    }

    public static String inversaRec(String s) {
        if(s.length() == 1) return s;
        return inversaRec(s.substring(1)) + s.charAt(0);
    }

    public static String inversaIt(String s) {
        String ret = "";
        for(int i = s.length() - 1; i >= 0; i--) 
            ret += s.charAt(i);
        return ret;
    }
}