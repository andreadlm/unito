#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>

#define LAST_PRINTABLE_ASCII_CODE 126
#define FIRST_PRINTABLE_ASCII_CODE 33

#define PRINT_ERROR fprintf(stderr, "[ERROR] --> :%d: %s\n", errno, strerror(errno));

void sigs_handler(int signum);

unsigned char c;

int main(int argc, char* argv[])
{
    if(argc != 2) {
        /* Errato utilizzo del programma */
        fprintf(stderr, "[ERROR] Usage: char-loop <random arg>\n");
        exit(10);
    }

    struct sigaction sa;
    bzero(&sa, sizeof(sa));
    sa.sa_handler = sigs_handler;

    /* Impostazione dell'handler per SIGINT */
    if(sigaction(SIGINT, &sa, NULL)) {
        /* Errore nell'impostazione dell'handler */
        PRINT_ERROR
        exit(11);
    }

     c = argv[1][0];

    while(1)
        if(++c == LAST_PRINTABLE_ASCII_CODE + 1) c = FIRST_PRINTABLE_ASCII_CODE;
}

void sigs_handler(int signum)
{
    switch (signum) {
        case SIGINT:
            /* Error recovery: potrebbe essere che il programma
             * venga interrotto quando c vale LAST_PRINTABLE_
             * ASCII_CODE + 1, in tal caso deve essere comunque
             * ritornato un valore corretto
             */
            _exit(c == LAST_PRINTABLE_ASCII_CODE + 1 ? FIRST_PRINTABLE_ASCII_CODE : c);
    }
}
