#ifndef TAXICAB_GAME_MSG_REPORT_H
#define TAXICAB_GAME_MSG_REPORT_H

#define MSG_REPORT_TYPE_SUCCESSFULL 1
#define MSG_REPORT_TYPE_ABORTED 2

#define STATE_INIT 0 /* Stato di inizializzazione di un taxi */
#define STATE_FREE 1 /* Stato libero, nessuna corsa ancora accettata dal taxi */
#define STATE_SERVING_REQ 2 /* Stato di esecuzione di una corsa da parte del taxi */

struct msg_report {
    long type;
    int n_crossed_cells;
    long time;
    pid_t sender_pid;
    short int state;
};

void msg_report_constructor(struct msg_report* self,
                            long type,
                            int n_crossed_cells,
                            long time,
                            pid_t sender_pid,
                            short int state);
void msg_report_print(struct msg_report* self);
#endif /* TAXICAB_GAME_MSG_REPORT_H */