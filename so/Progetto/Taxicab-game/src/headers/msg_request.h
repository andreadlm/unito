#ifndef TAXICAB_GAME_MSG_REQUEST_H
#define TAXICAB_GAME_MSG_REQUEST_H

#include "shm.h"

/* Struttura del messaggio nella coda di messaggi delle richieste */
struct msg_request {
    long type;
    struct point destination;
};

void msg_request_costructor(struct msg_request* self, struct shm* shm_p, struct point source_position);
void msg_request_print(struct msg_request* self);
int msg_request_send(int q_id, struct msg_request * msg, struct shm* shm_p, struct point source_position);
int msg_request_rcv(int q_id, struct msg_request * msg, int type);
#endif
