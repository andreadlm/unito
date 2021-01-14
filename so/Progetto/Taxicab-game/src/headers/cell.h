#ifndef TAXICAB_GAME_CELL_H
#define TAXICAB_GAME_CELL_H

#define CELL_TYPE_HOLE -1
#define CELL_TYPE_ROAD 0
#define CELL_TYPE_SOURCE 1
#define CELL_TOP_FLAG 0x4

/* Struttura cella, la cella rappresenta la singola unità di una mappa */
struct cell {
    short int type; /* Tipo della cella: può essere HOLE, ROAD, SOURCE */
    int crossing_time; /* Tempo di attraversamento della cella */
    int sem_access_idx; /* Indice nell'array di semafori che regola gli accessi alle celle */
    int capacity; /* Numero massimo di taxi che possono transitare contemporaneamente sulla cella */
};

short int cell_constructor(struct cell *self, short int type, int crossing_time, int sem_access_idx, int sems_access_id,
                           int sems_counter_id, int capacity);
void cell_print_type(struct cell* self);
void cell_print(struct cell* self);
int cell_print_state(struct cell* self, int sem_access_id);
void cell_print_top(struct cell* self);
#endif /* TAXICAB_GAME_CELL_H */