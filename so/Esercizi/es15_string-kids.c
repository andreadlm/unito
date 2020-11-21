#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

#define PRINT_ERROR fprintf(stderr, "[ERROR] --> :%d: %s\n", errno, strerror(errno));

#define LAST_PRINTABLE_ASCII_CODE 127
#define FIRST_PRINTABLE_ASCII_CODE 33

#define LOG

int main(int argc, char* argv[]) {
    if(argc != 2) {
        /* Errato utilizzo del programma */
        fprintf(stderr, "[ERROR] Usage: char-loop <number of kids>\n");
        exit(10);
    }

    int kids_num = atoi(argv[1]);

    /* Creazione dell'array per memorizzare i pid dei processi figli */
    pid_t* kids = (pid_t*)(malloc(sizeof(*kids) * kids_num));

    /* Creazione dei processi figli */
    pid_t kid_pid;
    int i;
    for(i = 0; i < kids_num; i++) {
        switch(kid_pid = fork()) {
            case -1:
                /* Errore */
                PRINT_ERROR
                exit(1);
                break;
            case 0:
                /* Figli */
                srand(getpid());
                char param = rand() % LAST_PRINTABLE_ASCII_CODE + FIRST_PRINTABLE_ASCII_CODE;

                char* exec_argv[2];
                exec_argv[0] = "./es15_char-loop";
                exec_argv[1] = &param;

                execve(exec_argv[0], exec_argv, NULL);

                PRINT_ERROR;
                exit(2);
                break;
            default:
                /* Padre */
#ifdef LOG
                printf("[LOG] --> Created child process nr. %d with pid [%d]\n", i + 1, kid_pid);
#endif
                kids[i] = kid_pid;
                break;
        }
    }

    sleep(1);
    /* Creazione del vettore per memorizzare la stringa
     * composta dagli exit status dei processi figli
     * terminati */
    char* kids_str = (char*)malloc(sizeof(*kids_str) * kids_num);
    /* Terminazione dei processi figli */
    int status;
    for(i = 0; i < kids_num; i++) {
        kill(kids[i], SIGINT);
        kid_pid = wait(&status);
#ifdef LOG
        printf("[LOG] --> Terminated child process [%d] with status [%d]\n", kid_pid, WEXITSTATUS(status));
#endif
        kids_str[i] = (char)WEXITSTATUS(status);
    }

    printf("[Main process] Generated string: %s\n", kids_str);

    free(kids_str);
    free(kids);
}