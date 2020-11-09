#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

#define NUM_KIDS 20
#define BASE_RAND 1
#define WIDTH_RAND 5

#define PRINT_ERROR fprintf(stderr, "[ERROR] --> :%d: %s\n", errno, strerror(errno));

void num_rand_kids()
{
#ifdef LOG
    printf("[LOG] --> Parent pid [%d]\n", getpid());
#endif

    /* Generazione dei processi figli */
    int i;
    pid_t child_pid;
    for(i = 0; i < NUM_KIDS; i++) {
        if((child_pid = fork())) {
            /* Padre */
#ifdef LOG
            printf("[LOG] --> Created child process with pid [%d]\n", child_pid);
#endif
        } else {
            /* Figlio */
            srand(getpid());
            int ret = (rand() % WIDTH_RAND) + BASE_RAND;

            printf("[(child) %d] --> Generated value %d\n", getpid(), ret);

            exit(ret);
        }
    }

    int status;
    int sum;
    for(i = 0, sum = 0; i < NUM_KIDS; i++)
        if((child_pid = wait(&status)) != -1) {
#ifdef LOG
            printf("[LOG] --> Terminated child process [%d] with status [%d]\n", child_pid, WEXITSTATUS(status));
#endif
            sum += WEXITSTATUS(status);
        } else {
            PRINT_ERROR
        }

    printf("[(parent) %d] --> Sum of returned values: %d\n", getpid(), sum);


}