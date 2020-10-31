#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

void file_revert(char* path_f_r, char* path_f_w)
{
#ifndef USE_FD
    FILE* f_r, * f_w;

    if((f_r = fopen(path_f_r, "r")) == NULL ||
       (f_w = fopen(path_f_w, "w")) == NULL) {
        fputs(strerror(errno), stderr);
        return;
    }

    int i = -1;
    do{
        fseek(f_r, i--, SEEK_END);
        fputc(fgetc(f_r), f_w);
    } while(ftell(f_r) != 1);

    fclose(f_r);
    fclose(f_w);
#else
    int f_r, f_w;

    if((f_r = open(path_f_r, O_RDONLY)) == -1 ||
       (f_w = open(path_f_w, O_WRONLY)) == -1) {
        fputs(strerror(errno), stderr);
        return;
    }

    int i = -1;
    char c;
    do{
        lseek(f_r, i--, SEEK_END);
        read(f_r, &c, 1);
        write(f_w, &c, 1);
    } while(lseek(f_r, 0, SEEK_CUR) != 1);

    close(f_r);
    close(f_w);
#endif
}