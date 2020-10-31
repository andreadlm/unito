#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#define MAX_LINE 1000

#define EXIT free_data(rows, rows_num); fclose(f_r); fclose(f_w);

void free_data(char**, int);

void file_shuffle_rows(char* path_f_r, char* path_f_w)
{
    FILE *f_r, *f_w;

    if((f_r = fopen(path_f_r, "r")) == NULL ||
       (f_w = fopen(path_f_w, "w")) == NULL) {
        fputs(strerror(errno), stderr);
        return;
    }

    int rows_num = 0;
    char c;
    while((c = (char)fgetc(f_r)) != EOF)
        if(c == '\n') rows_num++;

    fseek(f_r, 0, SEEK_SET);

    char** rows = (char**)calloc(rows_num, sizeof(*rows));

    int i;
    for(srand(getpid()), i = 0; i < rows_num; i++) {
        int r_idx = rand() % rows_num;

        while(rows[r_idx] != NULL) r_idx = (r_idx + 1) % rows_num;

        rows[r_idx] = (char*)malloc(sizeof(*rows[r_idx]) * MAX_LINE);

        if(fgets(rows[r_idx], MAX_LINE, f_r) == NULL) {
            fputs("Error reading line\n", stderr);
            EXIT
            return;
        }
    }

    for(i = 0; i < rows_num; i++) fputs(rows[i], f_w);

    EXIT
}

void free_data(char** rows, int rows_num)
{
    int i;
    for(i = 0; i < rows_num; i++) free(rows[i]);
    free(rows);
}