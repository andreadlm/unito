#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <sys/sem.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <signal.h>

#define EXIT_SEQ(ex_val) free_sysV(); \
                         exit(ex_val)
#define ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Errore | pid: %d | file: %s | line: %d]\n-> %s\n-> [%d] %s\n%s\n", ANSI_COLOR_RESET, ANSI_COLOR_MAGENTA, getpid(), __FILE__, __LINE__, str, errno, strerror(errno), ANSI_COLOR_RESET)
#define ERROR_EXIT_SEQ(str) ERROR_MESSAGE(str); \
                            EXIT_SEQ(EXIT_FAILURE)

#ifdef DEBUG
#define DEBUG_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#define DEBUG_ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_CYAN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#define SIGNAL_MESSAGE(str, signum) fprintf(stderr, "%s%s[Signal | pid: %d | file: %s | line: %d]\n-> %s [%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_YELLOW, getpid(), __FILE__, __LINE__, str, signum, ANSI_COLOR_RESET)
#else
#define DEBUG_MESSAGE(str) /* foo */
#define DEBUG_ERROR_MESSAGE(str) /* foo */
#define SIGNAL_MESSAGE(str, signum) /* foo */
#endif

#include "headers/global.h"
#include "headers/sem.h"
#include "headers/msg_request.h"

#define MAX_TIME_NEXT_ALARM 5

#define STATE_INIT 0 /* Stato di inizializzazione */
#define STATE_RUNNING 1 /* Stato di esecuzione */

/* Prototipi */
void handler(int signum);
void free_sysV();

/* Cordinate della source */
struct point position;

struct shm* shm_p; /* Puntatore alla shared memory*/
int msg_requests_id = -1; /* Identificativo della coda di messaggi per le richieste */
int sem_start_id = -1; /* Identificativo del semaforo di sincronizzazione */
int state; /*Stato della source */

int main(int argc, char const *argv[])
{
    int shm_id = -1; /*Identificativo della shared memory*/
    struct sigaction sa; /* Struttura per la gestione dei segnali */
    sigset_t sigset;

    /* == INIZIALIZZAZIONE == */
    /* Ottenimento le coordinate della source */
    if(argc < 3) {
        ERROR_EXIT_SEQ("Missing row, col parameters");
    }
    position.row = atoi(argv[1]);
    position.col = atoi(argv[2]);

#ifdef DEBUG
    fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> coordinate: [%d][%d]\n%s", ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, position.row, position.col, ANSI_COLOR_RESET);
#endif

    state = STATE_INIT;

    sigemptyset(&sigset);
    sigaddset(&sigset, SIGALRM);

    /* Intercettazione dei segnali SIGINT, SIGALARM, SIGUSR1 */
    memset(&sa, 0, sizeof(sa));
    sa.sa_handler = handler;
    sa.sa_mask = sigset;
    if(sigaction(SIGINT, &sa, NULL) < 0 ||
       sigaction(SIGALRM, &sa, NULL) < 0 ||
       sigaction(SIGUSR1, &sa, NULL) < 0) {
        ERROR_EXIT_SEQ("Errore nella creazione dell'handler dei segnali");
    }

    srand(getpid());

    /* Connessione alla coda di messaggi */
    msg_requests_id = msgget(MSG_REQUESTS_KEY, 0600);
    if(msg_requests_id < 0) {
        ERROR_EXIT_SEQ("Impossibile accedere alla coda delle richieste");
    }
    /* Connessione alla shared memory */
    shm_id = shmget(SHM_KEY, sizeof(struct shm), 0600);
    if(shm_id < 0) {
        ERROR_EXIT_SEQ("Impossibile accedere alla shared memory");
    }
    /* Attaccamento alla shared memory */
    shm_p = shmat(shm_id, NULL, 0);
    if(shm_p == (void*) -1) {
        ERROR_EXIT_SEQ("impossibile collegarsi alla shared memory");
    }
    /* Sincronizzazione con il main */
    sem_start_id = semget(SEM_START_KEY, 1, 0);
    if(sem_lock(sem_start_id, 0) < 0){
        ERROR_EXIT_SEQ("Impossibile effettuare la lock del semaforo");
    }
    /* ====================== */

    DEBUG_MESSAGE("pronto, attesa sul semaforo...");
    /* Attesa dello sblocco del semaforo da parte degli altri processi */
    if(sem_wait_for_0(sem_start_id, 0) < 0) {
        ERROR_EXIT_SEQ("Impossibile effettuare l'operazione di wait until 0");
    }

    state = STATE_RUNNING;

    /* Invio del primo mesaggio */
    raise(SIGALRM);

    while(1) pause();

    return 0; /* Necessario per evitare il warning */
}

