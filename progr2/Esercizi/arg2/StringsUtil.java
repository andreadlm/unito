package arg2;

public class StringsUtil {
    public static String longest(String[] arrS) {
        assert arrS != null && arrS.length > 0;

        String ret = "";
        
        for(String s : arrS) if(s.length() > ret.length()) ret = s;
        
        return ret;
    }

    public static String concatAll(String[] arrS) {
        assert arrS != null;

        String ret = "";

        for(String s : arrS) ret += s;

        return ret;
    }

    public static String trim(String s) {
        assert s != null;

        int i;
        for(i = 0; i < s.length() && s.charAt(i) == ' '; i++) {}
        int j;
        for(j = s.length() - 1; j >= 0 && s.charAt(j) == ' '; j--) {}

        return s.substring(i, j);
    }
}