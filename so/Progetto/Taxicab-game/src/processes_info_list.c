#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>

#include "headers/global.h"
#include "headers/processes_info_list.h"

/* La funzione inserisce in testa alla lista un nuovo record associato ad
 * un processo creato */
struct processes_info_list* processes_info_list_insert(struct processes_info_list* self, pid_t pid, short int type)
{
    struct processes_info_list* new_el;
    new_el = (struct processes_info_list*)malloc(sizeof(*new_el));

    new_el->pid = pid;
    new_el->type = type;
    new_el->state = PROCESS_INFO_STATE_RUNNING;
    new_el->n_crossed_cells = 0;
    new_el->longest_ride = 0;
    new_el->n_requests = 0;
    new_el->next = self;

    return new_el;
}

/* La funzione stampa il contenuto della lista */
void processes_info_list_print(struct processes_info_list* self)
{
    for(; self != NULL; self = self->next) {
        process_info_print(self);
        if(self->next != NULL) printf(" ->\n");
    }
    printf("\n");
}

/* La funzione ritorna l'elemento della lista associato ad un pid */
struct processes_info_list* processes_info_list_get_from_pid(struct processes_info_list* self, pid_t pid)
{
    for(; self != NULL; self = self->next)
        if(self->pid == pid) return self;
    return NULL;
}

/* La funzione ritorna il pid e il numero di celle attraversate del processo che ha attraversato
 * più celle */
void process_info_list_get_best_n_crossed_cells(struct processes_info_list* self, pid_t* pid, int* n_crossed_cells)
{
    *n_crossed_cells = -1;
    for(; self != NULL; self = self->next)
        if(self->type == PROCESS_INFO_TYPE_TAXI && self->n_crossed_cells > *n_crossed_cells) {
            *n_crossed_cells = self->n_crossed_cells;
            *pid = self->pid;
        }
}

/* La funzione ritorna il pid e il numero richieste del processo che ha evaso più richieste */
void process_info_list_get_best_n_requests(struct processes_info_list* self, pid_t* pid, int* n_requests)
{
    *n_requests = -1;
    for(; self != NULL; self = self->next)
        if(self->type == PROCESS_INFO_TYPE_TAXI && self->n_requests > *n_requests) {
            *n_requests = self->n_requests;
            *pid = self->pid;
        }
}

/* La funzione ritorna il pid e il numero di celle attraversate del processo che ha completato
 * la corsa più lunga */
void process_info_list_get_best_longest_ride(struct processes_info_list* self, pid_t* pid, long* longest_ride)
{
    *longest_ride = -1;
    for(; self != NULL; self = self->next)
        if(self->type == PROCESS_INFO_TYPE_TAXI && self->longest_ride > *longest_ride) {
            *longest_ride = self->longest_ride;
            *pid = self->pid;
        }
}

/* La funzione stampa un singolo elemento della lista */
void process_info_print(struct processes_info_list* self)
{
    if(self != NULL)
#ifdef ENHANCED_PRINT
        printf("{pid = %d | type = %s | state = %s | n_crossed_cells = %d | longest_ride = %ld | n_requests = %d}",
               self->pid,
               self->type == PROCESS_INFO_TYPE_TAXI ? "🚕" : "🏰",
               self->state == PROCESS_INFO_STATE_RUNNING ? "🟢" : "🔴",
               self->n_crossed_cells,
               self->longest_ride,
               self->n_requests);
#else
        printf("{pid = %d | type = %d | state = %d | n_crossed_cells = %d | longest_ride = %ld | n_requests = %d}",
               self->pid, self->type, self->state, self->n_crossed_cells, self->longest_ride, self->n_requests);
#endif
}

/* La funzione ripulisce la memoria dalla lista */
void processes_info_list_free(struct processes_info_list* self)
{
    if(self == NULL) return;
    processes_info_list_free(self->next);
    free(self);
}

/* La funzione cambia il flag state di un record della lista */
void processes_info_list_set_state(struct processes_info_list* self, pid_t pid, short int state)
{
    for(; self != NULL; self = self->next)
        if(self->pid == pid) self->state = state;
}