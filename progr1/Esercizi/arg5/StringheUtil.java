package arg5;

public class StringheUtil {

    public static boolean trova(String s, char c) {
        boolean res = false;

        for(int i = 0; !res && i < s.length(); i++)
            if(c == s.charAt(i)) res = true;

        return res;
    }

    public static boolean palindroma(String s) {
        
        boolean res = true;
        int i, j;

        /*
        // Considerando gli spazi
        for(i = 0, j = s.length() - 1; res && i < j; i++, j--) 
            if(s.charAt(i) != s.charAt(j)) res = false;
        */

        // Non considerando gli spazi
        i = 0;
        j = s.length() -1;

        while(res && i < j) {
            if(s.charAt(i) == ' ') {
                i++;
            } else if(s.charAt(j) == ' ') {
                j--;
            } else {
                if(s.charAt(i) != s.charAt(j)) res = false;
                i++; j--;
            }
        }

        return res;
    }
}