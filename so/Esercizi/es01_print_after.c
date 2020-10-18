#include <string.h>

char* print_after(char* s1, char* s2)
{
    s1[strlen(s1) - 1] = s2[strlen(s2) - 1] = '\0';

    int p = 0, i = 0;
    for(; i < strlen(s1) && p < strlen(s2); i++)
        if(s1[i] == s2[p]) p++;
        else p = 0;

    if(p == strlen(s2)) return &s1[i];
    return "";
}
