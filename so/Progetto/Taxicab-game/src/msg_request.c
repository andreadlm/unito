#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <stdio.h>
#include <sys/msg.h>
#include <sys/types.h>

#include "./headers/msg_request.h"
#include "./headers/global.h"

#ifdef DEBUG
#define DEBUG_ERROR_MESSAGE(str) fprintf(stderr, "%s%s[Debug | pid: %d | file: %s | line: %d]\n-> %s\n%s", ANSI_COLOR_RESET, ANSI_COLOR_CYAN, getpid(), __FILE__, __LINE__, str, ANSI_COLOR_RESET)
#else
#define DEBUG_ERROR_MESSAGE(str) /* foo */
#endif

/* Funzione per la creazione del messaggio da inviare sulla coda di richieste */
void msg_request_costructor(struct msg_request* self, struct shm* shm_p, struct point source_position)
{
    struct point destination;
    self->type = (long)shm_p->map[source_position.row][source_position.col].sem_access_idx + 1; /* type non puo' essere 0 */

    do {
        destination.row = rand() % SO_HEIGHT;
        destination.col = rand() % SO_WIDTH;

        if(shm_p->map[destination.row][destination.col].type == CELL_TYPE_HOLE) {
            if(destination.col == SO_WIDTH -1) destination.col--;
            else destination.col++;
        }
    } while(destination.row == source_position.row && destination.col == source_position.col);
    
    self->destination.row = destination.row;
    self->destination.col = destination.col;
}

/* Funzione di stampa di un messaggio di richiesta */
void msg_request_print(struct msg_request* self) {
#ifdef ENHANCED_PRINT
    printf("📧 {type = %ld | x_destination = %d | y_destination = %d }\n",
            self->type,
            self->destination.row,
            self->destination.col);
#else
    printf("{type = %ld | x_destination = %d | y_destination = %d }\n",
            self->type,
            self->destination.row,
            self->destination.col);
#endif
}

/* Funzione per l'invio di messaggi sulla coda delle richieste
 * Viene ripetuta la msgsnd in caso di interruzione della medesima  */
int msg_request_send(int q_id, struct msg_request * msg, struct shm* shm_p, struct point source_position)
{
    msg_request_costructor(msg, shm_p, source_position);

    while(msgsnd(q_id, msg, sizeof(struct msg_request) - sizeof(long) , 0) < 0)
        if(errno != EINTR) return -1;

    return 0;
}

/* Funzione per la ricezione di messaggi dalla coda delle richieste */
int msg_request_rcv(int q_id, struct msg_request * msg, int type)
{
    if(msgrcv(q_id, msg, sizeof(struct msg_request) - sizeof(long), type, IPC_NOWAIT) < 0) {
        if(errno == ENOMSG) {
            DEBUG_ERROR_MESSAGE("Nessun messaggio nella coda");
        }
        return -1;
    }

    return 0;
}