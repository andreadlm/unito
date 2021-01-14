#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>

#include "headers/global.h"
#include "headers/msg_report.h"


/* Costruttore del messaggio di report */
void msg_report_constructor(struct msg_report* self,
                            long type,
                            int n_crossed_cells,
                            long time,
                            pid_t sender_pid,
                            short int state)
{
    self->type = type;
    self->n_crossed_cells = n_crossed_cells;
    self->time = time;
    self->sender_pid = sender_pid;
    self->state = state;
}

/* Metodo di stampa di un messaggio di report */
void msg_report_print(struct msg_report* self)
{
#ifdef ENHANCED_PRINT
    if(self->type == MSG_REPORT_TYPE_ABORTED)
        printf("📧 {type = 💥 | n_crossed_cells = %d | time = %ld | sender_pid = %d | state = %d}\n",
               self->n_crossed_cells,
               self->time,
               self->sender_pid,
               self->state);
    else if(self->type == MSG_REPORT_TYPE_SUCCESSFULL)
        printf("📧 {type = 📄 | n_crossed_cells = %d | time = %ld | sender_pid = %d | state = %d}\n",
               self->n_crossed_cells,
               self->time,
               self->sender_pid,
               self->state);

#else
    printf("{type = %ld | n_crossed_cells = %d | time = %ld | sender_pid = %d | state = %d}\n",
           self->type,
           self->n_crossed_cells,
           self->time,
           self->sender_pid,
           self->state);
#endif
}