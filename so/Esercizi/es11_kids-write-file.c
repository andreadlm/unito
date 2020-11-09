#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

#define PRINT_ERROR fprintf(stderr, "[ERROR] --> :%d: %s\n", errno, strerror(errno));
#define PRINT_GENERIC_ERROR fprintf(stderr, "[ERROR] --> :generic error:\n");

void kids_write_file(char* path, int n_kids, int n_writes)
{
    printf("[LOG] --> Parent pid [%d]\n", getpid());

    /* Apertura del file */
    FILE* f = fopen(path, "w");
    if(f == NULL) {
        PRINT_ERROR
        return;
    }

    /* Creazione dei processi figli */
    int i;
    pid_t child_pid;
    for(i = 0; i < n_kids; i++) {
        if(!(child_pid = fork())) {
            /* figlio */
            pid_t parent_pid = getppid();
            child_pid = getpid();

#ifdef NOT_BUFFERED
            setvbuf(f, NULL, _IONBF, 0);
#endif

            int j;
            for(j = 0; j < n_writes; j++)
                if (fprintf(f, "[%d] [%d]\t", child_pid, parent_pid) < 0)
                    PRINT_GENERIC_ERROR

            if (fputc('\n', f) < 0)
                PRINT_GENERIC_ERROR

            exit(0);
        }

        printf("[LOG] --> Created child process with pid [%d]\n", child_pid);
    }

    /* Attesa del termine dei processi figli */
    int status;
    for(i = 0; i < n_kids; i++)
        if((child_pid = wait(&status)) != -1)
            printf("[LOG] --> Terminated child process [%d] with status [%d]\n", child_pid, WEXITSTATUS(status));
        else
            PRINT_ERROR

    if(fclose(f)) PRINT_ERROR
}