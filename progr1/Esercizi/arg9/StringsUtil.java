package  arg9;

public class StringsUtil {
    public static boolean stringEquals(String s1, String s2) {
        boolean ret = true;

        int l1 = s1.length(), l2 = s2.length();
        if(l1 != l2) return false;

        for(int i = 0; ret && i < l1; i++)
            ret = (s1.charAt(i) == s2.charAt(i));

        return ret;
    }

    public static boolean palindroma(String s) {
        if(s.length() == 0) return true;
        return (s.charAt(0) == s.charAt(s.length() -1)) && palindroma(s.substring(1, s.length() - 1));
    }
}