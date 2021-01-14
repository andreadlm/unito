#include <sys/sem.h>
#include <stdio.h>
#include "headers/cell.h"
#include "headers/global.h"

/* Metodo "costruttore" di una cella */
short int cell_constructor(struct cell *self,
                           short int type,
                           int crossing_time,
                           int sem_access_idx,
                           int sems_access_id,
                           int sems_counter_id,
                           int capacity)
{
    self->type = type;
    self->sem_access_idx = type == CELL_TYPE_HOLE ? -1 : sem_access_idx;
    if(type != CELL_TYPE_HOLE) {
        /* Informazioni utili solo ad una cella non buca */
        self->crossing_time = crossing_time;
        self->capacity = capacity;
        if(semctl(sems_access_id, sem_access_idx, SETVAL, capacity) < 0) return -1;
        if(semctl(sems_counter_id, sem_access_idx, SETVAL, 0) < 0) return -1;
    }

    return 0;
}

/* Metodo di stampa di una cella: fornisce informazioni sul tipo della cella */
void cell_print_type(struct cell* self)
{
#ifdef ENHANCED_PRINT /* Modo 'gggiovane di stampare la matrice */
    if(self->type == CELL_TYPE_HOLE) printf("[🚧]");
    else if(self->type == CELL_TYPE_ROAD) printf("[⚫]");
    else if(self->type == CELL_TYPE_SOURCE) printf("[🏰]");
#else
    if (self->type == CELL_TYPE_HOLE) printf("%s[h]%s", ANSI_COLOR_RED, ANSI_COLOR_RESET);
    else if (self->type == CELL_TYPE_ROAD) printf("[r]");
    else if (self->type == CELL_TYPE_SOURCE) printf("%s[s]%s", ANSI_COLOR_CYAN, ANSI_COLOR_RESET);
#endif
}

int cell_print_state(struct cell* self, int sem_access_id)
{
    int sem_val = 0;
#ifndef ENHANCED_PRINT
    char* highline_before_num;
    char* highline_after_num;
#endif

    /* Prelevo il valore corrente del semaforo */
    if(self->sem_access_idx >= 0) {
        sem_val = semctl(sem_access_id, self->sem_access_idx, GETVAL);
        if (sem_val < 0) return -1;
    }

#ifdef ENHANCED_PRINT
    if(self->type == CELL_TYPE_HOLE) printf("[🚧|  ]");
    else if(self->type == CELL_TYPE_ROAD) printf("[⚫|%s%2d%s]", self->capacity - sem_val == 0 ? ANSI_COLOR_GREEN : ANSI_COLOR_RED, self->capacity - sem_val, ANSI_COLOR_RESET);
    else if(self->type == CELL_TYPE_SOURCE) printf("[🏰|%s%2d%s]", self->capacity - sem_val == 0 ? ANSI_COLOR_GREEN : ANSI_COLOR_RED, self->capacity - sem_val, ANSI_COLOR_RESET);
#else
    if (self->type == CELL_TYPE_HOLE) {
        printf("%s[h|  ]%s", ANSI_COLOR_RED, ANSI_COLOR_RESET);
    } else if (self->type == CELL_TYPE_ROAD) {
        if(self->capacity - sem_val > 0) highline_before_num = ANSI_COLOR_YELLOW;
        else highline_before_num = "";
        highline_after_num = ANSI_COLOR_RESET;
        printf("[r|%s%2d%s]", highline_before_num, self->capacity - sem_val, highline_after_num);
    } else if (self->type == CELL_TYPE_SOURCE) {
        if(self->capacity - sem_val > 0) highline_before_num = ANSI_COLOR_YELLOW;
        else highline_before_num = "";
        highline_after_num = ANSI_COLOR_CYAN;
        printf("%s[s|%s%2d%s]%s", ANSI_COLOR_CYAN, highline_before_num, self->capacity - sem_val, highline_after_num, ANSI_COLOR_RESET);
    }
#endif

    return self->capacity - sem_val;
}

/* Metodo che stamap le top cells della mappa formattate */
void cell_print_top(struct cell* self)
{
    char* highline_before;
    char* highline_after;
#ifdef ENHANCED_PRINT
    if(self->type != CELL_TYPE_HOLE && self->type & CELL_TOP_FLAG) {
        highline_before = ANSI_COLOR_MAGENTA;
        highline_after = ANSI_COLOR_RESET;
        self->type = self->type & (~CELL_TOP_FLAG);
    } else {
        highline_before = ANSI_COLOR_RESET;
        highline_after = ANSI_COLOR_RESET;
    }
    if(self->type == CELL_TYPE_HOLE) printf("[🚧]");
    else if(self->type == CELL_TYPE_ROAD) printf("%s[⚫]%s", highline_before, highline_after);
    else if(self->type == CELL_TYPE_SOURCE) printf("%s[🏰]%s", highline_before, highline_after);
#else
    if(self->type != CELL_TYPE_HOLE && self->type & CELL_TOP_FLAG) {
        highline_before = ANSI_COLOR_MAGENTA;
        highline_after = ANSI_COLOR_RESET;
        self->type = self->type & (~CELL_TOP_FLAG);
    } else {
        if(self->type == CELL_TYPE_SOURCE) {
            highline_before = ANSI_COLOR_CYAN;
            highline_after = ANSI_COLOR_RESET;
        } else {
            highline_before = ANSI_COLOR_RESET;
            highline_after = ANSI_COLOR_RESET;
        }
    }
    if(self->type == CELL_TYPE_HOLE) printf("[h]");
    else if(self->type == CELL_TYPE_ROAD) printf("%s[%sr%s]%s", highline_before, ANSI_COLOR_RESET, highline_before, highline_after);
    else if(self->type == CELL_TYPE_SOURCE) printf("%s[%ss%s]%s", highline_before, ANSI_COLOR_CYAN, highline_before, highline_after);
#endif
}

/* Metodo di stampa di una cella: fornisce informazioni complete sulla cella */
void cell_print(struct cell* self)
{
    printf("{type: %hi | crossing_time = %d | sem_access_idx = %d | capacity = %d}\n",
           self->type, self->crossing_time, self->sem_access_idx, self->capacity);
}