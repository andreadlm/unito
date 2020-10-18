#include <string.h>

// ! parameters controls missing !
void count_char(char s[], char cs[], int ns[])
{
    for(int i = 0; i < strlen(s); i++) {
        int j = 0;
        while(j < strlen(cs) && cs[j] != s[i]) j++;
        cs[j] = s[i]; ns[j]++;
    }
}