void free_sysV()
{
    int sem_start_value;
    /* Se il processo si trova in STATE_INIT, significa che la sua terminazione potrebbe andare a
     * bloccare l'inizio della simulazione. Deve essere eseguita la lock del semaforo di start.
     * Se l'errore è legato all'impossibilità di prelevare l'id del semaforo di start, nessuna procedura
     * può essere messa in atto per risolvere il problema in questo punto */
    if(state == STATE_INIT && sem_start_id >= 0) {
        /* Controllo se il semaforo è già a zero */
        sem_start_value = semctl(sem_start_id, 0, GETVAL);
        if (sem_start_value < 0) {
            /* Errore */
            ERROR_MESSAGE("Impossibile prelevare le informazioni dal semaforo di start");
        } else if (sem_start_value > 0) {
            if (sem_lock(sem_start_id, 0) < 0) {
                /* Errore */
                ERROR_MESSAGE("La procedura di cancellazione delle strutture sysV non è riuscita a eseguire la lock sul semaforo start");
            }
        }
    }
}

void handler(int signum)
{
    struct msg_request msg_send;

    SIGNAL_MESSAGE("ricevuto segnale", signum);
    switch (signum) {
        case SIGINT: /* In caso di SIGINT si effettua una exit */
            alarm(0);
            EXIT_SEQ(0);
        case SIGALRM: /* In caso di SIGLRM si inserisce un nuovo messaggio nella coda e si richaiama un altro alarm */
            if(msg_request_send(msg_requests_id, &msg_send, shm_p, position) == -1)
                ERROR_MESSAGE("Impossibile inserire messaggio in coda");
            else
#ifdef DEBUG
#ifdef ENHANCED_PRINT
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> 📤 inviato un messaggio di request\n-> 📧 {type = %ld | destination = [%d][%d]}\n%s",
                        ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_send.type, msg_send.destination.row, msg_send.destination.col, ANSI_COLOR_RESET);
#else
                fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> inviato un messaggio di request\n-> {type = %ld | destination = [%d][%d]}\n%s",
                        ANSI_COLOR_RESET, ANSI_COLOR_GREEN, getpid(), __FILE__, __LINE__, msg_send.type, msg_send.destination.row, msg_send.destination.col, ANSI_COLOR_RESET);
#endif
#endif
            /* Impostazione del prossimo alarm in un intervallo compreso tra 1 e MAX_TIME_NEXT_ALARM secondi */
            alarm(rand() % MAX_TIME_NEXT_ALARM + 1);
            break;
        case SIGUSR1: /* Se si riceve un SIGUSR1 si immette una richiesta nella coda */
            if(msg_request_send(msg_requests_id, &msg_send, shm_p, position) == -1)
                ERROR_MESSAGE("Impossibile inserire messaggio in coda");
            else
#ifdef ENHANCED_PRINT
                printf("[Messaggio | pid: %d | file: %s | line: %d]\n-> 📤 inviato un messaggio di request\n-> {type = %ld | destination = [%d][%d]}\n\n", getpid(), __FILE__, __LINE__, msg_send.type, msg_send.destination.row, msg_send.destination.col);
#else
                printf("[Messaggio | pid: %d | file: %s | line: %d]\n-> inviato un messaggio di request\n-> {type = %ld | destination = [%d][%d]}\n\n", getpid(), __FILE__, __LINE__, msg_send.type, msg_send.destination.row, msg_send.destination.col);
#endif
            break;
        default: break; /* Necessario per evitare warning */
    }
}
